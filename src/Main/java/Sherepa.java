import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Sherepa {

    ImageView sherepaView;
    static int height = 91;
    static int width = 64;
    static boolean flick = false;
    static boolean attack = false;
    static int flickTimer;
    static int attackTimer;

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
        }  //  только для вертикального передвижения персонажа
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
                    flick = true;  //  Переменная мерцания персонажа становится true
                    flickTimer = 180;  //  Таймер мерцания ставится на 180, то есть на 3 секунды
                    Game.speedY = -15;  //  Скорость игрока по у становится -15 для того, чтобы персонаж подпрыгнул при
                    this.moveY(Game.speedY);  //  получении урона, затем прыжок персонажа с заданной скоростью
                }
            }
        }
    }

    public void punch() {  //  Атака в ближнем бою
        for (int i = 0; i < Math.abs(Game.speedX); i++) {  //  Проходит по каждому пикселю при скорости по х
            for (Enemy enemy : Enemy.enemys) {  //  Проходит по всем врагам
                if (sherepaView.getBoundsInParent().intersects(enemy.enemyView.getBoundsInParent()) && attack) {
                    Enemy.enemys.remove(enemy);  //  Если возникла коллизия при ударе, то враг удаляется из списка
                    Main.gameLayout.getChildren().remove(enemy.enemyView);  //  с врагами и удаляется с экрана
                    return;
                }
            }
        }
    }
}
