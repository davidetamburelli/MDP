package it.unicam.cs.mpgc.rpg125681.controller;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameStatus;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorld;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;
import it.unicam.cs.mpgc.rpg125681.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.SaveGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {

    private final GameWorldFactory factory;
    private final GameRepository repository;
    private final List<GameObserver> observers = new ArrayList<>();
    private GameWorld world;

    public GameController(GameWorldFactory factory, GameRepository repository) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public void addObserver(GameObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    public void newGame(PlayerClass playerClass) {
        this.world = factory.createDefaultGame(playerClass);
        notifyObservers();
    }

    public void handleMove(Direction direction) {
        if (world == null || world.isOver()) {
            return;
        }
        world.playerTurn(direction);
        notifyObservers();
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

    public GameWorld getWorld() {
        return this.world;
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
}
