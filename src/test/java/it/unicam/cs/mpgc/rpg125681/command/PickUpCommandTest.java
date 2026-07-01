package it.unicam.cs.mpgc.rpg125681.command;

import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PickUpCommandTest {

    private GameWorld world(Warrior hero) {
        GameMap map = GameMap.fromRows(List.of("####", "#..#", "####"));
        return new GameWorld.Builder(map, hero, List.of()).random(new Random(0)).build();
    }

    @Test
    void pickUpMovesGroundItemsIntoInventory() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        GameWorld world = world(hero);
        Item potion = new Item(ItemType.POTION_MINOR);
        world.dropItem(new Position(1, 1), potion);

        new PickUpCommand(world).execute();

        assertTrue(hero.getInventory().getItems().contains(potion));
        assertTrue(world.getItemsAt(new Position(1, 1)).isEmpty());
    }

    @Test
    void pickUpOnEmptyCellDoesNothing() {
        Warrior hero = new Warrior(new Position(1, 1), 1);
        GameWorld world = world(hero);
        new PickUpCommand(world).execute();
        assertTrue(hero.getInventory().getItems().isEmpty());
    }
}