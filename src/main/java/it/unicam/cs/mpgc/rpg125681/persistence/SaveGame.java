package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.KillLog;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public record SaveGame(GameMap map, Player player, List<Enemy> enemies, KillLog killLog,
                       Map<Position, List<Item>> droppedItems) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static SaveGame from(GameWorld world) {
        return new SaveGame(
                world.getGameMap(),
                world.getPlayer(),
                new ArrayList<>(world.getEnemies()),
                world.getKillLog(),
                new HashMap<>(world.getDroppedItems()));
    }

    public GameWorld toGameWorld() {
        return new GameWorld(map, player, enemies, new MovementService(map), killLog,
                new Random(), droppedItems);
    }
}