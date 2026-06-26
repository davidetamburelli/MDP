package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.RangedStrategy;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyBehaviorTest {

    private MovementService openRoom() {
        return new MovementService(GameMap.fromRows(List.of(
                "#########",
                "#.......#",
                "#########"
        )));
    }

    @Test
    void meleeAttacksWhenAdjacent() {
        MovementService movement = openRoom();
        Player hero = new Warrior(new Position(0, 0), 1);
        Enemy enemy = new Enemy(new Position(1, 0), 2, 30, 8, 5, new MeleeChaseStrategy());
        enemy.update(hero, movement);
        assertEquals(112, hero.getHp());
        assertEquals(new Position(1, 0), enemy.getPosition());
    }

    @Test
    void meleeMovesCloserWhenFar() {
        MovementService movement = openRoom();
        Player hero = new Warrior(new Position(0, 0), 3);
        Enemy enemy = new Enemy(new Position(4, 0), 4, 30, 8, 5, new MeleeChaseStrategy());
        enemy.update(hero, movement);
        assertEquals(120, hero.getHp());
        assertEquals(new Position(4, 0), enemy.getPosition());
    }

    @Test
    void rangedAttacksWithinRange() {
        MovementService movement = openRoom();
        Player hero = new Warrior(new Position(0, 0), 5);
        Enemy enemy = new Enemy(new Position(3, 0), 6, 30, 6, 5, new RangedStrategy(3));
        enemy.update(hero, movement);
        assertEquals(114, hero.getHp());
        assertEquals(new Position(3, 0), enemy.getPosition());
    }

    @Test
    void rangedMovesCloserWhenOutOfRange() {
        MovementService movement = openRoom();
        Player hero = new Warrior(new Position(1, 1), 7);
        Enemy enemy = new Enemy(new Position(6, 1), 8, 30, 6, 5, new RangedStrategy(3));
        enemy.update(hero, movement);
        assertEquals(120, hero.getHp());
        assertEquals(new Position(5, 1), enemy.getPosition());
    }

    @Test
    void enemyBlockedByWallStaysPut() {
        MovementService movement = new MovementService(GameMap.fromRows(List.of(
                "#####",
                "#.#.#",
                "#####"
        )));
        Player hero = new Warrior(new Position(1, 1), 9);
        Enemy enemy = new Enemy(new Position(3, 1), 10, 30, 8, 5, new MeleeChaseStrategy());
        enemy.update(hero, movement);
        assertEquals(120, hero.getHp());
        assertEquals(new Position(3, 1), enemy.getPosition());
    }

    @Test
    void stepTowardMovesVerticallyWhenTargetAbove() {
        Direction dir = BehaviorStrategy.stepToward(new Position(0 ,5), new Position(0, 0));
        assertEquals(Direction.UP, dir);
    }

    @Test
    void stepTowardsBreaksTieVertically() {
        Direction dir = BehaviorStrategy.stepToward(new Position(3, 3), new Position(0, 0));
        assertEquals(Direction.UP, dir);
    }

    @Test
    void negativeAttackPowerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Enemy(
                new Position(0, 0), 1, 30, -1, 5, new MeleeChaseStrategy()
        ));
    }

    @Test
    void nullBehaviorThrowsException() {
        assertThrows(NullPointerException.class, () -> new Enemy(
                new Position(0, 0), 1, 30, 8, 5, null));
    }
}
