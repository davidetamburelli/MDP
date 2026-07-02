package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class RecapView {

    private final GameSession session;

    public RecapView(GameSession session) {
        this.session = session;
    }

    public Parent getNode() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        RunRecord run = session.getLastRun();
        boolean victory = run.outcome() == RunOutcome.VICTORY;

        Label title = new Label(victory ? "Victory!" : "Defeat");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: "
                + (victory ? "#2e7d32" : "#c62828") + ";");

        box.getChildren().addAll(
                title,
                new Label("Depth reached: " + run.maxDepth()),
                new Label("Total kills: " + run.totalKills()),
                new Label("Level: " + run.level() + "   Exp: " + run.exp()));

        if (!run.killsBySpecies().isEmpty()) {
            box.getChildren().add(new Label("Kills by species:"));
            for (Map.Entry<EnemyType, Integer> entry : run.killsBySpecies().entrySet()) {
                box.getChildren().add(new Label("  " + entry.getKey().getDisplayName() + ": " + entry.getValue()));
            }
        }

        Button hub = new Button("Return to hub");
        hub.setOnAction(event -> session.returnToHub());
        Button leaderboard = new Button("Leaderboard");
        leaderboard.setOnAction(event -> session.openLeaderboard());
        HBox actions = new HBox(12, hub, leaderboard);
        actions.setAlignment(Pos.CENTER);
        box.getChildren().add(actions);

        return box;
    }
}