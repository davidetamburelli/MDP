package it.unicam.cs.mpgc.rpg125681.model;

public abstract class LivingEntity extends Entity{

    private int hp;
    private int maxHp;

    protected LivingEntity(Position position, int id, int maxHp) {
        super(position, id); //Entity parameters
        this.maxHp = maxHp;
        this.hp = maxHp; //created with full health
    }

    public void takeDamage(int damage) {
        if (damage < 0) { damage = 0; }
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    public void heal(int heal) {
        if (heal <= 0) throw new IllegalArgumentException("Healing must be positive");
        this.hp += heal;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }

    public void fullHeal() {
        this.hp = this.maxHp;
    }

    public void increaseMaxHp(int increase) {
        if (increase <= 0) throw new IllegalArgumentException("Increase must be positive");
        this.maxHp += increase;
    }

    public int getHp() { return this.hp; }
    public int getMaxHp() { return this.maxHp; }
}
