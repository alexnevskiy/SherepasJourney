import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

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

                if (f && !Sherepa.flick && Sherepa.attackTimer <= 0) { //  Если нажата клавиша f, персонаж не мерцает
                    try {  //  и время атаки меньше или равно 0, то создаётся картинка удара
                        Sherepa.punchView = new ImageView(new Image(new FileInputStream("./images/Punch.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    create((int)player.sherepaView.getX() + Sherepa.width, (int)player.sherepaView.getY(), 40,
                            Sherepa.height, Sherepa.punchView, Main.gameLayout);
                    Sherepa.punchView.setViewport(new Rectangle2D(0, 0, 338, 338));
                    Sherepa.attack = true;  //  Переменная, отвечающая за атаку становится true и выставляется таймер
                    Sherepa.attackTimer = 30;  //  атаки на 30, то есть 0.5 секунды
                }

                if (space && !Sherepa.flick && Beat.reloadTimer == 0) {  //  Если нажат пробел, персонаж не мерцает
                    ImageView beatView = null;  //  и время перезарядки = 0, то создаётся пустая картинка, с последующим
                    try {  //  добавлением png файла
                        beatView = new ImageView(new Image(new FileInputStream("./images/Beat2.png")));
                    } catch (FileNotFoundException e) {  //  Далее идёт создание объекта на экране, если персонаж повёрнут
                        e.printStackTrace();  //  вправо, то звук создаётся справа от игрока, если слева, то слева
                    }
                    if (moveRight) create((int)player.sherepaView.getX() + Sherepa.width, (int)player.sherepaView.getY()
                                    + Sherepa.height / 2, 32, 21, beatView, Main.gameLayout);
                    else create((int)player.sherepaView.getX() - 32, (int)player.sherepaView.getY()
                            + Sherepa.height / 2, 32, 21, beatView, Main.gameLayout);
                    Random random = new Random();  //  Генерируется случайное число для выбора случайной картинки звука
                    Beat beat = new Beat(beatView, random.nextInt(2), moveRight);
                    beat.beatView.setViewport(new Rectangle2D(beat.random * 640, 0, 640, 210));
                    Beat.beats.add(beat);  //  Новая картинка звука добавляется в лист со звками и таймер перезарядки
                    Beat.reloadTimer = 60;  //  выставляется на 60, то есть на 1 секунду
                }

                if (left && playerLeft >= 1) {
                    player.sherepaView.setViewport(new Rectangle2D(128, 0, 128, 192));
                    player.moveX(-speedX);
                    moveLeft = true;
                    moveRight = false;
                }

                if (right && playerLeft <= levelWidth - Sherepa.width - 1) {
                    player.sherepaView.setViewport(new Rectangle2D(0, 0, 128, 192));
                    player.moveX(speedX);
                    moveRight = true;
                    moveLeft = false;
                }

                if (up && playerUp >= 1) player.jumpPlayer();

                //  Блок кода для обработки класса Beat
                if (!Beat.beats.isEmpty()) for (Beat beat : Beat.beats) {  //  Если лист со звуками не пустой, то
                    if (beat.right) beat.moveX(5);  //  проходит по этому листу и взависимости от направления звука
                    else beat.moveX(-5);  //  передвигает его
                }

                if (Beat.reloadTimer > 0) Beat.reloadTimer --;

                // Блок кода для ближнего боя
                if (moveRight && Sherepa.attack) {  //  Обработка картинки во время удара и игрока, смотрящего вправо
                    Sherepa.punchView.setX(player.sherepaView.getX() + Sherepa.width);
                    Sherepa.punchView.setViewport(new Rectangle2D(0, 0, 338, 338));
                }

                if (moveLeft && Sherepa.attack) {  //  Обработка картинки во время удара и игрока, смотрящего влево
                    Sherepa.punchView.setX(player.sherepaView.getX() - 40);
                    Sherepa.punchView.setViewport(new Rectangle2D(338, 0, 338, 338));
                }

                player.punch();  //  Проверка на атаку в ближнем бою

                if (!Sherepa.flick && !Sherepa.attack) player.takingDamage();  //  Проверка на получение урона

                speedY++;
                player.moveY(speedY);

                //  Блок кода для flickTimer
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

                //  Блок кода для attackTimer
                Sherepa.attackTimer--;

                if (Sherepa.attackTimer == 0  && !Sherepa.flick) {  //  Если время атаки закончилось и игрок не мерцает,
                    if (moveRight) player.sherepaView.setViewport(new Rectangle2D(0, 0, 128, 192));
                    if (moveLeft) player.sherepaView.setViewport(new Rectangle2D(128, 0, 128, 192));
                    Main.gameLayout.getChildren().remove(Sherepa.punchView);  //  то проверка на состояние игрока,
                    Sherepa.attack = false;  //  удаление картинки удара и переменная, отвечающая за атаку, становится false
                }

                if (playerLeft > 640 && playerLeft < levelWidth - 640)  //  Передвижение заднего фона вместе с игроком
                    Main.gameLayout.setLayoutX(-(playerLeft - 640));
            }
        };
        timer.start();
    }
}
