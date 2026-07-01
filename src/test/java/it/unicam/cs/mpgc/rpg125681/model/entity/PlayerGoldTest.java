package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGoldTest {

    @Test
    void newPlayerStartsWithZeroGold() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        assertEquals(0, hero.getGold());
    }

    @Test
    void addGoldIncreasesBalance() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        hero.addGold(30);
        assertEquals(30, hero.getGold());
    }

    @Test
    void spendGoldDecreasesBalance() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        hero.addGold(30);
        hero.spendGold(20);
        assertEquals(10, hero.getGold());
    }

    @Test
    void spendingMoreThanBalanceThrows() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        hero.addGold(10);
        assertThrows(IllegalArgumentException.class, () -> hero.spendGold(50));
    }

    @Test
    void getPlayerClassReturnsConcreteClass() {
        assertEquals(PlayerClass.WARRIOR, new Warrior(new Position(1, 1), 1).getPlayerClass());
        assertEquals(PlayerClass.ROGUE, new Rogue(new Position(1, 1), 2).getPlayerClass());
        assertEquals(PlayerClass.SORCERER, new Sorcerer(new Position(1, 1), 3).getPlayerClass());
    }
}