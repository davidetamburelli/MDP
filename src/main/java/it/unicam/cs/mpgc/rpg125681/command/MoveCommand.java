package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;

import java.util.Objects;

public class MoveCommand implements Command {

    private final GameWorld world;
    private final Direction direction;

    public MoveCommand(GameWorld world, Direction direction) {
        this.world = Objects.requireNonNull(world, "world");
        this.direction = Objects.requireNonNull(direction, "direction");
    }

    @Override
    public void execute() {
        world.playerTurn(direction);
    }
}