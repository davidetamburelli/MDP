package it.unicam.cs.mpgc.rpg125681.model.item;

import it.unicam.cs.mpgc.rpg125681.model.entity.AbsorbStat;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerEquipmentTest {

    private static class DummyPlayer extends Player {
        public DummyPlayer(Position position, int id, int maxHp, int baseAttackPower) {
            super(position, id, maxHp, baseAttackPower);
        }
        @Override
        protected int hpPerLevel() { return 10; }
    }

    private static class DummyItem extends Item {
        public DummyItem(ItemType type) { super(type); }
    }

    @Test
    void attackPowerWithoutWeaponEqualsBasePlusAbsorbed() {
        Player player = new DummyPlayer(new Position(0, 0), 1, 100, 10);

        assertEquals(10, player.getAttackPower());

        player.absorb(AbsorbStat.ATTACK, 5);

        assertEquals(15, player.getAttackPower());
    }

    @Test
    void equippedWeaponIncreasesAttackPower() {
        Player player = new DummyPlayer(new Position(0, 0), 1, 100, 10);
        Item sword = new DummyItem(ItemType.SWORD_IRON);

        player.getInventory().add(sword);
        player.getInventory().equip(sword);

        assertEquals(10 + ItemType.SWORD_IRON.getEffectValue(), player.getAttackPower());
    }

    @Test
    void unequippedWeaponRemovesBonusFromAttackPower() {
        Player player = new DummyPlayer(new Position(0, 0), 1, 100, 10);
        Item sword = new DummyItem(ItemType.SWORD_IRON);

        player.getInventory().add(sword);
        player.getInventory().equip(sword);
        player.getInventory().remove(sword);

        assertEquals(10, player.getAttackPower());
    }

    @Test
    void absorbedStatsAndWeaponBonusStackCorrectly() {
        Player player = new DummyPlayer(new Position(0, 0), 1, 100, 10);
        Item sword = new DummyItem(ItemType.SWORD_IRON);

        player.absorb(AbsorbStat.ATTACK, 5);
        player.getInventory().add(sword);
        player.getInventory().equip(sword);

        int expected = 10 + 5 + ItemType.SWORD_IRON.getEffectValue();
        assertEquals(expected, player.getAttackPower());
    }
}