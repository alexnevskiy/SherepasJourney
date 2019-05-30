import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main extends Application {
    Button playButton;  //  Кнопки в меню
    Button helpButton;
    Button exitButton;
    Button backButton;

    public static Pane gameLayout;
    public static Scene gameScene;
    //private Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    static ImageView shrekView;

    static {
        try {
            shrekView = new ImageView(new Image(new FileInputStream("./images/Shrek4.png")));
            shrekView.setViewport(new Rectangle2D(0, 0, 128, 192));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Main() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setTitle("Sherepa's Journey");  //  Название окна
        playButton = new Button("Играть");  //  Название кнопок
        helpButton = new Button("Помощь");
        exitButton = new Button("Выход");
        backButton = new Button("Назад");

        primaryStage.setOnCloseRequest(e -> System.exit(0));  //  Закрывается окно при нажатии на крестик

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
        gameLayout = new Pane(backgroundGameView);

        Scene mainScene = new Scene(layout, 1280, 720);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        Scene helpScene = new Scene(helpLayout, 1280, 720);
        gameScene = new Scene(gameLayout, 1280, 720);

        playButton.setOnAction(e -> {
            primaryStage.setScene(gameScene);
            Game game = new Game();
            try {
                game.start();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        helpButton.setOnAction(e -> primaryStage.setScene(helpScene));
        exitButton.setOnAction(e -> primaryStage.close());
        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
    }
}


