package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.RangedStrategy;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyBehaviorTest {

    @Test
    void meleeAttacksWhenAdjacent() {
        Player hero = new Warrior(new Position(0, 0), 1);
        Enemy enemy = new Enemy(new Position(1, 0), 2, 30, 8, 5, new MeleeChaseStrategy());
        enemy.update(hero);
        assertEquals(112, hero.getHp());
        assertEquals(new Position(1, 0), enemy.getPosition());
    }

    @Test
    void meleeMovesCloserWhenFar() {
        Player hero = new Warrior(new Position(0, 0), 3);
        Enemy enemy = new Enemy(new Position(5, 0), 4, 30, 8, 5, new MeleeChaseStrategy());
        enemy.update(hero);
        assertEquals(120, hero.getHp());
        assertEquals(new Position(4, 0), enemy.getPosition());
    }

    @Test
    void rangedAttacksWithinRange() {
        Player hero = new Warrior(new Position(0, 0), 5);
        Enemy enemy = new Enemy(new Position(3, 0), 6, 30, 6, 5, new RangedStrategy(3));
        enemy.update(hero);
        assertEquals(114, hero.getHp());
        assertEquals(new Position(3, 0), enemy.getPosition());
    }

    @Test
    void rangedMovesCloserWhenOutOfRange() {
        Player hero = new Warrior(new Position(0, 0), 7);
        Enemy enemy = new Enemy(new Position(5, 0), 8, 30, 6, 5, new RangedStrategy(3));
        enemy.update(hero);
        assertEquals(120, hero.getHp());
        assertEquals(new Position(4, 0), enemy.getPosition());
    }

    @Test
    void stepTowardMovesVerticallyWhenTargetAbove() {
        Position next = BehaviorStrategy.stepToward(new Position(0 ,5), new Position(0, 0));
        assertEquals(new Position(0, 4), next);
    }

    @Test
    void stepTowardsBreaksTieVertically() {
        Position next = BehaviorStrategy.stepToward(new Position(3, 3), new Position(0, 0));
        assertEquals(new Position(3, 2), next);
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
