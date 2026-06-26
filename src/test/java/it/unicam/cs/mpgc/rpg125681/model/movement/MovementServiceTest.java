package it.unicam.cs.mpgc.rpg125681.model.movement;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MovementServiceTest {

    private GameMap bordered() {
        return GameMap.fromRows(List.of("#####", "#...#", "#.#.#", "#...#", "#####"));
    }

    @Test
    void validMoveReturnsMovedAndUpdatesPosition() {
        MovementService service = new MovementService(bordered());
        Player hero = new Warrior(new Position(1, 1), 1);
        MoveOutcome outcome = service.move(hero, Direction.RIGHT);
        assertEquals(MoveOutcome.MOVED, outcome);
        assertEquals(new Position(2, 1), hero.getPosition());
    }

    @Test
    void moveIntoWallIsBlockedAndPositionUnchanged() {
        MovementService service = new MovementService(bordered());
        Player hero = new Warrior(new Position(1, 1), 1);
        MoveOutcome outcome = service.move(hero, Direction.UP);
        assertEquals(MoveOutcome.BLOCKED_BY_WALL, outcome);
        assertEquals(new Position(1, 1), hero.getPosition());
    }

    @Test
    void moveOutsideBoundsReturnsOutOfBoundsAndPositionUnchanged() {
        GameMap open = GameMap.fromRows(List.of("...", "...", "..."));
        MovementService service = new MovementService(open);
        Player hero = new Warrior(new Position(0, 0), 1);
        MoveOutcome outcome = service.move(hero, Direction.LEFT);
        assertEquals(MoveOutcome.OUT_OF_BOUNDS, outcome);
        assertEquals(new Position(0, 0), hero.getPosition());
    }

    @Test
    void nullMapInConstructorThrowsException() {
        assertThrows(NullPointerException.class, () -> new MovementService(null));
    }

    @Test
    void nullMoverThrowsException() {
        MovementService service = new MovementService(bordered());
        assertThrows(NullPointerException.class, () -> service.move(null, Direction.RIGHT));
    }

    @Test
    void nullDirectionThrowsException() {
        MovementService service = new MovementService(bordered());
        Player hero = new Warrior(new Position(1, 1), 1);
        assertThrows(NullPointerException.class, () -> service.move(hero, null));
    }
}
