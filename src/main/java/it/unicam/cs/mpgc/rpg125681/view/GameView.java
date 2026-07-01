package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.controller.SessionObserver;
import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GameStatus;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.model.world.GameMap;
import it.unicam.cs.mpgc.rpg125681.model.world.TileType;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class GameView implements SessionObserver {

    private static final int TILE = 48;

    private final GameSession session;
    private final Canvas canvas;
    private final Pane root;

    public GameView(GameSession session) {
        this.session = session;
        this.canvas = new Canvas(640, 480);
        this.root = new Pane(canvas);
        session.addObserver(this);
        if (session.getWorld() != null) {
            render();
        }
    }

    public Pane getRoot(){ return this.root; }

    public void handleKey(KeyCode code){
        switch (code){
            case UP, W -> session.handleMove(Direction.UP);
            case DOWN, S -> session.handleMove(Direction.DOWN);
            case LEFT, A -> session.handleMove(Direction.LEFT);
            case RIGHT, D -> session.handleMove(Direction.RIGHT);
            case E, SPACE -> session.handlePickUp();
            default -> {}
        }
    }

    @Override
    public void onSessionChanged(GameSession session) {
        render();
    }

    private void render(){
        GameWorld world = session.getWorld();
        if (world == null) {
            return;
        }
        GameMap map = world.getGameMap();
        resizeIfNeeded(map);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawMap(gc, map);
        drawEnemies(gc, world);
        drawPlayer(gc, world.getPlayer());
        drawUI(gc, world);
        drawEndOverlay(gc, world.status());
    }

    private void resizeIfNeeded(GameMap map){
        double width = map.getWidth() * TILE;
        double height = map.getHeight() * TILE;
        if (canvas.getWidth() != width || canvas.getHeight() != height) {
            canvas.setWidth(width);
            canvas.setHeight(height);
        }
    }

    private void drawMap(GraphicsContext gc, GameMap map){
        for (int y = 0; y < map.getHeight(); y++){
            for (int x = 0; x < map.getWidth(); x++){
                TileType tile = map.tileAt(new Position(x, y));
                gc.setFill(tile == TileType.WALL ? Color.web("#2b2b3a") : Color.web("#cfd2dc"));
                gc.fillRect(x * TILE, y * TILE, TILE, TILE);
            }
        }
    }

    private void drawEnemies(GraphicsContext gc, GameWorld world){
        gc.setFill(Color.web("#d64545"));
        for (Enemy enemy : world.getEnemies()) {
            drawEntity(gc, enemy.getPosition());
        }
    }

    private void drawPlayer(GraphicsContext gc, Player player){
        gc.setFill(Color.web("#3b7dd8"));
        drawEntity(gc, player.getPosition());
    }

    private void drawEntity(GraphicsContext gc,  Position pos){
        double margin = TILE * 0.15;
        gc.fillOval(pos.getX() * TILE + margin, pos.getY() * TILE + margin,
                TILE - 2 * margin, TILE - 2 * margin);
    }

    private void drawUI(GraphicsContext gc, GameWorld world){
        Player player = world.getPlayer();
        gc.setFill(Color.WHITE);
        gc.fillText("HP " + player.getHp() + "/" + player.getMaxHp() +
                    "   Lv " + player.getLevel() + "   Exp" + player.getExp() +
                    "   Enemies " + world.getEnemies().size(), 8, 16);
    }

    private void drawEndOverlay(GraphicsContext gc, GameStatus status){
        if (status == GameStatus.RUNNING) {
            return;
        }
        gc.setFill(Color.color(0, 0, 0, 0.55));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        String message = status == GameStatus.WON ? "You Won" : "Game Over";
        gc.fillText(message, canvas.getWidth() / 2 - 30, canvas.getHeight() / 2);
    }
}
