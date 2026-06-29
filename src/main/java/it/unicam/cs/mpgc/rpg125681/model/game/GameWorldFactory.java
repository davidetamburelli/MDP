package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.entity.Rogue;
import it.unicam.cs.mpgc.rpg125681.model.entity.Sorcerer;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.MeleeChaseStrategy;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.RangedStrategy;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.GeneratedLevel;
import it.unicam.cs.mpgc.rpg125681.model.world.MapGenerator;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWorldFactory {

    private final MapGenerator generator;
    private int nextId = 1;

    public GameWorldFactory() {
        this(new MapGenerator(new Random()));
    }

    public GameWorldFactory(MapGenerator generator) {
        this.generator = generator;
    }

    public GameWorld createFirstLevel(PlayerClass playerClass) {
        GeneratedLevel level = generateFor(1);
        Player player = createPlayer(playerClass, level.rooms().get(0));
        return assemble(1, level, player);
    }

    public GameWorld createNextLevel(int depth, Player existingPlayer) {
        GeneratedLevel level = generateFor(depth);
        existingPlayer.moveTo(level.rooms().get(0));
        return assemble(depth, level, existingPlayer);
    }

    private GeneratedLevel generateFor(int depth) {
        int rooms = depth + 3;
        int width = 13 + depth * 2;
        int height = 9 + depth * 2;
        return generator.generate(width, height, rooms);
    }

    private GameWorld assemble(int depth, GeneratedLevel level, Player player) {
        GameMap map = level.map();
        MovementService movementService = new MovementService(map);
        List<Enemy> enemies = createEnemies(depth, level.rooms());
        return new GameWorld(map, player, enemies, movementService);
    }

    private List<Enemy> createEnemies(int depth, List<Position> rooms) {
        List<Enemy> enemies = new ArrayList<>();
        int count = depth + 2;
        for (int i = 0; i < count; i++) {
            Position spawn = rooms.get(1 + i);
            int hp = 20 + depth * 5;
            int attack = 6 + depth;
            int exp = 10 + depth * 2;
            BehaviorStrategy behavior = (i % 3 == 0) ? new RangedStrategy(3) : new MeleeChaseStrategy();
            enemies.add(new Enemy(spawn, nextId(), hp, attack, exp, behavior));
        }
        return enemies;
    }

    private Player createPlayer(PlayerClass type, Position start) {
        int id = nextId();
        return switch (type) {
            case WARRIOR  -> new Warrior(start, id);
            case ROGUE    -> new Rogue(start, id);
            case SORCERER -> new Sorcerer(start, id);
        };
    }

    private int nextId() {
        return nextId++;
    }
}