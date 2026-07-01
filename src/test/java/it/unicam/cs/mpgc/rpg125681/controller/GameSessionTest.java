package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.shop.Shop;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.MapGenerator;
import it.unicam.cs.mpgc.rpg125681.persistence.JsonLeaderboardRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.LeaderboardRepository;
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
        return new GameSession(factory, leaderboard, new Shop());
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
}