package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameStatus;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.SaveGame;
import it.unicam.cs.mpgc.rpg125681.persistence.LeaderboardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {

    private static final int FINAL_DEPTH = 5;

    private final GameWorldFactory factory;
    private final GameRepository repository;
    private final List<GameObserver> observers = new ArrayList<>();
    private final LeaderboardRepository leaderboard;
    private GameWorld world;
    private int depth;

    public GameController(GameWorldFactory factory, GameRepository repository, LeaderboardRepository leaderboard) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.leaderboard = Objects.requireNonNull(leaderboard, "leaderboard");
    }

    public void addObserver(GameObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    public void newGame(PlayerClass playerClass) {
        this.depth = 1;
        this.world = factory.createFirstLevel(playerClass);
        notifyObservers();
    }

    public void handleMove(Direction direction) {
        if (world == null || world.isOver()) {
            return;
        }
        world.playerTurn(direction);
        GameStatus status = world.status();
        if (status == GameStatus.WON && depth < FINAL_DEPTH) {
            descend();
        } else if (status == GameStatus.WON) {
            recordRun(RunOutcome.VICTORY);
        } else if (status == GameStatus.LOST) {
            recordRun(RunOutcome.DEFEAT);
        }
        notifyObservers();
    }

    private void descend() {
        this.depth++;
        this.world = factory.createNextLevel(depth, world.getPlayer());
    }

    public void save(String slot) {
        requireActiveGame();
        repository.save(slot, SaveGame.from(world));
    }

    public void load(String slot) {
        this.world = repository.load(slot).toGameWorld();
        notifyObservers();
    }

    public List<String> savedSlots() {
        return repository.listSlots();
    }

    public GameStatus status() {
        requireActiveGame();
        return world.status();
    }

    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onGameChanged(this);
        }
    }

    private void requireActiveGame() {
        if (world == null) {
            throw new IllegalStateException("No active game");
        }
    }

    private void recordRun(RunOutcome outcome) {
        leaderboard.add(RunRecord.of(outcome, depth, world, System.currentTimeMillis()));
    }

    public GameWorld getWorld() { return this.world; }
    public int getDepth() { return this.depth; }
    public List<RunRecord> getRunHistory() { return leaderboard.findAll(); }
    public List<RunRecord> getRunHistory(RunOutcome outcome) { return leaderboard.findByOutcome(outcome); }
}