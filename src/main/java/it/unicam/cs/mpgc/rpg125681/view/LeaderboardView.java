package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeaderboardView {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final GameSession session;

    public LeaderboardView(GameSession session) {
        this.session = session;
    }

    public Parent getNode() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));

        Label title = new Label("Leaderboard");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        box.getChildren().add(title);

        List<RunRecord> history = session.getRunHistory();
        VBox rows = new VBox(4);
        rows.setAlignment(Pos.CENTER_LEFT);
        if (history.isEmpty()) {
            rows.getChildren().add(new Label("No runs recorded yet."));
        } else {
            for (RunRecord run : history) {
                rows.getChildren().add(new Label(formatRow(run)));
            }
        }
        ScrollPane scroll = new ScrollPane(rows);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(420);
        box.getChildren().add(scroll);

        Button back = new Button("Back to hub");
        back.setOnAction(event -> session.returnToHub());
        box.getChildren().add(back);

        return box;
    }

    private String formatRow(RunRecord run) {
        String result = run.outcome() == RunOutcome.VICTORY ? "VICTORY" : "DEFEAT";
        String when = LocalDateTime.ofInstant(Instant.ofEpochMilli(run.timestamp()), ZoneId.systemDefault()).format(DATE);
        return String.format("%-8s  depth %-2d  kills %-3d  lv %-2d   %s",
                result, run.maxDepth(), run.totalKills(), run.level(), when);
    }
}