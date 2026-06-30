package it.unicam.cs.mpgc.rpg125681.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class JsonLeaderboardRepository implements LeaderboardRepository {

    private static final Type LIST_TYPE = new TypeToken<List<RunRecord>>() {}.getType();

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonLeaderboardRepository(Path file) {
        this.file = Objects.requireNonNull(file, "file");
    }

    @Override
    public void add(RunRecord record) {
        Objects.requireNonNull(record, "record");
        List<RunRecord> all = new ArrayList<>(readAll());
        all.add(record);
        writeAll(all);
    }

    @Override
    public List<RunRecord> findAll() {
        List<RunRecord> all = new ArrayList<>(readAll());
        all.sort(Comparator.comparingLong(RunRecord::timestamp).reversed());
        return all;
    }

    @Override
    public List<RunRecord> findByOutcome(RunOutcome outcome) {
        Objects.requireNonNull(outcome, "outcome");
        return findAll().stream().filter(record -> record.outcome() == outcome).toList();
    }

    private List<RunRecord> readAll() {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try {
            String json = Files.readString(file);
            List<RunRecord> parsed = gson.fromJson(json, LIST_TYPE);
            return parsed != null ? parsed : new ArrayList<>();
        } catch (IOException e) {
            throw new PersistenceException("Can't read the leaderboard: " + file, e);
        }
    }

    private void writeAll(List<RunRecord> records) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, gson.toJson(records, LIST_TYPE));
        } catch (IOException e) {
            throw new PersistenceException("Can't write the leaderboard: " + file, e);
        }
    }
}