package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class BestiaryView {

    private final GameSession session;

    public BestiaryView(GameSession session) {
        this.session = session;
    }

    public Parent getNode() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));

        Label title = new Label("Bestiary");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        box.getChildren().add(title);

        VBox rows = new VBox(4);
        rows.setAlignment(Pos.CENTER_LEFT);
        for (EnemyType type : EnemyType.values()) {
            rows.getChildren().add(new Label(describe(type)));
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

    private String describe(EnemyType type) {
        return String.format("%-10s  HP %-3d  ATK %-2d  EXP %-2d  gold %-2d   %-6s   absorbs %s +%d",
                type.getDisplayName(), type.getBaseHp(), type.getBaseAttack(), type.getBaseExp(),
                type.getBaseGold(), type.getBehaviorKind(), type.getAbsorbStat(), type.getAbsorbAmount());
    }
}