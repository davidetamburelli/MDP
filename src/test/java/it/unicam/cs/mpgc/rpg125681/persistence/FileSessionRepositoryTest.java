package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.MapGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileSessionRepositoryTest {

    @TempDir
    Path tempDir;

    private SessionState hubState() {
        GameWorldFactory factory = new GameWorldFactory(new MapGenerator(new Random(7)));
        return new SessionState(GamePhase.HUB, 0, factory.createPlayer(PlayerClass.WARRIOR), null);
    }

    @Test
    void saveAndLoadPreservesPhaseAndDepth() {
        SessionRepository repo = new FileSessionRepository(tempDir);
        repo.save("slot1", new SessionState(GamePhase.DUNGEON, 3, hubState().profile(), null));
        SessionState loaded = repo.load("slot1");
        assertEquals(GamePhase.DUNGEON, loaded.phase());
        assertEquals(3, loaded.depth());
        assertNotNull(loaded.profile());
    }

    @Test
    void loadMissingSlotThrows() {
        SessionRepository repo = new FileSessionRepository(tempDir);
        assertThrows(PersistenceException.class, () -> repo.load("nope"));
    }

    @Test
    void listSlotsReturnsSavedSlotsSorted() {
        SessionRepository repo = new FileSessionRepository(tempDir);
        repo.save("slotB", hubState());
        repo.save("slotA", hubState());
        assertEquals(List.of("slotA", "slotB"), repo.listSlots());
    }

    @Test
    void deleteRemovesSlot() {
        SessionRepository repo = new FileSessionRepository(tempDir);
        repo.save("slot1", hubState());
        repo.delete("slot1");
        assertFalse(repo.exists("slot1"));
    }
}