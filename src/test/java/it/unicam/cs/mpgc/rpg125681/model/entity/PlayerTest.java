package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void takeDamageReducesHp() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(50);
        assertEquals(70, p.getHp());
    }

    @Test
    void takeDamageClampsAtZeroAndMarksDead() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(1000);
        assertEquals(0, p.getHp());
        assertTrue(p.isDead());
    }

    @Test
    void negativeDamageLeavesHpUnchanged() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(-50);
        assertEquals(p.getMaxHp(), p.getHp());
    }

    @Test
    void gainExpBelowThresholdAccumulates() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.gainExp(5);
        assertEquals(5, p.getExp());
        assertEquals(120, p.getMaxHp());
    }

    @Test
    void gainExpSingleLevelUp() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.gainExp(10);
        assertEquals(2, p.getLevel());
        assertEquals(0, p.getExp());
        assertEquals(15, p.getMaxExp());
        assertEquals(135, p.getMaxHp());
    }

    @Test
    void gainExpMultipleLevelUp() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.gainExp(50);
        assertEquals(4, p.getLevel());
        assertEquals(2, p.getExp());
        assertEquals(35, p.getMaxExp());
        assertEquals(165, p.getMaxHp());
    }

    @Test
    void negativeGainExpThrowsException() {
        Player p = new Warrior(new Position(0, 0), 1);
        assertThrows(IllegalArgumentException.class, () -> p.gainExp(-10));
    }

    @Test
    void hpGrowthDiffersByClass() {
        Player warrior =  new Warrior(new Position(0, 0), 1);
        Player sorcerer = new Sorcerer(new Position(0, 0), 2);

        warrior.gainExp(10);
        sorcerer.gainExp(10);

        assertEquals(135, warrior.getMaxHp());
        assertEquals(65, sorcerer.getMaxHp());
    }

    @Test
    void healClampsToMaxHp() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(50);
        p.heal(1000);
        assertEquals(120, p.getHp());
    }

    @Test
    void fullHealRestoresToMaxHp() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(50);
        p.fullHeal();
        assertEquals(120, p.getHp());
    }

    @Test
    void healAddsPartialAmount() {
        Player p = new Warrior(new Position(0, 0), 1);
        p.takeDamage(50);
        p.heal(30);
        assertEquals(100, p.getHp());
    }

    @Test
    void negativeHealThrowsException() {
        Player p = new Warrior(new Position(0, 0), 1);
        assertThrows(IllegalArgumentException.class, () -> p.heal(-10));
    }
}
