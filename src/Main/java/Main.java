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
            shrekView = new ImageView(new Image(new FileInputStream("./images/Sherepa.png")));
            shrekView.setViewport(new Rectangle2D(0, 0, 180, 180));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setTitle("Sherepa's Journey");  //  Название окна
        playButton = new Button("Играть");  //  Название кнопок
        helpButton = new Button("Помощь");
        exitButton = new Button("Выход");
        backButton = new Button("Назад");

        playButton.setStyle(buttonStyle);
        helpButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        primaryStage.setOnCloseRequest(e -> System.exit(0));  //  Закрывается окно при нажатии на крестик

        StackPane.setMargin(playButton, new Insets(0, 800, 300, 0));
        StackPane.setMargin(helpButton, new Insets(0, 800, 0, 0));
        StackPane.setMargin(exitButton, new Insets(300, 800, 0, 0));
        StackPane.setMargin(backButton, new Insets(600, 0, 0, 1080));

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


