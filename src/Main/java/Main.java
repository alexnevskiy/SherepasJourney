import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {
    Button playButton;  //  Кнопки в меню
    Button helpButton;
    Button exitButton;
    Button backButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Sherepa's Journey");  //  Название окна
        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        playButton = new Button("Играть");  //  Название кнопок
        helpButton = new Button("Помощь");
        exitButton = new Button("Выход");
        backButton = new Button("Назад");

        StackPane.setMargin(playButton, new Insets(0, 0, 300, 0));
        StackPane.setMargin(helpButton, new Insets(0, 0, 0, 0));
        StackPane.setMargin(exitButton, new Insets(300, 0, 0, 0));
        StackPane.setMargin(backButton, new Insets(600, 0, 0, 1200));

        Image background = new Image(new FileInputStream("./images/BackgroundMenu.jpg"), 1280.0, 720.0, true, true);
        Image backgroundGame = new Image(new FileInputStream("./images/BackgroundGame.jpg"), 1280.0, 720.0, true, true);
        ImageView backgroundMenu = new ImageView(background);
        ImageView backgroundHelpMenu = new ImageView(background);
        ImageView backgroundGameView = new ImageView(backgroundGame);

        StackPane layout = new StackPane(backgroundMenu, playButton, helpButton, exitButton, backButton);
        StackPane helpLayout = new StackPane(backgroundHelpMenu, backButton);
        StackPane gameLayout = new StackPane(backgroundGameView);

        Scene mainScene = new Scene(layout, 1280, 720);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        Scene helpScene = new Scene(helpLayout, 1280, 720);
        Scene gameScene = new Scene(gameLayout, 1280, 720);

        playButton.setOnAction(e -> primaryStage.setScene(gameScene));
        helpButton.setOnAction(e -> primaryStage.setScene(helpScene));
        exitButton.setOnAction(e -> primaryStage.close());
        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
    }
}
