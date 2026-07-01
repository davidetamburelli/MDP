package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.item.Inventory;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemCategory;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.Objects;

public abstract class Player extends LivingEntity implements Attacker {

    private static final int INITIAL_MAX_EXP = 10;
    private static final double EXP_GROWTH_FACTOR = 1.5;

    private int level;
    private int exp;
    private int maxExp;
    private final int attackPower;
    private int absorbedAttack;
    private int absorbedMaxHp;
    private int gold;
    private final Inventory inventory;
    private int maxDepthReached;

    protected Player(Position position, int id, int maxHp, int attackPower) {
        super(position, id, maxHp);
        if (attackPower < 0) throw new IllegalArgumentException("Attack must be greater than 0.");
        this.level = 1;
        this.exp = 0;
        this.maxExp = INITIAL_MAX_EXP;
        this.attackPower = attackPower;
        this.absorbedAttack = 0;
        this.absorbedMaxHp = 0;
        this.gold = 0;
        this.inventory = new Inventory();
        this.maxDepthReached = 0;
    }

    protected abstract int hpPerLevel();

    public abstract PlayerClass getPlayerClass();

    private void levelUp() {
        this.level++;
        this.maxExp = (int) Math.round(this.maxExp * EXP_GROWTH_FACTOR);
        this.increaseMaxHp(hpPerLevel());
        fullHeal();
    }

    public void gainExp(int gainedExp) {
        if (gainedExp <= 0) throw new IllegalArgumentException("Exp must be greater than 0.");
        this.exp += gainedExp;
        while (this.exp >= this.maxExp) {
            this.exp -= this.maxExp;
            levelUp();
        }
    }

    public void addGold(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Gold to add must be non-negative.");
        this.gold += amount;
    }

    public void spendGold(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Gold to spend must be non-negative.");
        if (amount > this.gold) throw new IllegalArgumentException("Not enough gold.");
        this.gold -= amount;
    }

    public void absorb(AbsorbStat stat, int amount) {
        Objects.requireNonNull(stat, "stat");
        if (amount <= 0) throw new IllegalArgumentException("Absorbed amount must be positive.");
        switch (stat) {
            case ATTACK -> this.absorbedAttack += amount;
            case MAX_HP -> {
                this.absorbedMaxHp += amount;
                increaseMaxHp(amount);
                heal(amount);
            }
        }
    }

    public void useItem(Item item) {
        Objects.requireNonNull(item, "item");
        if (!inventory.getItems().contains(item)) {
            throw new IllegalArgumentException("Item not in inventory.");
        }
        if (item.getType().getCategory() != ItemCategory.POTION) {
            throw new IllegalArgumentException("Only potions can be used.");
        }
        int healing = item.getType().isPercentage()
                ? (int) Math.round(getMaxHp() * item.getType().getEffectValue() / 100.0)
                : item.getType().getEffectValue();
        heal(healing);
        inventory.remove(item);
    }

    @Override
    public void takeDamage(int damage) {
        if (damage <= 0) {
            super.takeDamage(damage);
            return;
        }
        super.takeDamage(Math.max(1, damage - getDefense()));
    }

    public void recordDepthReached(int depth) {
        if (depth > this.maxDepthReached) {
            this.maxDepthReached = depth;
        }
    }

    public int getDefense() {
        Item armor = inventory.getEquippedArmor();
        return armor != null ? armor.getType().getEffectValue() : 0;
    }

    @Override
    public int getAttackPower() {
        Item weapon = inventory.getEquippedWeapon();
        int weaponBonus = weapon != null ? weapon.getType().getEffectValue() : 0;
        return this.attackPower + this.absorbedAttack + weaponBonus;
    }

    public int getLevel() { return this.level; }
    public int getExp() { return this.exp; }
    public int getMaxExp() { return this.maxExp; }
    public int getAbsorbedAttack() { return this.absorbedAttack; }
    public int getAbsorbedMaxHp() { return this.absorbedMaxHp; }
    public int getGold() { return this.gold; }
    public Inventory getInventory() { return this.inventory; }
    public int getMaxDepthReached() { return this.maxDepthReached; }
}