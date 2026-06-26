package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameWorldTest {

    private GameMap room() {
        return GameMap.fromRows(List.of(
                "#####",
                "#...#",
                "#...#",
                "#####"
        ));
    }

    @Test
    void freeMoveUpdatesPlayerPosition() {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        GameWorld world = new GameWorld(map, hero, List.of(), new MovementService(map));
        world.playerTurn(Direction.RIGHT);
        assertEquals(new Position(2, 1), hero.getPosition());
    }

    @Test
    void bumpAttackDamagesEnemyAndPlayerStaysPut() {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Enemy enemy = new Enemy(new Position(2, 1), 2, 30, 8, 12, new MeleeChaseStrategy());
        GameWorld world = new GameWorld(map, hero, List.of(enemy), new MovementService(map));
        world.playerTurn(Direction.RIGHT);
        assertEquals(21, world.getEnemies().get(0).getHp());
        assertEquals(new Position(1, 1), hero.getPosition());
    }

    @Test
    void killingEnemyRemovesItAndAwardsExp () {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Enemy enemy = new Enemy(new Position(2, 1), 2, 8, 8, 12, new MeleeChaseStrategy());
        GameWorld world = new GameWorld(map, hero, List.of(enemy), new MovementService(map));
        world.playerTurn(Direction.RIGHT);
        assertTrue(world.getEnemies().isEmpty());
        assertEquals(2, hero.getLevel());
        assertEquals(2, hero.getExp());
    }

    @Test
    void distantEnemyAdvancesDuringTurn() {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Enemy enemy = new Enemy(new Position(3, 1), 2, 30, 8, 12, new MeleeChaseStrategy());
        GameWorld world = new GameWorld(map, hero, List.of(enemy), new MovementService(map));
        world.playerTurn(Direction.DOWN);
        assertEquals(new Position(1, 2), hero.getPosition());
        assertEquals(new Position(2, 1), world.getEnemies().get(0).getPosition());
    }

    @Test
    void getEnemiesIsUnmodifiable() {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Enemy enemy = new Enemy(new  Position(2, 1), 2, 30, 8, 12, new MeleeChaseStrategy());
        GameWorld world = new GameWorld(map, hero, List.of(enemy), new MovementService(map));
        assertThrows(UnsupportedOperationException.class, () -> world.getEnemies().clear());
    }

    @Test
    void nullMapInConstructorThrowsException() {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        assertThrows(NullPointerException.class, () -> new GameWorld(null, hero, List.of(), new MovementService(map)));
    }
}
