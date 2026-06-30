package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ItemDropTest {

    private GameMap room() {
        return GameMap.fromRows(List.of("####", "#..#", "####"));
    }

    private GameWorld worldWithGoblin(long seed) {
        GameMap map = room();
        Warrior hero = new Warrior(new Position(1, 1), 1);
        Enemy goblin = new Enemy(new Position(2, 1), 2, 1, 5, 1, new MeleeChaseStrategy(), EnemyType.GOBLIN);
        return new GameWorld(map, hero, new ArrayList<>(List.of(goblin)),
                new MovementService(map), new KillLog(), new Random(seed));
    }

    @Test
    void droppedItemCanBeRetrieved() {
        GameWorld world = worldWithGoblin(0);
        Item potion = new Item(ItemType.POTION_MINOR);
        world.dropItem(new Position(1, 1), potion);
        assertEquals(List.of(potion), world.getItemsAt(new Position(1, 1)));
    }

    @Test
    void removeItemsAtClearsCell() {
        GameWorld world = worldWithGoblin(0);
        world.dropItem(new Position(1, 1), new Item(ItemType.POTION_MINOR));
        world.removeItemsAt(new Position(1, 1));
        assertTrue(world.getItemsAt(new Position(1, 1)).isEmpty());
    }

    @Test
    void killingEnemyCanDropItem() {
        GameWorld world = worldWithGoblin(4640);
        world.playerTurn(Direction.RIGHT);
        assertFalse(world.getItemsAt(new Position(2, 1)).isEmpty());
    }

    @Test
    void killingEnemyWithoutLuckDropsNothing() {
        GameWorld world = worldWithGoblin(0);
        world.playerTurn(Direction.RIGHT);
        assertTrue(world.getItemsAt(new Position(2, 1)).isEmpty());
    }
}