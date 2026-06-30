package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerItemsTest {

    @Test
    void equippedWeaponIncreasesAttack() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        int base = hero.getAttackPower();
        Item sword = new Item(ItemType.SWORD_IRON);
        hero.getInventory().add(sword);
        hero.getInventory().equip(sword);
        assertEquals(base + 5, hero.getAttackPower());
    }

    @Test
    void equippedArmorReducesDamageTaken() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Item armor = new Item(ItemType.ARMOR_PLATE);
        hero.getInventory().add(armor);
        hero.getInventory().equip(armor);
        hero.takeDamage(20);
        assertEquals(112, hero.getHp());
    }

    @Test
    void damageNeverGoesBelowOne() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Item armor = new Item(ItemType.ARMOR_PLATE);
        hero.getInventory().add(armor);
        hero.getInventory().equip(armor);
        hero.takeDamage(5);
        assertEquals(119, hero.getHp());
    }

    @Test
    void fixedPotionHealsAndIsConsumed() {
        Sorcerer hero = new Sorcerer(new Position(1, 1), 1);
        hero.takeDamage(40);
        Item potion = new Item(ItemType.POTION_MINOR);
        hero.getInventory().add(potion);
        hero.useItem(potion);
        assertEquals(40, hero.getHp());
        assertFalse(hero.getInventory().getItems().contains(potion));
    }

    @Test
    void percentPotionHealsByPercentageOfMaxHp() {
        Sorcerer hero = new Sorcerer(new Position(1, 1), 1);
        hero.takeDamage(40);
        Item potion = new Item(ItemType.POTION_PERCENT_HALF);
        hero.getInventory().add(potion);
        hero.useItem(potion);
        assertEquals(50, hero.getHp());
    }

    @Test
    void useItemRejectsNonPotion() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Item sword = new Item(ItemType.SWORD_IRON);
        hero.getInventory().add(sword);
        assertThrows(IllegalArgumentException.class, () -> hero.useItem(sword));
    }

    @Test
    void useItemRejectsItemNotInInventory() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Item potion = new Item(ItemType.POTION_MINOR);
        assertThrows(IllegalArgumentException.class, () -> hero.useItem(potion));
    }
}