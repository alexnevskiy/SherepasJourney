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
import java.io.FileNotFoundException;


public class Main extends Application {
    Button playButton;  //  Кнопки в меню
    Button helpButton;
    Button exitButton;
    Button backButton;
    Button restartButton;
    Button menuButton;
    Button endButton;

    public static Stage window = new Stage();

    public static Pane gameLayout;
    public static Scene gameScene;
    public static Scene mainScene;
    public static Scene helpScene;
    public static Scene diedScene;
    public static Scene endGameScene;
    //private Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));

    String buttonStyle = "-fx-padding: 8 15 15 15;\n" +
            "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" +
            "    -fx-background-radius: 8;\n" +
            "    -fx-background-color: \n" +
            "        linear-gradient(from 0% 93% to 0% 100%, #13A32B 0%, #105C19 100%),\n" +
            "        #469D24,\n" +
            "        #64D83A,\n" +
            "        radial-gradient(center 50% 50%, radius 100%, #3CAA3C, #1FFF20);\n" +
            "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #395306;" +
            "    -fx-font-size: 4.0em;";

    public Main() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        primaryStage.setTitle("Sherepa's Journey");  //  Название окна
        playButton = new Button("Играть");  //  Название кнопок
        helpButton = new Button("Помощь");
        exitButton = new Button("Выход");
        backButton = new Button("Назад");
        restartButton = new Button("Начать заново");
        menuButton = new Button("В главное меню");
        endButton = new Button("Выйти в главное меню");

        playButton.setStyle(buttonStyle);
        helpButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
        restartButton.setStyle(buttonStyle);
        menuButton.setStyle(buttonStyle);
        endButton.setStyle(buttonStyle);

        primaryStage.setOnCloseRequest(e -> System.exit(0));  //  Закрывается окно при нажатии на крестик

        StackPane.setMargin(playButton, new Insets(0, 800, 300, 0));
        StackPane.setMargin(helpButton, new Insets(0, 800, 0, 0));
        StackPane.setMargin(exitButton, new Insets(300, 800, 0, 0));
        StackPane.setMargin(backButton, new Insets(600, 0, 0, 1080));
        StackPane.setMargin(restartButton, new Insets(0, 0, 150, 0));
        StackPane.setMargin(menuButton, new Insets(0, 0, -150, 0));
        StackPane.setMargin(endButton, new Insets(0, 0, 0, 0));

        Image background = new Image(new FileInputStream("./images/BackgroundMenu.jpg"), 1280.0, 720.0, true, true);
        Image backgroundGame = new Image(new FileInputStream("./images/BackgroundGame.jpg"));
        Image backgroundHelp = new Image(new FileInputStream("./images/BackgroundHelp.jpg"), 1280.0, 720.0, true, true);
        Image backgroundDied = new Image(new FileInputStream("./images/BackgroundDied.jpg"), 1280.0, 720.0, true, true);
        Image backgroundEndGame = new Image(new FileInputStream("./images/BackgroundEndGame.jpg"), 1280.0, 720.0, true, true);
        ImageView backgroundMenu = new ImageView(background);
        ImageView backgroundHelpMenu = new ImageView(backgroundHelp);
        ImageView backgroundGameView = new ImageView(backgroundGame);
        ImageView backgroundDiedView = new ImageView(backgroundDied);
        ImageView backgroundEndGameView = new ImageView(backgroundEndGame);

        StackPane layout = new StackPane(backgroundMenu, playButton, helpButton, exitButton, backButton);
        StackPane helpLayout = new StackPane(backgroundHelpMenu, backButton);
        StackPane deadLayout = new StackPane(backgroundDiedView, restartButton, menuButton);
        StackPane endGameLayout = new StackPane(backgroundEndGameView, endButton);
        mainScene = new Scene(layout, 1280, 720);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        helpScene = new Scene(helpLayout, 1280, 720);
        diedScene = new Scene(deadLayout, 1280, 720);
        endGameScene = new Scene(endGameLayout, 1280, 720);

        playButton.setOnAction(e -> {
            gameLayout = new Pane(backgroundGameView);
            window.setScene(gameScene = new Scene(gameLayout, 1280, 720));
            Game game = new Game();
            try {
                game.start();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        helpButton.setOnAction(e -> window.setScene(helpScene));
        exitButton.setOnAction(e -> window.close());
        backButton.setOnAction(e -> window.setScene(mainScene));
        restartButton.setOnAction(e -> {
            gameLayout = new Pane(backgroundGameView);
            window.setScene(gameScene = new Scene(gameLayout, 1280, 720));
            Game game = new Game();
            try {
                game.start();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        menuButton.setOnAction(e -> window.setScene(mainScene));
        endButton.setOnAction(e -> window.setScene(mainScene));
    }
}