package it.unicam.cs.mpgc.rpg125681;

import it.unicam.cs.mpgc.rpg125681.controller.GameController;
import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import it.unicam.cs.mpgc.rpg125681.model.game.GameWorldFactory;
import it.unicam.cs.mpgc.rpg125681.persistence.FileGameRepository;
import it.unicam.cs.mpgc.rpg125681.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg125681.view.GameView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.nio.file.Path;

public class App extends Application{

    @Override
    public void start(Stage stage) {
        GameWorldFactory factory = new GameWorldFactory();
        GameRepository repository = new FileGameRepository(Path.of("saves"));
        GameController controller = new GameController(factory, repository);

        controller.newGame(PlayerClass.WARRIOR);
        GameView view = new GameView(controller);

        Scene scene = new Scene(view.getRoot());
        scene.setOnKeyPressed(event -> view.handleKey(event.getCode()));

        stage.setTitle("Game Title");
        stage.setScene(scene);
        stage.show();
    }

}
