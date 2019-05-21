import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Game {

    boolean up;
    boolean down;
    boolean right;
    boolean left;
    int levelWidth = LevelStructure.LEVEL1[0].length() * 60;

    public static ImageView create(int x, int y, int w, int h, ImageView imageView, Pane gameLayout) {
        imageView.setFitHeight(h);
        imageView.setFitWidth(w);
        imageView.setX(x);
        imageView.setY(y);
        gameLayout.getChildren().add(imageView);
        return imageView;
    }

    Sherepa player = new Sherepa(create(100, 100, 64, 91, Main.shrekView, Main.gameLayout));

    public void createBlocks() throws FileNotFoundException {
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
    }

    public static boolean collision(ImageView image1, ImageView image2) {
        Pair <Double, Double> leftUp1 = new Pair<>(image1.getX(), image1.getY());
        Pair <Double, Double> rightUp1 = new Pair<>(image1.getX() + image1.getFitWidth(), image1.getY());
        Pair <Double, Double> leftDown1 = new Pair<>(image1.getX(), image1.getY() + image1.getFitHeight());
        Pair <Double, Double> rightDown1 = new Pair<>(image1.getX() + image1.getFitWidth(), image1.getY()
                + image1.getFitHeight());  //  Создание пар координат углов image1

        Pair <Double, Double> leftUp2 = new Pair<>(image2.getX(), image2.getY());
        Pair <Double, Double> rightUp2 = new Pair<>(image2.getX() + image2.getFitWidth(), image2.getY());
        Pair <Double, Double> leftDown2 = new Pair<>(image2.getX(), image2.getY() + image2.getFitHeight());
        Pair <Double, Double> rightDown2 = new Pair<>(image2.getX() + image2.getFitWidth(), image2.getY()
                + image2.getFitHeight());  //  Создание пар координат углов image2

        if (leftUp1.getKey() >= leftUp2.getKey() && leftUp1.getKey() <= rightDown2.getKey() &&
                leftUp1.getValue() >= leftUp2.getValue() && leftUp1.getValue() <= rightDown2.getValue())
            return true;  //  Левая верхняя точка image1 находится внутри image2
        if (rightUp1.getKey() >= leftUp2.getKey() && rightUp1.getKey() <= rightDown2.getKey() &&
                rightUp1.getValue() >= leftUp2.getValue() && rightUp1.getValue() <= rightDown2.getValue())
            return true;  //  Правая верхняя точка image1 находится внутри image2
        if (leftDown1.getKey() >= leftUp2.getKey() && leftDown1.getKey() <= rightDown2.getKey() &&
                leftDown1.getValue() >= leftUp2.getValue() && leftDown1.getValue() <= rightDown2.getValue())
            return true;  //  Левая нижняя точка image1 находится внутри image2
        if (rightDown1.getKey() >= leftUp2.getKey() && rightDown1.getKey() <= rightDown2.getKey() &&
                rightDown1.getValue() >= leftUp2.getValue() && rightDown1.getValue() <= rightDown2.getValue())
            return true;  //  Правая нижняя точка image1 находится внутри image2

        if (leftUp2.getKey() >= leftUp1.getKey() && leftUp2.getKey() <= rightDown1.getKey() &&
                leftUp2.getValue() >= leftUp1.getValue() && leftUp2.getValue() <= rightDown1.getValue())
            return true;  //  Левая верхняя точка image2 находится внутри image1
        if (rightUp2.getKey() >= leftUp1.getKey() && rightUp2.getKey() <= rightDown1.getKey() &&
                rightUp2.getValue() >= leftUp1.getValue() && rightUp2.getValue() <= rightDown1.getValue())
            return true;  //  Правая верхняя точка image2 находится внутри image1
        if (leftDown2.getKey() >= leftUp1.getKey() && leftDown2.getKey() <= rightDown1.getKey() &&
                leftDown2.getValue() >= leftUp1.getValue() && leftDown2.getValue() <= rightDown1.getValue())
            return true;  //  Левая нижняя точка image2 находится внутри image1
        if (rightDown2.getKey() >= leftUp1.getKey() && rightDown2.getKey() <= rightDown1.getKey() &&
                rightDown2.getValue() >= leftUp1.getValue() && rightDown2.getValue() <= rightDown1.getValue())
            return true;  //  Правая нижняя точка image2 находится внутри image1
        return false;
    }

    public void start() throws FileNotFoundException {
        createBlocks();
        Main.gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = true;
            if (keycode.equals(KeyCode.A)) left = true;
            if (keycode.equals(KeyCode.D)) right = true;
            if (keycode.equals(KeyCode.S)) down = true;
        });

        Main.gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = false;
            if (keycode.equals(KeyCode.A)) left = false;
            if (keycode.equals(KeyCode.D)) right = false;
            if (keycode.equals(KeyCode.S)) down = false;
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                boolean col = false;
                for (ImageView platform : BlockProperties.platforms) {
                    if (collision(player.sherepaView, platform)) {
                        col = true;
                        if (player.sherepaView.getX() + player.sherepaView.getFitWidth() == platform.getX())
                            player.sherepaView.setX(player.sherepaView.getX() - 1);
                        if (player.sherepaView.getX() == platform.getX() + platform.getFitWidth())
                            player.sherepaView.setX(player.sherepaView.getX() + 1);
                        if (player.sherepaView.getY() + player.sherepaView.getFitHeight() == platform.getY())
                            player.sherepaView.setY(player.sherepaView.getY() - 1);
                        if (player.sherepaView.getY() == platform.getY() + platform.getFitHeight())
                            player.sherepaView.setY(player.sherepaView.getX() + 1);
                        break;
                    } else col = false;
                }

                if (!col) {
                    if (up && player.sherepaView.getY() >= 1) player.moveY(-1);
                    if (down) player.moveY(1);
                    if (left && player.sherepaView.getX() >= 1) player.moveX(-1);
                    if (right && player.sherepaView.getX() <= levelWidth - player.sherepaView.getFitWidth() - 1)
                        player.moveX(1);
                }
                //  Передвижение заднего фона вместе с игроком
                if (player.sherepaView.getX() > 640 && player.sherepaView.getX() < levelWidth - 640)
                    Main.gameLayout.setLayoutX(-(player.sherepaView.getX() - 640));

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
