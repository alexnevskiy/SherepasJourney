import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    boolean up;
    boolean right;
    boolean left;

    public static ImageView create(int x, int y, int w, int h, ImageView imageView, Pane gameLayout) {
        imageView.setFitHeight(h);
        imageView.setFitWidth(w);
        imageView.setX(x);
        imageView.setY(y);
        gameLayout.getChildren().add(imageView);
        return imageView;
    }

    Sherepa sherepa = new Sherepa(create(100, 100, 192, 192, Main.shrekView, Main.gameLayout));

    public void createBlocks() throws FileNotFoundException {
        int levelWidth = LevelStructure.LEVEL1[0].length() * 60;

        for (int i = 0; i < LevelStructure.LEVEL1.length; i++) {
            String line = LevelStructure.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        ImageView blockView = new ImageView(new Image(new FileInputStream("./images/Block.png")));
                        ImageView platform = create(j * 60, i * 60, 60, 60, blockView, Main.gameLayout);
                        BlockProperties.platforms.add(platform);
                        break;
                }
            }
        }

        sherepa.translateXProperty().addListener((obs, old, newValue) -> {  //  Смещение заднего фона
            int position = newValue.intValue();

            if (position > 640 && position < levelWidth - 640) {
                Main.gameLayout.setLayoutX(-(position - 640));
            }
        });
    }

    public void start() throws FileNotFoundException {
        createBlocks();
        Main.gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = true;
            if (keycode.equals(KeyCode.A)) left = true;
            if (keycode.equals(KeyCode.D)) right = true;
        });

        Main.gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
                    KeyCode keycode = key.getCode();
                    if (keycode.equals(KeyCode.W)) up = false;
                    if (keycode.equals(KeyCode.A)) left = false;
                    if (keycode.equals(KeyCode.D)) right = false;
        });
        Timer timer = new Timer();

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
                }
                if (left) sherepa.move(-1);
                if (right) sherepa.move(1);
            }
        };
        timer.schedule(timerTask, 0, 5);
    }
}
