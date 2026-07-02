package it.unicam.cs.mpgc.rpg125681.view;

import it.unicam.cs.mpgc.rpg125681.controller.GameSession;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HubView {

    private final GameSession session;

    public HubView(GameSession session) {
        this.session = session;
    }

    public Parent getNode() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));

        Label title = new Label("Hub");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.setTop(title);
        BorderPane.setMargin(title, new Insets(0, 0, 12, 0));

        HBox columns = new HBox(16, statsColumn(), shopColumn(), inventoryColumn());
        HBox.setHgrow(columns, Priority.ALWAYS);
        root.setCenter(columns);

        root.setBottom(actions());
        BorderPane.setMargin(columns, new Insets(0, 0, 12, 0));
        return root;
    }

    private VBox statsColumn() {
        Player player = session.getPlayer();
        VBox box = titledColumn("Character");
        box.getChildren().addAll(
                new Label("Class: " + player.getPlayerClass()),
                new Label("HP: " + player.getHp() + "/" + player.getMaxHp()),
                new Label("Level: " + player.getLevel() + " (" + player.getExp() + "/" + player.getMaxExp() + ")"),
                new Label("Attack: " + player.getAttackPower()),
                new Label("Defense: " + player.getDefense()),
                new Label("Gold: " + player.getGold()),
                new Label("Max depth: " + player.getMaxDepthReached()),
                new Label("Weapon: " + itemName(player.getInventory().getEquippedWeapon())),
                new Label("Armor: " + itemName(player.getInventory().getEquippedArmor())));
        return box;
    }

    private VBox shopColumn() {
        Player player = session.getPlayer();
        VBox list = new VBox(6);
        for (ItemType type : session.getShopCatalog()) {
            Button buy = new Button(type.getDisplayName() + "  (" + type.getBasePrice() + "g)");
            buy.setMaxWidth(Double.MAX_VALUE);
            buy.setDisable(player.getGold() < type.getBasePrice());
            buy.setOnAction(event -> session.buy(type));
            list.getChildren().add(buy);
        }
        if (list.getChildren().isEmpty()) {
            list.getChildren().add(new Label("Nothing available."));
        }
        VBox box = titledColumn("Shop");
        box.getChildren().add(scrollable(list));
        return box;
    }

    private VBox inventoryColumn() {
        Player player = session.getPlayer();
        VBox list = new VBox(6);
        for (Item item : player.getInventory().getItems()) {
            ItemType type = item.getType();
            Button action = new Button();
            action.setMaxWidth(Double.MAX_VALUE);
            if (type.getCategory().isConsumable()) {
                action.setText("Use " + type.getDisplayName());
                action.setOnAction(event -> session.useItem(item));
            } else {
                boolean canEquip = type.getAllowedClasses().contains(player.getPlayerClass());
                action.setText((canEquip ? "Equip " : "Locked ") + type.getDisplayName());
                action.setDisable(!canEquip);
                if (canEquip) {
                    action.setOnAction(event -> session.equip(item));
                }
            }
            list.getChildren().add(action);
        }
        if (list.getChildren().isEmpty()) {
            list.getChildren().add(new Label("Inventory empty."));
        }
        VBox box = titledColumn("Inventory");
        box.getChildren().add(scrollable(list));
        return box;
    }

    private HBox actions() {
        Button enter = new Button("Enter dungeon");
        enter.setOnAction(event -> session.enterDungeon());
        Button leaderboard = new Button("Leaderboard");
        leaderboard.setOnAction(event -> session.openLeaderboard());
        Button bestiary = new Button("Bestiary");
        bestiary.setOnAction(event -> session.openBestiary());
        TextField slot = new TextField("slot1");
        slot.setPrefWidth(120);
        Button save = new Button("Save");
        save.setOnAction(event -> {
            String name = slot.getText().trim();
            if (!name.isEmpty()) {
                session.save(name);
            }
        });
        HBox box = new HBox(10, enter, leaderboard, bestiary, new Label("Slot:"), slot, save);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private VBox titledColumn(String heading) {
        Label label = new Label(heading);
        label.setStyle("-fx-font-weight: bold;");
        VBox box = new VBox(6, label);
        box.setPrefWidth(260);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private ScrollPane scrollable(VBox content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(380);
        return scroll;
    }

    private String itemName(Item item) {
        return item == null ? "—" : item.getType().getDisplayName();
    }
}