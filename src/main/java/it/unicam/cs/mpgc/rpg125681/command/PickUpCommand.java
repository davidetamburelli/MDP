package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.List;
import java.util.Objects;

public class PickUpCommand implements Command {

    private final GameWorld world;

    public PickUpCommand(GameWorld world) {
        this.world = Objects.requireNonNull(world, "world");
    }

    @Override
    public void execute() {
        Player player = world.getPlayer();
        Position position = player.getPosition();
        List<Item> itemsOnGround = world.getItemsAt(position);
        if (itemsOnGround.isEmpty()) {
            return;
        }
        for (Item item : itemsOnGround) {
            player.getInventory().add(item);
        }
        world.removeItemsAt(position);
    }
}