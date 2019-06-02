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

    public void createBlocks() throws FileNotFoundException {
        for (int i = 0; i < LevelStructure.LEVEL1.length; i++) {
            String line = LevelStructure.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        ImageView UpLayer = new ImageView(new Image(new FileInputStream("./images/UpLayer.png")));
                        ImageView UpLayerPlatform = create(j * 60, i * 60, 60, 60, UpLayer, Main.gameLayout);
                        BlockProperties.platforms.add(UpLayerPlatform);
                        break;
                    case '2':
                        ImageView downLayer = new ImageView(new Image(new FileInputStream("./images/DownLayer.png")));
                        ImageView downLayerPlatform = create(j * 60, i * 60, 60, 60, downLayer, Main.gameLayout);
                        BlockProperties.platforms.add(downLayerPlatform);
                        break;
                    case '3':
                        ImageView Block = new ImageView(new Image(new FileInputStream("./images/Block.png")));
                        ImageView BlockPlatform = create(j * 60, i * 60, 60, 60, Block, Main.gameLayout);
                        BlockProperties.platforms.add(BlockPlatform);
                        break;
                    case '4':
                        ImageView Dirt = new ImageView(new Image(new FileInputStream("./images/Dirt.png")));
                        ImageView DirtPlatform = create(j * 60, i * 60, 60, 60, Dirt, Main.gameLayout);
                        BlockProperties.platforms.add(DirtPlatform);
                        break;
                    case '5':
                        ImageView DirtStone = new ImageView(new Image(new FileInputStream("./images/DirtStone.png")));
                        ImageView DirtStonePlatform = create(j * 60, i * 60, 60, 60, DirtStone, Main.gameLayout);
                        BlockProperties.platforms.add(DirtStonePlatform);
                        break;
                    case '6':
                        ImageView Flag = new ImageView(new Image(new FileInputStream("./images/Flag.png")));
                        ImageView FlagPlatform = create(j * 60, i * 60, 60, 60, Flag, Main.gameLayout);
                        BlockProperties.platforms.add(FlagPlatform);
                        break;
                    case '9':
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
                playerDown = playerUp + Sherepa.height;
                playerLeft = player.sherepaView.getX();
                playerRight = playerLeft + Sherepa.width;

                if (f && !Sherepa.flick && Sherepa.attackTimer <= 0) { //  Если нажата клавиша f, персонаж не мерцает
                    try {  //  и время атаки меньше или равно 0, то создаётся картинка удара
                        Sherepa.punchView = new ImageView(new Image(new FileInputStream("./images/Punch.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    create((int)player.sherepaView.getX() + Sherepa.width, (int)player.sherepaView.getY() + 20,
                            36, 36, Sherepa.punchView, Main.gameLayout);
                    Sherepa.punchView.setViewport(new Rectangle2D(0, 0, 338, 338));
                    Sherepa.attack = true;  //  Переменная, отвечающая за атаку становится true и выставляется таймер
                    Sherepa.attackTimer = 30;  //  атаки на 30, то есть 0.5 секунды
                }

                if (space && !Sherepa.flick && Beat.reloadTimer == 0) {  //  Если нажат пробел, персонаж не мерцает
                    ImageView beatView = null;  //  и время перезарядки = 0, то создаётся пустая картинка, с последующим
                    try {  //  добавлением png файла
                        beatView = new ImageView(new Image(new FileInputStream("./images/Beat.png")));
                    } catch (FileNotFoundException e) {  //  Далее идёт создание объекта на экране, если персонаж повёрнут
                        e.printStackTrace();  //  вправо, то звук создаётся справа от игрока, если слева, то слева
                    }  //  Реализован выбор случайной ноты и случайного цвета
                    Random randomWidth = new Random();  //  Создаётся рандомный генератор, который генерирует от 0 до 2
                    int beatWidthRandom = randomWidth.nextInt(3);
                    int beatWidth = 0;  //  При помощи данной конструкции определяется ширина картинки
                    switch (beatWidthRandom) {
                        case 0 :
                            beatWidth = 14;  //  Двойная нота
                            break;
                        case 1 :
                            beatWidth = 10;  //  Нота с хвостиком
                            break;
                        case 2:
                            beatWidth = 6;  //  Одна нота
                            break;
                    }  //  Создание картинки ноты с нужными параметрами на экране
                    if (moveRight) create((int)player.sherepaView.getX() + Sherepa.width, (int)player.sherepaView.getY()
                            + 18, beatWidth * 2, 24, beatView, Main.gameLayout);
                    else create((int)player.sherepaView.getX() - beatWidth * 2, (int)player.sherepaView.getY()
                            + 18, beatWidth * 2, 28, beatView, Main.gameLayout);
                    Random random = new Random();  //  Генерируется случайное число для выбора случайного цвета
                    Beat beat = new Beat(beatView, random.nextInt(7), moveRight);
                    if (moveRight) beat.beatView.setViewport(new Rectangle2D(beat.color * beatWidth * 10,
                            beatWidthRandom * 12 * 10, beatWidth * 10, 120));
                    if (moveLeft) beat.beatView.setViewport(new Rectangle2D(beat.color * beatWidth * 10,
                            beatWidthRandom * 12 * 10 + 360, beatWidth * 10, 120));
                    Beat.beats.add(beat);  //  Новая картинка звука добавляется в лист со звуками и таймер перезарядки
                    Beat.reloadTimer = 30;  //  выставляется на 30, то есть на 0,5 секунды
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

                //  Блок кода для обработки класса Beat
                if (!Beat.beats.isEmpty()) for (Beat beat : Beat.beats) {  //  Если лист со звуками не пустой, то
                    if (beat.right) beat.moveX(5);  //  проходит по этому листу и взависимости от направления звука
                    else beat.moveX(-5);  //  передвигает его
                }

                if (Beat.reloadTimer > 0) Beat.reloadTimer --;

                // Блок кода для ближнего боя
                if (moveRight && Sherepa.attack) {  //  Обработка картинки во время удара и игрока, смотрящего вправо
                    Sherepa.punchView.setX(player.sherepaView.getX() + Sherepa.width);
                    Sherepa.punchView.setViewport(new Rectangle2D(0, 0, 130, 130));
                }

                if (moveLeft && Sherepa.attack) {  //  Обработка картинки во время удара и игрока, смотрящего влево
                    Sherepa.punchView.setX(player.sherepaView.getX() - 36);
                    Sherepa.punchView.setViewport(new Rectangle2D(130, 0, 130, 130));
                }

                player.punch();  //  Проверка на атаку в ближнем бою

                if (!Sherepa.flick && !Sherepa.attack) player.takingDamage();  //  Проверка на получение урона

                player.isDied();  //  Проверка на смерть персонажа

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
                    if (moveRight) player.sherepaView.setViewport(new Rectangle2D(0, 0, 180, 180));
                    if (moveLeft) player.sherepaView.setViewport(new Rectangle2D(180, 0, 180, 180));
                    Main.gameLayout.getChildren().remove(Sherepa.punchView);  //  то проверка на состояние игрока,
                    Sherepa.attack = false;  //  удаление картинки удара и переменная, отвечающая за атаку, становится false
                }

                if (playerLeft > 640 && playerLeft < levelWidth - 640) { //  Передвижение заднего фона вместе с игроком
                    Main.gameLayout.setLayoutX(-(playerLeft - 640));
                    healthView.setX(playerLeft - 640);
                }

                if (Sherepa.died || playerRight >= levelWidth - 180) {
                    Sherepa.died = false;
                    Sherepa.health = 5;
                    Main.gameLayout.getChildren().clear();
                    if (Sherepa.died) Main.window.setScene(Main.diedScene);
                    else Main.window.setScene(Main.endGameScene);
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
            }
        };
        timer.start();
    }
}