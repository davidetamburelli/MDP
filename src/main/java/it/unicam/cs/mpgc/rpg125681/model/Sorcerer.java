package it.unicam.cs.mpgc.rpg125681.model;

public class Sorcerer extends Player{

    private static final int MAX_HP = 60;

    public Sorcerer(Position position, int id) {
        super(position, id, MAX_HP);
    }
}
