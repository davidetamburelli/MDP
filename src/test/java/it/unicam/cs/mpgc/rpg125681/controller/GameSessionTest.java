package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.shop.Shop;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.MapGenerator;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.persistence.FileSessionRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.JsonLeaderboardRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.LeaderboardRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @TempDir
    Path tempDir;

    private GameSession newSession() {
        GameWorldFactory factory = new GameWorldFactory(new MapGenerator(new Random(7)));
        LeaderboardRepository leaderboard = new JsonLeaderboardRepository(tempDir.resolve("lb.json"));
        SessionRepository sessions = new FileSessionRepository(tempDir);
        return new GameSession(factory, leaderboard, new Shop(), sessions);
    }

    @Test
    void startsInMenu() {
        assertEquals(GamePhase.MENU, newSession().getPhase());
    }

    @Test
    void newGameGoesToHubAndCreatesPlayer() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        assertEquals(GamePhase.HUB, session.getPhase());
        assertNotNull(session.getPlayer());
    }

    @Test
    void enterDungeonStartsAtDepthOne() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();
        assertEquals(GamePhase.DUNGEON, session.getPhase());
        assertEquals(1, session.getDepth());
        assertEquals(1, session.getPlayer().getMaxDepthReached());
    }

    @Test
    void enteringDungeonFromMenuThrows() {
        GameSession session = newSession();
        assertThrows(IllegalStateException.class, session::enterDungeon);
    }

    @Test
    void winningRunRecordsVictoryAndGoesToRecap() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();
        while (session.getPhase() == GamePhase.DUNGEON) {
            session.getWorld().getEnemies().forEach(e -> e.takeDamage(99999));
            session.handleMove(Direction.RIGHT);
        }
        assertEquals(GamePhase.RUN_RECAP, session.getPhase());
        assertEquals(RunOutcome.VICTORY, session.getLastRun().outcome());
        assertEquals(1, session.getRunHistory().size());
    }

    @Test
    void returningToHubKeepsTheSamePlayer() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        var profile = session.getPlayer();
        session.enterDungeon();
        session.returnToHub();
        assertEquals(GamePhase.HUB, session.getPhase());
        assertSame(profile, session.getPlayer());
        assertNull(session.getWorld());
        assertEquals(1, session.getPlayer().getMaxDepthReached());
    }

    @Test
    void savingFromMenuThrows() {
        assertThrows(IllegalStateException.class, () -> newSession().save("slot1"));
    }

    @Test
    void saveAndLoadFromHubRestoresProfile() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.getPlayer().addGold(120);
        session.enterDungeon();
        session.returnToHub();                 // maxDepthReached = 1
        session.save("slot1");

        GameSession loaded = newSession();
        loaded.load("slot1");
        assertEquals(GamePhase.HUB, loaded.getPhase());
        assertNull(loaded.getWorld());
        assertEquals(120, loaded.getPlayer().getGold());
        assertEquals(1, loaded.getPlayer().getMaxDepthReached());
    }

    @Test
    void saveAndLoadFromDungeonResumesRun() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();
        int enemyCount = session.getWorld().getEnemies().size();
        session.save("slot1");

        GameSession loaded = newSession();
        loaded.load("slot1");
        assertEquals(GamePhase.DUNGEON, loaded.getPhase());
        assertEquals(1, loaded.getDepth());
        assertNotNull(loaded.getWorld());
        assertEquals(enemyCount, loaded.getWorld().getEnemies().size());
    }

    @Test
    void handlePickUpCollectsDroppedItems() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();
        Position pos = session.getPlayer().getPosition();
        session.getWorld().dropItem(pos, new Item(ItemType.POTION_MINOR));
        int before = session.getPlayer().getInventory().getItems().size();

        session.handlePickUp();

        assertEquals(before + 1, session.getPlayer().getInventory().getItems().size());
    }

    @Test
    void savingDungeonPreservesDroppedItems() {
        GameSession session = newSession();
        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();
        Position pos = session.getPlayer().getPosition();
        session.getWorld().dropItem(pos, new Item(ItemType.POTION_MINOR));
        session.save("slot1");

        GameSession loaded = newSession();
        loaded.load("slot1");
        assertEquals(1, loaded.getWorld().getItemsAt(pos).size());
    }
}