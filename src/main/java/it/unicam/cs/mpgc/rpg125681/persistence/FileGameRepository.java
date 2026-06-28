package it.unicam.cs.mpgc.rpg125681.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileGameRepository implements GameRepository {

    private static final String EXTENSION = ".sav";
    private final Path directory;

    public FileGameRepository(Path directory) {
        this.directory = Objects.requireNonNull(directory, "directory");
    }

    @Override
    public void save(String slot, SaveGame data) {
        Objects.requireNonNull(data, "data");
        try {
            Files.createDirectories(directory);
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fileFor(slot)))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new PersistenceException("Save failed for slot: " + slot, e);
        }
    }

    @Override
    public SaveGame load(String slot) {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fileFor(slot)))) {
            return (SaveGame) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException("Load failed for slot: " + slot, e);
        }
    }

    @Override
    public List<String> listSlots() {
        if (!Files.isDirectory(directory)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(directory)) {
            return files
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.endsWith(EXTENSION))
                    .map(name -> name.substring(0, name.length() - EXTENSION.length()))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new PersistenceException("Saves folder couldn't be read", e);
        }
    }

    @Override
    public boolean exists(String slot) {
        return Files.exists(fileFor(slot));
    }

    @Override
    public void delete(String slot) {
        try {
            Files.deleteIfExists(fileFor(slot));
        } catch (IOException e) {
            throw new PersistenceException("Delete failed for slot: " + slot, e);
        }
    }

    private Path fileFor(String slot) {
        if (slot == null || slot.isBlank()) {
            throw new IllegalArgumentException("Slot name is invalid");
        }
        return directory.resolve(slot + EXTENSION);
    }
}
