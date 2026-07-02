package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuView {

    private final GameSession session;

    public MenuView(GameSession session) {
        this.session = session;
    }

    public Parent getNode() {
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        Label title = new Label("Absorb'em");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        Label newGameLabel = new Label("New game — choose a class");

        HBox classes = new HBox(12,
                classButton("Warrior", PlayerClass.WARRIOR),
                classButton("Rogue", PlayerClass.ROGUE),
                classButton("Sorcerer", PlayerClass.SORCERER));
        classes.setAlignment(Pos.CENTER);

        box.getChildren().addAll(title, newGameLabel, classes);

        List<String> slots = session.savedSlots();
        if (!slots.isEmpty()) {
            box.getChildren().add(new Label("Load game"));
            for (String slot : slots) {
                Button load = new Button("Load: " + slot);
                load.setOnAction(event -> session.load(slot));
                box.getChildren().add(load);
            }
        }
        return box;
    }

    private Button classButton(String label, PlayerClass playerClass) {
        Button button = new Button(label);
        button.setOnAction(event -> session.newGame(playerClass));
        return button;
    }
}