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
    static boolean moveRight;
    static boolean moveLeft;
    static boolean canJump;
    int levelWidth = LevelStructure.LEVEL1[0].length() * 60;
    static int speedY = 1;
    static int speedX = 4;
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

    Sherepa player = new Sherepa(create(100, 100, Sherepa.width, Sherepa.height, Main.shrekView, Main.gameLayout));

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
            if (keycode.equals(KeyCode.F) && !Sherepa.flick) f = true;
        });

        Main.gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = false;
            if (keycode.equals(KeyCode.A)) left = false;
            if (keycode.equals(KeyCode.D)) right = false;
            if (keycode.equals(KeyCode.F)) f = false;
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Enemy enemy : Enemy.enemys) {
                    enemy.moveX();
                }

                playerUp = player.sherepaView.getY();
                playerDown = playerUp + Sherepa.height;
                playerLeft = player.sherepaView.getX();
                playerRight = playerLeft + Sherepa.width;

                if (f && !Sherepa.flick) { //  Если нажата клавиша f и персонаж не мерцает, то ...
                    Sherepa.width = 84;  //  Атака происходит счёт расширения ImageView, в котором находится картинка
                    player.sherepaView.setFitWidth(Sherepa.width);  //  бьющего персонажа
                    Sherepa.attack = true;  //  Переменная, отвечающая за атаку становится true и выставляется таймер
                    Sherepa.attackTimer = 30;  //  атаки на 30, то есть 0.5 секунды
                }

                if (left && playerLeft >= 1) {
                    if (!Sherepa.attack) player.sherepaView.setViewport(new Rectangle2D(128, 0, 128, 192));
                    player.moveX(-speedX);
                    moveLeft = true;
                    moveRight = false;
                }

                if (right && playerLeft <= levelWidth - Sherepa.width - 1) {
                    if (!Sherepa.attack) player.sherepaView.setViewport(new Rectangle2D(0, 0, 128, 192));
                    player.moveX(speedX);
                    moveRight = true;
                    moveLeft = false;
                }

                if (moveRight && Sherepa.attack) player.sherepaView.setViewport(new Rectangle2D(256, 0, 168, 192));

                if (moveLeft && Sherepa.attack) player.sherepaView.setViewport(new Rectangle2D(424, 0, 168, 192));

                if (up && playerUp >= 1) player.jumpPlayer();

                player.punch();  //  Проверка на атаку в ближнем бою

                if (!Sherepa.flick && !Sherepa.attack) player.takingDamage();  //  Проверка на получение урона

                speedY++;
                player.moveY(speedY);

                if (Sherepa.flickTimer > 0) {
                    Sherepa.flickTimer--;  //  Уменьшение таймера мерцания персонажа
                    if (Sherepa.flickTimer % 10 < 5) {
                        player.sherepaView.setVisible(false);
                    } else player.sherepaView.setVisible(true);
                }

                if (Sherepa.flickTimer == 0 && !Sherepa.attack) {
                    Sherepa.flick = false;  //  flick = false, если время мерцания закончилось и нет атаки
                    player.sherepaView.setVisible(true);
                }

                Sherepa.attackTimer--;

                if (Sherepa.attackTimer == 0  && !Sherepa.flick) {  //  Если время атаки закончилось и игрок не мерцает,
                    if (moveRight) player.sherepaView.setViewport(new Rectangle2D(0, 0, 128, 192));
                    if (moveLeft) player.sherepaView.setViewport(new Rectangle2D(128, 0, 128, 192));
                    Sherepa.width = 64;
                    player.sherepaView.setFitWidth(Sherepa.width);  //  то ставится обратно картинка с его шириной и
                    Sherepa.attack = false;  //  переменная, отвечающая за атаку становится false
                }

                if (playerLeft > 640 && playerLeft < levelWidth - 640)  //  Передвижение заднего фона вместе с игроком
                    Main.gameLayout.setLayoutX(-(playerLeft - 640));
            }
        };
        timer.start();
    }
}
