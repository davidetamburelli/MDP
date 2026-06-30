package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;

import java.util.List;

public class PickUpCommand implements Command {

    private final GameWorld world;
    private final Player player;

    public PickUpCommand(GameWorld world, Player player) {
        this.world = world;
        this.player = player;
    }

    @Override
    public void execute() {
        List<Item> itemsOnGround = world.getItemsAt(player.getPosition());

        if (!itemsOnGround.isEmpty()) {
            for (Item item : itemsOnGround) {
                player.getInventory().add(item);
            }
            world.removeItemsAt(player.getPosition());
        }
    }
}