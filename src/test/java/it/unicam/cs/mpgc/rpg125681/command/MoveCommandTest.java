package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.movement.MoveOutcome;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MoveCommandTest {

    private MovementService service() {
        return new MovementService(GameMap.fromRows(List.of("#####", "#...#", "#.#.#", "#...#", "#####")));
    }

    @Test
    void outcomeIsNullBeforeExecute() {
        Player hero = new Warrior(new Position(1, 1), 1);
        MoveCommand command = new MoveCommand(service(), hero, Direction.RIGHT);
        assertNull(command.getLastOutcome());
    }

    @Test
    void executeMovesEntityAndCapturesOutcome() {
        Player hero = new Warrior(new Position(1, 1), 1);
        MoveCommand command = new MoveCommand(service(), hero, Direction.RIGHT);
        command.execute();
        assertEquals(MoveOutcome.MOVED, command.getLastOutcome());
        assertEquals(new Position(2, 1), hero.getPosition());
    }
}
