package it.unicam.cs.mpgc.rpg125681.model;

public class Rogue extends Player{

    private static final int MAX_HP = 90;

    public Rogue(Position position, int id) {
        super(position, id, MAX_HP);
    }
}
