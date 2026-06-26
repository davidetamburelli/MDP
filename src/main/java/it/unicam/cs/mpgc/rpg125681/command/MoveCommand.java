package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Movable;
import it.unicam.cs.mpgc.rpg125681.model.movement.MoveOutcome;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;

import java.util.Objects;

public class MoveCommand implements Command {

    private final MovementService movementService;
    private final Movable mover;
    private final Direction direction;
    private MoveOutcome lastOutcome;

    public MoveCommand(MovementService movementService, Movable mover, Direction direction) {
        this.movementService = Objects.requireNonNull(movementService, "movementService");
        this.mover = Objects.requireNonNull(mover, "mover");
        this.direction = Objects.requireNonNull(direction, "direction");
    }

    @Override
    public void execute() {
        this.lastOutcome = movementService.move(mover, direction);
    }

    public MoveOutcome getLastOutcome() {
        return this.lastOutcome;
    }
}
