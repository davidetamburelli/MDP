package it.unicam.cs.mpgc.rpg125681.model.shop;

import it.unicam.cs.mpgc.rpg125681.model.entity.Rogue;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void catalogContainsClassWeaponNotOtherClassWeapon() {
        Shop shop = new Shop();
        Warrior warrior = new Warrior(new Position(1, 1), 1);
        assertTrue(shop.getCatalog(warrior, 0).contains(ItemType.SWORD_IRON));
        assertFalse(shop.getCatalog(warrior, 0).contains(ItemType.DAGGER_IRON));
    }

    @Test
    void rogueSeesDaggerNotSword() {
        Shop shop = new Shop();
        Rogue rogue = new Rogue(new Position(1, 1), 1);
        assertTrue(shop.getCatalog(rogue, 0).contains(ItemType.DAGGER_IRON));
        assertFalse(shop.getCatalog(rogue, 0).contains(ItemType.SWORD_IRON));
    }

    @Test
    void catalogGrowsWithProgress() {
        Shop shop = new Shop();
        Warrior warrior = new Warrior(new Position(1, 1), 1);
        assertTrue(shop.getCatalog(warrior, 2).size() > shop.getCatalog(warrior, 0).size());
    }

    @Test
    void higherTierItemUnlocksWithProgress() {
        Shop shop = new Shop();
        Warrior warrior = new Warrior(new Position(1, 1), 1);
        assertFalse(shop.getCatalog(warrior, 0).contains(ItemType.SWORD_SILVER)); // prezzo 120
        assertTrue(shop.getCatalog(warrior, 1).contains(ItemType.SWORD_SILVER));
    }

    @Test
    void buyDeductsGoldAndAddsItem() {
        Shop shop = new Shop();
        Warrior warrior = new Warrior(new Position(1, 1), 1);
        warrior.addGold(100);
        shop.buy(warrior, ItemType.SWORD_IRON, 0);   // prezzo 50
        assertEquals(50, warrior.getGold());
        assertEquals(1, warrior.getInventory().getItems().size());
        assertEquals(ItemType.SWORD_IRON, warrior.getInventory().getItems().get(0).getType());
    }

    @Test
    void buyingItemNotInCatalogThrows() {
        Shop shop = new Shop();
        Rogue rogue = new Rogue(new Position(1, 1), 1);
        rogue.addGold(1000);
        assertThrows(IllegalArgumentException.class, () -> shop.buy(rogue, ItemType.SWORD_IRON, 0));
    }

    @Test
    void buyingWithoutEnoughGoldThrows() {
        Shop shop = new Shop();
        Warrior warrior = new Warrior(new Position(1, 1), 1);
        warrior.addGold(10);
        assertThrows(IllegalArgumentException.class, () -> shop.buy(warrior, ItemType.SWORD_IRON, 0));
    }
}