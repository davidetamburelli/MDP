package it.unicam.cs.mpgc.rpg125681.model;

public abstract class Player extends LivingEntity {

    private static final int INITIAL_MAX_EXP = 10;
    private static final double EXP_GROWTH_FACTOR = 1.5;
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
            this.exp -= this.maxExp; //carry over exceeding exp
            levelUp();
        }
    }
}
