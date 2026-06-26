package it.unicam.cs.mpgc.rpg125681.model.movement;

import it.unicam.cs.mpgc.rpg125681.model.entity.Movable;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.Objects;

public class MovementService {

    private final GameMap map;

    public MovementService(GameMap map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    public MoveOutcome move(Movable mover, Direction direction) {
        Objects.requireNonNull(mover, "mover");
        Objects.requireNonNull(direction, "direction");
        Position current = mover.getPosition();
        Position target = new Position(
                current.getX() + direction.getDx(),
                current.getY() + direction.getDy()
                );
        if (!map.contains(target)) {
            return MoveOutcome.OUT_OF_BOUNDS;
        }
        if (!map.isWalkable(target)) {
            return MoveOutcome.BLOCKED_BY_WALL;
        }
        mover.moveTo(target);
        return MoveOutcome.MOVED;
    }
}
