package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record SaveGame(GameMap map, Player player, List<Enemy> enemies) implements Serializable {

    private final static long serialVersionUID = 1L;

    public static SaveGame from(GameWorld world) {
        return new SaveGame(world.getGameMap(), world.getPlayer(), new ArrayList<>(world.getEnemies()));
    }

    public GameWorld toGameWorld() {
        return new GameWorld(map, player, enemies, new MovementService(map));
    }
}
