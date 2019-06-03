import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Sherepa {

    ImageView sherepaView;
    static int height = 50;
    static int width = 50;
    static int health = 5;
    static boolean flick = false;
    static boolean attack = false;
    static boolean died = false;
    static int flickTimer;
    static int attackTimer = 0;
    static ImageView punchView;

    public Sherepa(ImageView sherepaView) {
        this.sherepaView = sherepaView;
    }

    public void moveX(double x) {  //  Реализация движения персонажа по оси х
        boolean movingRight = x > 0;

        for (int i = 0; i < Math.abs(x); i++) {  //  Проходит по каждому пикселю при заданной скорости
            for (ImageView platform : BlockProperties.platforms) {  //  Проходит по всем платформам
                if (sherepaView.getBoundsInParent().intersects(platform.getBoundsInParent())) {  //  Обнаружение коллизии
                    if (movingRight) {
                        if (sherepaView.getX() + width == platform.getX()) {//  Если правая часть игрока
                            sherepaView.setX(sherepaView.getX() - 1);  //  вступает в коллизию с левой частью платформы,
                            return;  //  то игрок переносится на 1 пиксель влево для того, чтобы правильно работала
                        }  // вертикальная коллизия в следующем методе и цикл завершается
                    }
                    else {
                        if (sherepaView.getX() == platform.getX() + 60) {  //  То же самое, только для левой части
                            sherepaView.setX(sherepaView.getX() + 1);  //  игрока и правой части платформы
                            return;
                        }
                    }
                }
            }
            sherepaView.setX(sherepaView.getX() + (movingRight ? 1 : -1));  //  Если переменная movingRight = true, то
        }  //  передвигаем персонажа на 1 пиксель вправо иначе на 1 пиксель влево. Это служит для того, чтобы правильно
    }  //  обработать коллизию при совпадении правой и левой грани игрока и платформы

    public void moveY(double y) {  //  Реализация движения персонажа по оси у
        boolean movingDown = y > 0;

        for (int i = 0; i < Math.abs(y); i++) {  //  Проходит по каждому пикселю при заданной скорости
            for (ImageView platform : BlockProperties.platforms) {  //  Проходит по всем платформам
                if (sherepaView.getBoundsInParent().intersects(platform.getBoundsInParent())) {  //  Обнаружение коллизии
                    if (movingDown) {
                        if (sherepaView.getY() + height == platform.getY()) {  //  Если нижняя часть игрока
                            sherepaView.setY(sherepaView.getY() - 1);  //  вступает в коллизию с верхней частью
                            Game.speedY = 1;  //  платформы, то игрок переносится на 1 пиксель вверх для того, чтобы
                            Game.canJump = true;  //  правильно работала горизонтальная коллизия в прошлом методе,
                            return;  // скорости по у присваивается значение 1, чтобы физика персонажа правильно работала
                        } // при падении с платформы, переменная, твечающая за возможность совершить прыжок становится
                    }   // true и цикл завершается
                    else {
                        if (sherepaView.getY() == platform.getY() + 60) {  //  Если верхняя часть игрока вступает в коллизию
                            Game.speedY = 0;  //  с нижней частью платформы, то скорость игрока по у становится равной 0
                            return;  //  и цикл завершается
                        }
                    }
                }
            }
            sherepaView.setY(sherepaView.getY() + (movingDown ? 1 : -1));  //  То же самое, что и в прошлом методе,
            if (attack) punchView.setY(sherepaView.getY() + 7);  //  только для вертикального передвижения персонажа и
        }  //  для удара, если переменная attack = true
    }

    public void jumpPlayer() {
        if (Game.canJump) {  //  Если переменная равняется true, то скорость по у персонажа становится -20, то есть он
            Game.speedY = -20;  //  подпрыгивает и переменная становится false
            Game.canJump = false;
        }
    }

    public void takingDamage() {  //  Проверка на получение урона от противника
        for (int i = 0; i < Math.abs(Game.speedX); i++) {  //  Проходит по каждому пикселю при скорости по х
            for (Enemy enemy : Enemy.enemys) {  //  Проходит по всем врагам
                if (sherepaView.getBoundsInParent().intersects(enemy.enemyView.getBoundsInParent())) {  //  Коллизия
                    Game.healthWidth -= 120;
                    if (Game.healthWidth < 0) Game.healthWidth = 0;
                    Game.healthHeight += 110;
                    Game.healthView.setViewport(new Rectangle2D(0, Game.healthHeight - 110, Game.healthWidth, 110));
                    Game.healthView.setFitWidth(Game.healthWidth / 2.5);
                    flick = true;  //  Переменная мерцания персонажа становится true
                    flickTimer = 180;  //  Таймер мерцания ставится на 180, то есть на 3 секунды
                    health--;  //  Жизнь игрока отнимается
                    Game.speedY = -15;  //  Скорость игрока по у становится -15 для того, чтобы персонаж подпрыгнул при
                    this.moveY(Game.speedY);  //  получении урона, затем прыжок персонажа с заданной скоростью
                    return;
                }
            }
        }
    }

    public void flickCheck() {
        if (flickTimer > 0) {
            flickTimer--;  //  Уменьшение таймера мерцания персонажа
            if (flickTimer % 10 < 5) {
                sherepaView.setVisible(false);
            } else sherepaView.setVisible(true);
        }

        if (flickTimer == 0 && !attack) {
            flick = false;  //  flick = false, если время мерцания закончилось и нет атаки
            sherepaView.setVisible(true);
        }
    }

    public void hit() {
        try {  //  и время атаки меньше или равно 0, то создаётся картинка удара
            Sherepa.punchView = new ImageView(new Image(new FileInputStream("./images/Punch.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Game.create((int)sherepaView.getX() + width, (int)sherepaView.getY() + 20,
                36, 36, punchView, Main.gameLayout);
        punchView.setViewport(new Rectangle2D(0, 0, 338, 338));
        attack = true;  //  Переменная, отвечающая за атаку становится true и выставляется таймер
        attackTimer = 30;  //  атаки на 30, то есть 0.5 секунды
    }

    public void hitCheck() {
        Sherepa.attackTimer--;

        if (attackTimer == 0  && !flick) {  //  Если время атаки закончилось и игрок не мерцает,
            if (Game.moveRight) sherepaView.setViewport(new Rectangle2D(0, 0, 180, 180));
            if (Game.moveLeft) sherepaView.setViewport(new Rectangle2D(180, 0, 180, 180));
            Main.gameLayout.getChildren().remove(punchView);  //  то проверка на состояние игрока,
            attack = false;  //  удаление картинки удара и переменная, отвечающая за атаку, становится false
        }
    }

    public void punch() {  //  Атака в ближнем бою
        for (int i = 0; i < Math.abs(Game.speedX); i++) {  //  Проходит по каждому пикселю при скорости по х
            for (Enemy enemy : Enemy.enemys) {  //  Проходит по всем врагам
                if ((sherepaView.getBoundsInParent().intersects(enemy.enemyView.getBoundsInParent()) && attack) ||
                        (punchView != null && punchView.getBoundsInParent().intersects(enemy.enemyView.getBoundsInParent())
                                && attack)) {  //  Коллизия игрока или удара с противником
                    Enemy.enemys.remove(enemy);  //  Если возникла коллизия при ударе, то враг удаляется из списка
                    Main.gameLayout.getChildren().remove(enemy.enemyView);  //  с врагами и удаляется с экрана
                    return;
                }
            }
        }
    }

    public void punchCheck() {
        if (Game.moveRight && attack) {  //  Обработка картинки во время удара и игрока, смотрящего вправо
            punchView.setX(sherepaView.getX() + width);
            punchView.setViewport(new Rectangle2D(0, 0, 130, 130));
        }

        if (Game.moveLeft && attack) {  //  Обработка картинки во время удара и игрока, смотрящего влево
            punchView.setX(sherepaView.getX() - 36);
            punchView.setViewport(new Rectangle2D(130, 0, 130, 130));
        }
    }

    public void beat() {
        ImageView beatView = null;  //  Создаётся пустая картинка, с последующим
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
        if (Game.moveRight) Game.create((int)sherepaView.getX() + width, (int)sherepaView.getY()
                + 18, beatWidth * 2, 24, beatView, Main.gameLayout);
        else Game.create((int)sherepaView.getX() - beatWidth * 2, (int)sherepaView.getY()
                + 18, beatWidth * 2, 28, beatView, Main.gameLayout);
        Random random = new Random();  //  Генерируется случайное число для выбора случайного цвета
        Beat beat = new Beat(beatView, random.nextInt(7), Game.moveRight);
        if (Game.moveRight) beat.beatView.setViewport(new Rectangle2D(beat.color * beatWidth * 10,
                beatWidthRandom * 12 * 10, beatWidth * 10, 120));
        if (Game.moveLeft) beat.beatView.setViewport(new Rectangle2D(beat.color * beatWidth * 10,
                beatWidthRandom * 12 * 10 + 360, beatWidth * 10, 120));
        Beat.beats.add(beat);  //  Новая картинка звука добавляется в лист со звуками и таймер перезарядки
        Beat.reloadTimer = 30;  //  выставляется на 30, то есть на 0,5 секунды
    }

    public void isDied() {  //  Проверка на смерть персонажа
        if (sherepaView.getY() > 1280) health = 0;
        if (health == 0) {
            died = true;
        }
    }
}