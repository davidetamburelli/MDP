package it.unicam.cs.mpgc.rpg125681.model.movement;

public enum MoveOutcome {
    MOVED,
    BLOCKED_BY_WALL,
    OUT_OF_BOUNDS;

    public boolean isSuccess() { return this == MOVED; }
}
