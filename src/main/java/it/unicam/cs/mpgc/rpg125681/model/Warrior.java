package it.unicam.cs.mpgc.rpg125681.model;

public class Warrior extends Player{

    private static final int MAX_HP = 120;

    public Warrior(Position position, int id) {
        super(position, id, MAX_HP);
    }
}
