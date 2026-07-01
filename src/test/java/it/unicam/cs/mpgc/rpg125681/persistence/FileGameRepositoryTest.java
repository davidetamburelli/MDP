package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.MapGenerator;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileGameRepositoryTest {

    @TempDir
    Path tempDir;

    private GameWorld newGame() {
        GameWorldFactory factory = new GameWorldFactory(new MapGenerator(new Random(7)));
        return factory.createFirstLevel(PlayerClass.WARRIOR);
    }

    @Test
    void saveAndLoadPreservesState() {
        GameRepository repo = new FileGameRepository(tempDir);
        GameWorld original = newGame();
        Position playerPos = original.getPlayer().getPosition();
        int playerHp = original.getPlayer().getHp();
        int enemyCount = original.getEnemies().size();

        repo.save("slot1", SaveGame.from(original));
        GameWorld loaded = repo.load("slot1").toGameWorld();

        assertEquals(playerPos, loaded.getPlayer().getPosition());
        assertEquals(playerHp, loaded.getPlayer().getHp());
        assertEquals(enemyCount, loaded.getEnemies().size());
    }

    @Test
    void loadMissingSlotThrows() {
        GameRepository repo = new FileGameRepository(tempDir);
        assertThrows(PersistenceException.class, () -> repo.load("nonEsiste"));
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

    @Test
    void droppedItemsSurviveSaveLoad() {
        GameRepository repo = new FileGameRepository(tempDir);
        GameWorld original = newGame();
        original.dropItem(new it.unicam.cs.mpgc.rpg125681.model.world.Position(1, 1),
                new it.unicam.cs.mpgc.rpg125681.model.item.Item(it.unicam.cs.mpgc.rpg125681.model.item.ItemType.POTION_MINOR));

        repo.save("slot1", SaveGame.from(original));
        GameWorld loaded = repo.load("slot1").toGameWorld();

        assertEquals(1, loaded.getItemsAt(new it.unicam.cs.mpgc.rpg125681.model.world.Position(1, 1)).size());
    }
}