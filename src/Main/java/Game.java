import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

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

    Sherepa player = new Sherepa(create(200, 500, 192, 192, Main.shrekView, Main.gameLayout));

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

//        sherepa.translateXProperty().addListener((obs, old, newValue) -> {  //  Смещение заднего фона
//            int position = newValue.intValue();
//
//            if (position > 640 && position < levelWidth - 640) {
//                Main.gameLayout.setLayoutX(-(position - 640));
//            }
//        });
    }

    public static boolean collison(ImageView image1, ImageView image2) {
        Pair <Double, Double> point1 = new Pair<>(image1.getX(), image1.getY());
        Pair <Double, Double> point2 = new Pair<>(image1.getX() + image1.getFitWidth(), image1.getY());
        Pair <Double, Double> point3 = new Pair<>(image1.getX(), image1.getY() + image1.getFitHeight());
        Pair <Double, Double> point4 = new Pair<>(image1.getX() + image1.getFitWidth(),
                image1.getY() + image1.getFitHeight());

        Pair <Double, Double> point5 = new Pair<>(image2.getX(), image2.getY());
        Pair <Double, Double> point6 = new Pair<>(image2.getX() + image2.getFitWidth(), image2.getY());
        Pair <Double, Double> point7 = new Pair<>(image2.getX(), image2.getY() + image2.getFitHeight());
        Pair <Double, Double> point8 = new Pair<>(image2.getX() + image2.getFitWidth(),
                image2.getY() + image2.getFitHeight());


        if (point1.getKey() >= point5.getKey() && point1.getKey() <= point8.getKey() &&
                point1.getValue() >= point5.getValue() && point1.getValue() <= point8.getValue())
            return true;
        if (point2.getKey() >= point5.getKey() && point2.getKey() <= point8.getKey() &&
                point2.getValue() >= point5.getValue() && point2.getValue() <= point8.getValue())
            return true;
        if (point3.getKey() >= point5.getKey() && point3.getKey() <= point8.getKey() &&
                point3.getValue() >= point5.getValue() && point3.getValue() <= point8.getValue())
            return true;
        if (point4.getKey() >= point5.getKey() && point4.getKey() <= point8.getKey() &&
                point4.getValue() >= point5.getValue() && point4.getValue() <= point8.getValue())
            return true;

        if (point5.getKey() >= point1.getKey() && point5.getKey() <= point4.getKey() &&
                point5.getValue() >= point1.getValue() && point5.getValue() <= point4.getValue())
            return true;
        if (point6.getKey() >= point1.getKey() && point6.getKey() <= point4.getKey() &&
                point6.getValue() >= point1.getValue() && point6.getValue() <= point4.getValue())
            return true;
        if (point7.getKey() >= point1.getKey() && point7.getKey() <= point4.getKey() &&
                point7.getValue() >= point1.getValue() && point7.getValue() <= point4.getValue())
            return true;
        if (point8.getKey() >= point1.getKey() && point8.getKey() <= point4.getKey() &&
                point8.getValue() >= point1.getValue() && point8.getValue() <= point4.getValue())
            return true;
        return false;
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

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                boolean col = false;
                for (ImageView platform : BlockProperties.platforms) {
                    System.out.println(platform.getX());
                    System.out.println(collison(player.sherepaView, platform));
                    if (collison(player.sherepaView, platform)) {
                       col = true;
                    } else col = false;
                }
                System.out.println(col);
                if (!col) {
                    if (up) player.down(1);
                    if (left) player.move(-1);
                    if (right) player.move(1);
                } else {

                }
                double a = 1;

                double v = 1;
                double g = 0.98;
                v -= g;
                int t = 1;
                double y = 0;
                y += v;

                int t1 = 1;
                t1++;


            }
        };
        timer.start();
    }
}
