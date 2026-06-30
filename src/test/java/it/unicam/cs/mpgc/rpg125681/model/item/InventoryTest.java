package it.unicam.cs.mpgc.rpg125681.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void addMakesItemRetrievable() {
        Inventory inv = new Inventory();
        Item potion = new Item(ItemType.POTION_MINOR);
        inv.add(potion);
        assertTrue(inv.getItems().contains(potion));
    }

    @Test
    void getItemsIsUnmodifiable() {
        Inventory inv = new Inventory();
        assertThrows(UnsupportedOperationException.class,
                () -> inv.getItems().add(new Item(ItemType.POTION_MINOR)));
    }

    @Test
    void equipWeaponAcceptsWeapon() {
        Inventory inv = new Inventory();
        Item sword = new Item(ItemType.SWORD_IRON);
        inv.add(sword);
        inv.equip(sword);
        assertEquals(sword, inv.getEquippedWeapon());
    }

    @Test
    void equipRejectsNonEquippableCategory() {
        Inventory inv = new Inventory();
        Item potion = new Item(ItemType.POTION_MINOR);
        inv.add(potion);
        assertThrows(IllegalArgumentException.class, () -> inv.equip(potion));
    }

    @Test
    void equipRequiresItemInInventory() {
        Inventory inv = new Inventory();
        Item sword = new Item(ItemType.SWORD_IRON);
        assertThrows(IllegalArgumentException.class, () -> inv.equip(sword));
    }

    @Test
    void removingEquippedItemUnequipsIt() {
        Inventory inv = new Inventory();
        Item armor = new Item(ItemType.ARMOR_PLATE);
        inv.add(armor);
        inv.equip(armor);
        inv.remove(armor);
        assertNull(inv.getEquippedArmor());
        assertFalse(inv.getItems().contains(armor));
    }
}