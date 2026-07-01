package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;

public class Warrior extends Player{

    private static final int MAX_HP = 120;
    private static final int HP_PER_LEVEL = 15;
    private static final int ATTACK_POWER = 9;

    public Warrior(Position position, int id) {
        super(position, id, MAX_HP, ATTACK_POWER);
    }

    @Override
    public PlayerClass getPlayerClass() { return PlayerClass.WARRIOR; }

    @Override
    protected int hpPerLevel() {
        return HP_PER_LEVEL;
    }
}
