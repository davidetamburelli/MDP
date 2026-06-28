package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;

public class FileGameRepositoryTest {

    @TempDir
    Path tempDir;

    private GameWorld newGame() {
        return new GameWorldFactory().createDefaultGame(PlayerClass.WARRIOR);
    }

    @Test
    void saveAndLoadPreservesState() {
        GameRepository repo = new FileGameRepository(tempDir);
        GameWorld original = newGame();
        repo.save("slot1", SaveGame.from(original));

        GameWorld loaded = repo.load("slot1").toGameWorld();
        assertEquals(new Position(1, 1), loaded.getPlayer().getPosition());
        assertEquals(120, loaded.getPlayer().getHp());
        assertEquals(3, loaded.getEnemies().size());
    }

    @Test
    void loadMissingSlotThrowsException() {
        GameRepository repo = new FileGameRepository(tempDir);
        assertThrows(PersistenceException.class, () -> repo.load("doesNotExist"));
    }

    @Test
    void listSlotsReturnsAllSavedSlotsSorted() {
        GameRepository repo = new FileGameRepository(tempDir);
        repo.save("slotB", SaveGame.from(newGame()));
        repo.save("slotA", SaveGame.from(newGame()));
        assertEquals(List.of("slotA", "slotB"), repo.listSlots());
    }

    @Test
    void deleteRemovesSlot() {
        GameRepository repo = new FileGameRepository(tempDir);
        repo.save("slot1", SaveGame.from(newGame()));
        repo.delete("slot1");
        assertFalse(repo.exists("slot1"));
        assertTrue(repo.listSlots().isEmpty());
    }

    @Test
    void listSlotsOnEmptyDirectoryReturnsEmpty() {
        GameRepository repo = new FileGameRepository(tempDir);
        assertTrue(repo.listSlots().isEmpty());
    }
}
