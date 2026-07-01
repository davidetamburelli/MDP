package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.*;

public class GameWorld {

    private final GameMap map;
    private final Player player;
    private final List<Enemy> enemies;
    private final MovementService movementService;
    private final KillLog killLog;
    private final Random random;
    private final Map<Position, List<Item>> droppedItems = new HashMap<>();

    public GameWorld(GameMap map, Player player, List<Enemy> enemies, MovementService movementService) {
        this(map, player, enemies, movementService, new KillLog());
    }

    public GameWorld(GameMap map, Player player, List<Enemy> enemies, MovementService movementService, KillLog killLog) {
        this(map, player, enemies, movementService, killLog, new Random());
    }

    public GameWorld(GameMap map, Player player, List<Enemy> enemies, MovementService movementService,
                     KillLog killLog, Random random) {
        this(map, player, enemies, movementService, killLog, random, new HashMap<>());
    }

    public GameWorld(GameMap map, Player player, List<Enemy> enemies, MovementService movementService,
                     KillLog killLog, Random random, Map<Position, List<Item>> droppedItems) {
        this.map = Objects.requireNonNull(map, "map");
        this.player = Objects.requireNonNull(player, "player");
        this.enemies = new ArrayList<>(Objects.requireNonNull(enemies, "enemies"));
        this.movementService = Objects.requireNonNull(movementService, "movementService");
        this.killLog = Objects.requireNonNull(killLog, "killLog");
        this.random = Objects.requireNonNull(random, "random");
        Objects.requireNonNull(droppedItems, "droppedItems")
                .forEach((position, items) -> this.droppedItems.put(position, new ArrayList<>(items)));
    }

    public void playerTurn(Direction direction) {
        if (player.isDead()) {
            return;
        }
        resolvePlayerAction(direction);
        removeDeadEnemiesAndAwardExp();
        advanceEnemies();
    }

    public GameStatus status() {
        if (player.isDead()) {
            return GameStatus.LOST;
        }
        if (enemies.isEmpty()) {
            return GameStatus.WON;
        }
        return GameStatus.RUNNING;
    }

    public boolean isOver() {
        return status() != GameStatus.RUNNING;
    }

    private void resolvePlayerAction(Direction direction) {
        Position target = new Position(
                player.getPosition().getX() + direction.getDx(),
                player.getPosition().getY() + direction.getDy()
        );
        Enemy occupant = enemyAt(target);
        if (occupant != null) {
            player.attack(occupant);
        } else {
            movementService.move(player, direction);
        }
    }

    private void advanceEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update(player, movementService);
        }
    }

    private void removeDeadEnemiesAndAwardExp() {
        List<Enemy> dead = enemies.stream().filter(Enemy::isDead).toList();
        dead.forEach(this::registerKill);
        int gainedExp = dead.stream().mapToInt(Enemy::getExpReward).sum();
        enemies.removeAll(dead);
        if (gainedExp > 0) {
            player.gainExp(gainedExp);
        }
    }

    private void registerKill(Enemy enemy) {
        EnemyType type = enemy.getType();
        killLog.record(type);
        player.absorb(type.getAbsorbStat(), type.getAbsorbAmount());
        player.addGold(type.getBaseGold());
        rollDrop(enemy);
    }

    private void rollDrop(Enemy enemy) {
        if (random.nextDouble() < enemy.getType().getDropChance()) {
            ItemType dropType = ItemType.getRandomDrop(random);
            if (dropType != null) {
                dropItem(enemy.getPosition(), new Item(dropType));
            }
        }
    }

    private Enemy enemyAt(Position position) {
        return enemies.stream()
                .filter(enemy -> enemy.getPosition().equals(position))
                .findFirst().orElse(null);
    }

    public void dropItem(Position position, Item item) {
        droppedItems.computeIfAbsent(position, k -> new ArrayList<>()).add(item);
    }

    public void removeItemsAt(Position position) {
        droppedItems.remove(position);
    }

    public GameMap getGameMap() { return this.map; }
    public Player getPlayer() { return this.player; }
    public List<Enemy> getEnemies() { return Collections.unmodifiableList(this.enemies); }
    public KillLog getKillLog() { return this.killLog; }

    public List<Item> getItemsAt(Position position) {
        return droppedItems.getOrDefault(position, Collections.emptyList());
    }

    public Map<Position, List<Item>> getDroppedItems() {
        return Collections.unmodifiableMap(this.droppedItems);
    }
}