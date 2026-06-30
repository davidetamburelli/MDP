package it.unicam.cs.mpgc.rpg125681.model.entity;

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

    protected Player(Position position, int id, int maxHp, int attackPower) {
        super(position, id, maxHp);
        if (attackPower < 0) throw new IllegalArgumentException("Attack must be greater than 0.");
        this.level = 1;
        this.exp = 0;
        this.maxExp = INITIAL_MAX_EXP;
        this.attackPower = attackPower;
        this.absorbedAttack = 0;
        this.absorbedMaxHp = 0;
    }

    protected abstract int hpPerLevel();

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

    @Override
    public int getAttackPower() { return this.attackPower + this.absorbedAttack; }
    public int getLevel() { return this.level; }
    public int getExp() { return this.exp; }
    public int getMaxExp() { return this.maxExp; }
    public int getAbsorbedAttack() { return this.absorbedAttack; }
    public int getAbsorbedMaxHp() { return this.absorbedMaxHp; }
}