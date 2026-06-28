package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameStatus;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.persistence.FileGameRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

public class GameControllerTest {

    @TempDir
    Path tempDir;

    private static final class CountingObserver implements GameObserver {
        private int count = 0;
        @Override
        public void onGameChanged(GameController controller) {
            this.count++;
        }
        int count() { return this.count; }
    }

    private GameController newController() {
        GameRepository repository = new FileGameRepository(tempDir);
        return new GameController(new GameWorldFactory(), repository);
    }

    @Test
    void newGameStartsRunningGameAndNotifies() {
        GameController controller = newController();
        CountingObserver observer = new CountingObserver();
        controller.addObserver(observer);

        controller.newGame(PlayerClass.WARRIOR);

        assertNotNull(controller.getWorld());
        assertEquals(GameStatus.RUNNING, controller.status());
        assertEquals(1, observer.count());
    }

    @Test
    void handleMoveAdvancesTurnAndNotifies() {
        GameController controller = newController();
        CountingObserver observer = new CountingObserver();
        controller.addObserver(observer);
        controller.newGame(PlayerClass.WARRIOR);

        controller.handleMove(Direction.RIGHT);

        assertEquals(new Position(2, 1), controller.getWorld().getPlayer().getPosition());
        assertEquals(2, observer.count());
    }

    @Test
    void handleMoveWithoutActiveGameIsIgnored() {
        GameController controller = newController();
        CountingObserver observer = new CountingObserver();
        controller.addObserver(observer);

        controller.handleMove(Direction.RIGHT);

        assertNull(controller.getWorld());
        assertEquals(0, observer.count());
    }

    @Test
    void statusWithoutActiveGameThrowsException() {
        GameController controller = newController();
        assertThrows(IllegalStateException.class, controller::status);
    }

    void saveAndLoadThroughControllerPreservesState() {
        GameController controller = newController();
        controller.newGame(PlayerClass.WARRIOR);
        controller.handleMove(Direction.RIGHT);
        controller.save("slot1");

        GameController other = newController();
        other.load("slot1");

        assertEquals(new Position(2, 1), other.getWorld().getPlayer().getPosition());
    }
}
