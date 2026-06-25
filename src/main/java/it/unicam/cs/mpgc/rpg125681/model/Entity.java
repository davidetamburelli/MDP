package it.unicam.cs.mpgc.rpg125681.model;

public abstract class Entity {
    private Position position;
    private final int id;

    protected Entity(Position position, int id) {
        this.position = position;
        this.id = id;
    }

    public int getId() { return this.id; }
    public Position getPosition() { return this.position; }

    public void moveTo(Position destination) {
        this.position = destination;
    }
}
