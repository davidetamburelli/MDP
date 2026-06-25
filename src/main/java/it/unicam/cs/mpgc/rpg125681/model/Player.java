package it.unicam.cs.mpgc.rpg125681.model;

public abstract class Player extends LivingEntity {

    private static final int INITIAL_MAX_EXP = 10;
    private int level;
    private int exp;
    private int maxExp;

    protected Player(Position position, int id, int maxHp) {
        super(position, id, maxHp);
        this.level = 1;
        this.exp = 0;
        this.maxExp = INITIAL_MAX_EXP;
    }

    public int getLevel() { return this.level; }
    public int getExp() { return this.exp; }
    public int getMaxExp() { return this.maxExp; }
}
