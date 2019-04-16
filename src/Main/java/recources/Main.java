package recources;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    Button playButton;
    Button helpButton;
    Button exitButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sherepa's Journey");
        playButton = new Button("Играть");
        helpButton = new Button("Помощь");
        exitButton = new Button("Выход");

        StackPane layout = new StackPane();
        layout.getChildren().add(playButton);
        layout.getChildren().add(helpButton);
        layout.getChildren().add(exitButton);

        exitButton.setOnAction(e -> primaryStage.close());

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
