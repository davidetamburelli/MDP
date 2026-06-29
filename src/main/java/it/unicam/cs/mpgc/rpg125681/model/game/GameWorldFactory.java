package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.entity.Rogue;
import it.unicam.cs.mpgc.rpg125681.model.entity.Sorcerer;
import it.unicam.cs.mpgc.rpg125681.model.entity.Warrior;
import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorKind;
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
        List<EnemyType> pool = speciesForDepth(depth);
        List<Enemy> enemies = new ArrayList<>();
        int count = depth + 2;
        for (int i = 0; i < count; i++) {
            EnemyType type = pool.get(i % pool.size());
            Position spawn = rooms.get(1 + i);
            enemies.add(spawn(type, depth, spawn));
        }
        return enemies;
    }

    private List<EnemyType> speciesForDepth(int depth) {
        List<EnemyType> pool = new ArrayList<>();
        pool.add(EnemyType.GOBLIN);
        pool.add(EnemyType.SKELETON);
        if (depth >= 2) pool.add(EnemyType.ARCHER);
        if (depth >= 3) pool.add(EnemyType.MAGE);
        return pool;
    }

    private Enemy spawn(EnemyType type, int depth, Position position) {
        int hp = type.getBaseHp() + depth * 5;
        int attack = type.getBaseAttack() + depth;
        int exp = type.getBaseExp() + depth * 2;
        return new Enemy(position, nextId(), hp, attack, exp, behaviorFor(type), type);
    }

    private BehaviorStrategy behaviorFor(EnemyType type) {
        return type.getBehaviorKind() == BehaviorKind.RANGED
                ? new RangedStrategy(type.getRange())
                : new MeleeChaseStrategy();
    }

    private Player createPlayer(PlayerClass classType, Position start) {
        int id = nextId();
        return switch (classType) {
            case WARRIOR  -> new Warrior(start, id);
            case ROGUE    -> new Rogue(start, id);
            case SORCERER -> new Sorcerer(start, id);
        };
    }

    private int nextId() {
        return nextId++;
    }
}