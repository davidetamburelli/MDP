package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.entity.Sorcerer;
import it.unicam.cs.mpgc.rpg125681.model.entity.Rogue;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.RangedStrategy;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.List;

public class GameWorldFactory {

    private int nextId = 1;

    public GameWorld createDefaultGame(PlayerClass playerClass) {
        GameMap map = GameMap.fromRows(List.of(
                "#########",
                "#.......#",
                "#..###..#",
                "#.......#",
                "#..###..#",
                "#.......#",
                "#########"
        ));
        MovementService movementService = new MovementService(map);
        Player player = createPlayer(playerClass, new Position(1, 1));
        List<Enemy> enemies = List.of(
                meleeEnemy(new Position(7, 1), 30, 8, 12),
                meleeEnemy(new Position(7, 5), 30, 8, 12),
                rangedEnemy(new Position(1, 5), 20, 6, 15, 3)
        );
        return new GameWorld(map, player, enemies, movementService);
    }

    private Player createPlayer(PlayerClass type, Position start) {
        int id = nextId();
        return switch (type) {
            case WARRIOR -> new Warrior(start, id);
            case ROGUE -> new Rogue(start, id);
            case SORCERER -> new Sorcerer(start, id);
        };
    }

    private Enemy meleeEnemy (Position pos, int maxHp, int attack, int exp) {
        return new Enemy(pos, nextId(), maxHp, attack, exp, new MeleeChaseStrategy());
    }

    private Enemy rangedEnemy (Position pos, int maxHp, int attack, int exp, int range) {
        return new Enemy(pos, nextId(), maxHp, attack, exp, new RangedStrategy(range));
    }

    private int nextId() {
        return nextId++;
    }
}
