package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;
import it.unicam.cs.mpgc.rpg125681.model.game.GameStatus;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;
import it.unicam.cs.mpgc.rpg125681.model.shop.Shop;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.persistence.LeaderboardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSession {

    private static final int FINAL_DEPTH = 5;

    private final GameWorldFactory factory;
    private final LeaderboardRepository leaderboard;
    private final Shop shop;
    private final List<SessionObserver> observers = new ArrayList<>();

    private GamePhase phase = GamePhase.MENU;
    private Player player;
    private GameWorld world;
    private int depth;
    private RunRecord lastRun;

    public GameSession(GameWorldFactory factory, LeaderboardRepository leaderboard, Shop shop) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.leaderboard = Objects.requireNonNull(leaderboard, "leaderboard");
        this.shop = Objects.requireNonNull(shop, "shop");
    }

    public void addObserver(SessionObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    public void newGame(PlayerClass playerClass) {
        this.player = factory.createPlayer(playerClass);
        this.world = null;
        this.depth = 0;
        this.lastRun = null;
        this.phase = GamePhase.HUB;
        notifyObservers();
    }

    public void enterDungeon() {
        requirePhase(GamePhase.HUB);
        this.depth = 1;
        this.world = factory.createNextLevel(depth, player);
        player.recordDepthReached(depth);
        this.phase = GamePhase.DUNGEON;
        notifyObservers();
    }

    public void handleMove(Direction direction) {
        if (phase != GamePhase.DUNGEON || world.isOver()) {
            return;
        }
        world.playerTurn(direction);
        GameStatus status = world.status();
        if (status == GameStatus.WON && depth < FINAL_DEPTH) {
            descend();
        } else if (status == GameStatus.WON) {
            endRun(RunOutcome.VICTORY);
        } else if (status == GameStatus.LOST) {
            endRun(RunOutcome.DEFEAT);
        }
        notifyObservers();
    }

    private void descend() {
        depth++;
        world = factory.createNextLevel(depth, player);
        player.recordDepthReached(depth);
    }

    private void endRun(RunOutcome outcome) {
        lastRun = RunRecord.of(outcome, depth, world, System.currentTimeMillis());
        leaderboard.add(lastRun);
        phase = GamePhase.RUN_RECAP;
    }

    public void openLeaderboard() {
        if (phase != GamePhase.HUB && phase != GamePhase.RUN_RECAP) {
            throw new IllegalStateException("Leaderboard reachable only from hub or recap.");
        }
        phase = GamePhase.LEADERBOARD;
        notifyObservers();
    }

    public void returnToHub() {
        this.world = null;
        this.depth = 0;
        this.phase = GamePhase.HUB;
        notifyObservers();
    }

    public List<ItemType> getShopCatalog() {
        return shop.getCatalog(player, player.getMaxDepthReached());
    }

    public void buy(ItemType type) {
        shop.buy(player, type, player.getMaxDepthReached());
        notifyObservers();
    }

    public List<RunRecord> getRunHistory() { return leaderboard.findAll(); }
    public List<RunRecord> getRunHistory(RunOutcome outcome) { return leaderboard.findByOutcome(outcome); }

    public GamePhase getPhase() { return phase; }
    public Player getPlayer() { return player; }
    public GameWorld getWorld() { return world; }
    public int getDepth() { return depth; }
    public RunRecord getLastRun() { return lastRun; }

    private void requirePhase(GamePhase expected) {
        if (phase != expected) {
            throw new IllegalStateException("Expected phase " + expected + " but was " + phase);
        }
    }

    private void notifyObservers() {
        for (SessionObserver observer : observers) {
            observer.onSessionChanged(this);
        }
    }
}