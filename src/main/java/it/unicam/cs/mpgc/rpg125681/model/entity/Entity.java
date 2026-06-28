package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.io.Serializable;

public abstract class Entity implements Serializable{
    private Position position;
    private final int id;

    protected Entity(Position position, int id) {
        this.position = position;
        this.id = id;
    }

    public int getId() { return this.id; }
    public Position getPosition() { return this.position; }

    protected void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity other)) return false;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}
