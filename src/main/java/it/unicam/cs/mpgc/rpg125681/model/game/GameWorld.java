package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MoveOutcome;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameWorld {

    private final GameMap map;
    private final Player player;
    private final List<Enemy> enemies;
    private final MovementService movementService;

    public GameWorld(GameMap map, Player player, List<Enemy> enemies,  MovementService movementService) {
        this.map = Objects.requireNonNull(map, "map");
        this.player = Objects.requireNonNull(player, "player");
        this.movementService = Objects.requireNonNull(movementService, "movementService");
        this.enemies = new ArrayList<>(Objects.requireNonNull(enemies, "enemies"));
    }

    private void advanceEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update(player, movementService);
        }
    }

    public void playerTurn(Direction direction) {
        resolvePlayerAction(direction);
        removeDeadEnemiesAndAwardExp();
        advanceEnemies();
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

    private void removeDeadEnemiesAndAwardExp() {
        int gainedExp = enemies.stream()
                .filter(Enemy::isDead)
                .mapToInt(Enemy::getExpReward)
                .sum();
        enemies.removeIf(Enemy::isDead);
        if (gainedExp > 0) {
            player.gainExp(gainedExp);
        }
    }

    private Enemy enemyAt(Position position) {
        return enemies.stream()
                .filter(enemy -> enemy.getPosition().equals(position))
                .findFirst().orElse(null);
    }

    public GameMap getGameMap() { return this.map; }
    public Player getPlayer() { return this.player; }
    public List<Enemy> getEnemies() { return Collections.unmodifiableList(this.enemies); }
}
