package it.unicam.cs.mpgc.rpg125681;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.model.shop.Shop;
import it.unicam.cs.mpgc.rpg125681.persistence.JsonLeaderboardRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.LeaderboardRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.FileSessionRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.SessionRepository;
import it.unicam.cs.mpgc.rpg125681.view.GameView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.nio.file.Path;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        GameWorldFactory factory = new GameWorldFactory();
        LeaderboardRepository leaderboard = new JsonLeaderboardRepository(Path.of("leaderboard.json"));
        Shop shop = new Shop();
        SessionRepository sessions = new FileSessionRepository(Path.of("saves"));
        GameSession session = new GameSession(factory, leaderboard, shop, sessions);

        session.newGame(PlayerClass.WARRIOR);
        session.enterDungeon();

        GameView view = new GameView(session);

        Scene scene = new Scene(view.getRoot());
        scene.setOnKeyPressed(event -> view.handleKey(event.getCode()));

        stage.setTitle("Absorb'em");
        stage.setScene(scene);
        stage.show();
    }
}