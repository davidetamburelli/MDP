package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.controller.SessionObserver;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class GameView implements SessionObserver {

    private final GameSession session;
    private final StackPane root = new StackPane();
    private final DungeonView dungeonView;

    public GameView(GameSession session) {
        this.session = session;
        this.dungeonView = new DungeonView(session);
        session.addObserver(this);
        render();
    }

    public Parent getRoot() {
        return this.root;
    }

    public void handleKey(KeyCode code) {
        if (session.getPhase() == GamePhase.DUNGEON) {
            dungeonView.handleKey(code);
        }
    }

    @Override
    public void onSessionChanged(GameSession session) {
        render();
    }

    private void render() {
        Parent content = switch (session.getPhase()) {
            case MENU -> new MenuView(session).getNode();
            case HUB -> new HubView(session).getNode();
            case DUNGEON -> (Parent) dungeonView.getNode();
            case RUN_RECAP -> new RecapView(session).getNode();
            case LEADERBOARD -> new LeaderboardView(session).getNode();
            case BESTIARY -> new BestiaryView(session).getNode();
        };
        root.getChildren().setAll(content);
        if (session.getPhase() == GamePhase.DUNGEON) {
            dungeonView.render();
        }
    }
}