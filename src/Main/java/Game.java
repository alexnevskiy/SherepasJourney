import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Game {

    boolean up;
    boolean right;
    boolean left;
    static boolean canJump;
    int levelWidth = LevelStructure.LEVEL1[0].length() * 60;
    static int playerHeight = 91;
    static int playerWight = 64;
    static int speedY = 1;
    int speedX = 4;
    double playerUp;
    double playerDown;
    double playerLeft;
    double playerRight;

    public static ImageView create(int x, int y, int w, int h, ImageView imageView, Pane gameLayout) {
        imageView.setFitHeight(h);
        imageView.setFitWidth(w);
        imageView.setX(x);
        imageView.setY(y);
        gameLayout.getChildren().add(imageView);
        return imageView;
    }

    Sherepa player = new Sherepa(create(100, 100, playerWight, playerHeight, Main.shrekView, Main.gameLayout));

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
                    case '2':
                        ImageView mario = new ImageView(new Image(new FileInputStream("./images/Mario.png")));
                        Enemy enemy = new Enemy(create(j * 60, i * 60 + 19, 40, 40, mario, Main.gameLayout));
                        Enemy.enemys.add(enemy);
                        break;
                }
            }
        }
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
                for (Enemy enemy : Enemy.enemys) {
                    enemy.moveX();
                }
                playerUp = player.sherepaView.getY();
                playerDown = playerUp + playerHeight;
                playerLeft = player.sherepaView.getX();
                playerRight = playerLeft + playerWight;

                if (right && playerLeft <= levelWidth - playerWight - 1) player.moveX(speedX);

                if (left && playerLeft >= 1) player.moveX(-speedX);

                if (up && playerUp >= 1) player.jumpPlayer();

                speedY++;
                player.moveY(speedY);

                //  Передвижение заднего фона вместе с игроком
                if (playerLeft > 640 && playerLeft < levelWidth - 640)
                    Main.gameLayout.setLayoutX(-(playerLeft - 640));
            }
        };
        timer.start();
    }
}
