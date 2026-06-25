package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;

public class Sorcerer extends Player{

    private static final int MAX_HP = 60;
    private static final int HP_PER_LEVEL = 5;

    public Sorcerer(Position position, int id) {
        super(position, id, MAX_HP);
    }

    @Override
    protected int hpPerLevel() {
        return HP_PER_LEVEL;
    }
}
