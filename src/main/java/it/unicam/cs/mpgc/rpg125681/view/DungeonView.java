package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.model.world.TileType;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DungeonView {

    private static final int TILE = 32;

    private final GameSession session;
    private final Canvas canvas = new Canvas(640, 480);
    private final Pane root = new Pane(canvas);

    public DungeonView(GameSession session) {
        this.session = session;
    }

    public Node getNode() {
        return this.root;
    }

    public void handleKey(KeyCode code) {
        switch (code) {
            case UP, W -> session.handleMove(Direction.UP);
            case DOWN, S -> session.handleMove(Direction.DOWN);
            case LEFT, A -> session.handleMove(Direction.LEFT);
            case RIGHT, D -> session.handleMove(Direction.RIGHT);
            case E, SPACE -> session.handlePickUp();
            case Q -> session.usePotion();
            default -> { }
        }
    }

    public void render() {
        GameWorld world = session.getWorld();
        if (world == null) {
            return;
        }
        GameMap map = world.getGameMap();
        resizeIfNeeded(map);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#14141c"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawMap(gc, map);
        drawItems(gc, world);
        drawEnemies(gc, world);
        drawPlayer(gc, world.getPlayer());
        drawHud(gc, world);
    }

    private void resizeIfNeeded(GameMap map) {
        double width = map.getWidth() * TILE;
        double height = map.getHeight() * TILE;
        if (canvas.getWidth() != width || canvas.getHeight() != height) {
            canvas.setWidth(width);
            canvas.setHeight(height);
        }
    }

    private void drawMap(GraphicsContext gc, GameMap map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                TileType tile = map.tileAt(new Position(x, y));
                gc.setFill(tile == TileType.WALL ? Color.web("#2b2b3a") : Color.web("#cfd2dc"));
                gc.fillRect(x * TILE, y * TILE, TILE, TILE);
            }
        }
    }

    private void drawItems(GraphicsContext gc, GameWorld world) {
        gc.setFill(Color.web("#e0c040"));
        world.getDroppedItems().keySet().forEach(position -> {
            double margin = TILE * 0.32;
            gc.fillRect(position.getX() * TILE + margin, position.getY() * TILE + margin,
                    TILE - 2 * margin, TILE - 2 * margin);
        });
    }

    private void drawEnemies(GraphicsContext gc, GameWorld world) {
        gc.setFill(Color.web("#d64545"));
        for (Enemy enemy : world.getEnemies()) {
            drawEntity(gc, enemy.getPosition());
        }
    }

    private void drawPlayer(GraphicsContext gc, Player player) {
        gc.setFill(Color.web("#3b7dd8"));
        drawEntity(gc, player.getPosition());
    }

    private void drawEntity(GraphicsContext gc, Position position) {
        double margin = TILE * 0.15;
        gc.fillOval(position.getX() * TILE + margin, position.getY() * TILE + margin,
                TILE - 2 * margin, TILE - 2 * margin);
    }

    private void drawHud(GraphicsContext gc, GameWorld world) {
        Player player = world.getPlayer();
        gc.setFill(Color.color(0, 0, 0, 0.55));
        gc.fillRect(0, 0, canvas.getWidth(), 44);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(14));
        gc.fillText("HP " + player.getHp() + "/" + player.getMaxHp()
                + "    Lv " + player.getLevel() + " (" + player.getExp() + "/" + player.getMaxExp() + ")"
                + "    Gold " + player.getGold()
                + "    Depth " + session.getDepth()
                + "    Enemies " + world.getEnemies().size(), 10, 20);
        gc.setFill(Color.web("#aab0c0"));
        gc.setFont(Font.font(11));
        gc.fillText("WASD / arrows: move    E / Space: pick up    Q: use potion", 10, 38);    }
}