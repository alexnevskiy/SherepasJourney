import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
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
    boolean f;
    boolean space;
    static boolean moveRight = true;
    static boolean moveLeft;
    static boolean canJump;
    static int levelWidth = LevelStructure.LEVEL1[0].length() * 60;
    static int speedY = 1;
    static int speedX = 4;
    double playerUp;
    double playerLeft;
    double playerRight;
    AnimationTimer timer;
    static ImageView healthView;
    static int healthWidth = 590;
    static int healthHeight = 110;
    static ImageView playerView;
    static {
        try {
            playerView = new ImageView(new Image(new FileInputStream("./images/Sherepa.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        playerView.setViewport(new Rectangle2D(0, 0, 180, 180));
    }


    public static ImageView create(int x, int y, int w, int h, ImageView imageView, Pane gameLayout) {
        imageView.setFitHeight(h);
        imageView.setFitWidth(w);
        imageView.setX(x);
        imageView.setY(y);
        gameLayout.getChildren().add(imageView);
        return imageView;
    }

    Sherepa player = new Sherepa(create(0, 500, Sherepa.width, Sherepa.height, playerView, Main.gameLayout));

    public void restart() {
        Main.gameLayout.getChildren().clear();
        if (Sherepa.died) Main.window.setScene(Main.diedScene);
        else Main.window.setScene(Main.endGameScene);
        Sherepa.died = false;
        Sherepa.health = 5;
        speedY = 1;
        healthWidth = 590;
        healthHeight = 110;
        Sherepa.flick = false;
        Sherepa.flickTimer = -1;
        Sherepa.attack = false;
        Sherepa.attackTimer = 0;
        Enemy.enemys.clear();
        Beat.beats.clear();
        timer.stop();
    }

    public void start() throws FileNotFoundException {
        BlockProperties.createBlocks();

        healthView = new ImageView(new Image(new FileInputStream("./images/Health.png")));
        healthView.setViewport(new Rectangle2D(0, 0, healthWidth, healthHeight));
        healthView.setFitWidth(healthWidth / 2.5);
        healthView.setFitHeight(healthHeight / 2.5);
        Main.gameLayout.getChildren().add(healthView);

        Main.gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = true;
            if (keycode.equals(KeyCode.A)) left = true;
            if (keycode.equals(KeyCode.D)) right = true;
            if (keycode.equals(KeyCode.F) && !Sherepa.flick) f = true;
            if (keycode.equals(KeyCode.SPACE) && Beat.reloadTimer == 0) space = true;
        });

        Main.gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = false;
            if (keycode.equals(KeyCode.A)) left = false;
            if (keycode.equals(KeyCode.D)) right = false;
            if (keycode.equals(KeyCode.F)) f = false;
            if (keycode.equals(KeyCode.SPACE)) space = false;
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Enemy enemy : Enemy.enemys) {
                    enemy.moveX();
                }

                playerUp = player.sherepaView.getY();
                playerLeft = player.sherepaView.getX();
                playerRight = playerLeft + Sherepa.width;

                if (f && !Sherepa.flick && Sherepa.attackTimer <= 0) { //  Если нажата клавиша f, персонаж не мерцает,
                    player.hit();  //  то происходит удар
                }

                if (space && !Sherepa.flick && Beat.reloadTimer == 0) {  //  Если нажат пробел, персонаж не мерцает
                    player.beat();  //  и время перезарядки = 0, то игрок стреляет звуками
                }

                if (left && playerLeft >= 1) {
                    player.sherepaView.setViewport(new Rectangle2D(180, 0, 180, 180));
                    player.moveX(-speedX);
                    moveLeft = true;
                    moveRight = false;
                }

                if (right && playerLeft <= levelWidth - Sherepa.width - 1) {
                    player.sherepaView.setViewport(new Rectangle2D(0, 0, 180, 180));
                    player.moveX(speedX);
                    moveRight = true;
                    moveLeft = false;
                }

                if (up && playerUp >= 1) player.jumpPlayer();

                Beat.check();  //  Блок кода для обработки класса Beat

                player.punchCheck();  // Блок кода для ближнего боя

                player.punch();  //  Проверка на атаку в ближнем бою

                if (!Sherepa.flick && !Sherepa.attack) player.takingDamage();  //  Проверка на получение урона

                player.flickCheck();  //  Блок кода для flickTimer

                player.isDied();  //  Проверка на смерть персонажа

                speedY++;
                player.moveY(speedY);

                player.hitCheck();  //  Блок кода для attackTimer

                if (playerLeft > 640 && playerLeft < levelWidth - 640) { //  Передвижение заднего фона вместе с игроком
                    Main.gameLayout.setLayoutX(-(playerLeft - 640));
                    healthView.setX(playerLeft - 640);
                }

                if (Sherepa.died || playerRight >= levelWidth - 180) restart();
            }
        };
        timer.start();
    }
}