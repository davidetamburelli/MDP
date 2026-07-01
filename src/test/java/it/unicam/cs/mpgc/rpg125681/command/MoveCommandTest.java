package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MoveCommandTest {

    private GameWorld world(Warrior hero) {
        GameMap map = GameMap.fromRows(List.of("#####", "#...#", "#...#", "#...#", "#####"));
        return new GameWorld(map, hero, List.of());
    }

    @Test
    void executeMovesPlayerAsATurn() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        new MoveCommand(world(hero), Direction.RIGHT).execute();
        assertEquals(new Position(2, 1), hero.getPosition());
    }

    @Test
    void executeIntoWallLeavesPlayerInPlace() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        new MoveCommand(world(hero), Direction.UP).execute();
        assertEquals(new Position(1, 1), hero.getPosition());
    }

}
