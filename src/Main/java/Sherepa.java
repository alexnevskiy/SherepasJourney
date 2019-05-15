import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Sherepa {

    private boolean up;
    private boolean right;
    private boolean left;

    Scene gameScene;
    ImageView sherepa;

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private int levelWidth;

    public Sherepa(Scene gameScene, ImageView sherepa) {
        this.gameScene = gameScene;
        this.sherepa = sherepa;
    }

    public void move(double x, double y) {  //  Реализация движения персонажа
        Platform.runLater(() -> sherepa.setLayoutX(sherepa.getLayoutX() + x));
        Platform.runLater(() -> sherepa.setLayoutY(sherepa.getLayoutY() + y));
    }

    public void start() {
        sherepa.setX(100);
        sherepa.setY(100);

        gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = true;
            if (keycode.equals(KeyCode.A)) left = true;
            if (keycode.equals(KeyCode.D)) right = true;
        });

        gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = false;
            if (keycode.equals(KeyCode.A)) left = false;
            if (keycode.equals(KeyCode.D)) right = false;
        });

        Timer timer = new Timer();

        Sherepa player = new Sherepa(gameScene, sherepa);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                double a = 1;

                double v = 1;
                double g = 0.98;
                v -= g;
                int t = 1;
                double y = 0;
                y += v;

//                Platform.runLater(() -> {
//                    int t1 = 1;
//                    t1++;
//                    sherepa.setLayoutY(sherepa.getLayoutY() + t1);
//                });

                if (up) {
                    v = -1;
                    a = v * t - g * t * t * 0.5;
                    player.move(0, a);
                }
                if (left) player.move(-1, 0);
                if (right) player.move(1, 0);
            }
        };

        System.out.println(sherepa.getFitWidth());

//        private Node createEntity(int x, int y, int w, int h, Color color) {
//            Rectangle entity = new Rectangle(w, h);
//            entity.setTranslateX(x);
//            entity.setTranslateY(y);
//            entity.setFill(color);
//            entity.getProperties().put("alive", true);
//
//            //gameRoot.getChildren().add(entity);
//            return entity;
//        }
//
//        private void initContent() {
//
//            levelWidth = LevelStructure.LEVEL1[0].length() * 60;
//
//            for (int i = 0; i < LevelStructure.LEVEL1.length; i++) {
//                String line = LevelStructure.LEVEL1[i];
//                for (int j = 0; j < line.length(); j++) {
//                    switch (line.charAt(j)) {
//                        case '0':
//                            break;
//                        case '1':
//                            Node platform = createEntity(j*60, i*60, 60, 60, Color.BROWN);
//                            platforms.add(platform);
//                            break;
//                        case '2':
//                            Node coin = createEntity(j*60, i*60, 60, 60, Color.GOLD);
//                            coins.add(coin);
//                            break;
//                    }
//                }
//            }
//
//            player = createEntity(0, 600, 40, 40, Color.BLUE);
//
//            player.translateXProperty().addListener((obs, old, newValue) -> {
//                int offset = newValue.intValue();
//
//                if (offset > 640 && offset < levelWidth - 640) {
//                    gameRoot.setLayoutX(-(offset - 640));
//                }
//            });
//
//            appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
//        }
//
//        timer.schedule(timerTask, 0, 5);
//    }


    }
}
