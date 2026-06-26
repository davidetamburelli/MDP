package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;

public interface Movable {
    void moveTo(Position destination);
    Position getPosition();
}
