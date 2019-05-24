import javafx.scene.image.ImageView;

public class Sherepa {

    ImageView sherepaView;

    public Sherepa(ImageView sherepaView) {
        this.sherepaView = sherepaView;
    }

    public void moveX(double x) {  //  Реализация движения персонажа по оси х
        boolean movingRight = x > 0;

        for (int i = 0; i < Math.abs(x); i++) {  //  Проходит по каждому пикселю при заданной скорости
            for (ImageView platform : BlockProperties.platforms) {  //  Проходит по всем платформам
                if (sherepaView.getBoundsInParent().intersects(platform.getBoundsInParent())) {  //  Обнаружение коллизии
                    if (movingRight) {
                        if (sherepaView.getX() + Game.playerWight == platform.getX()) {//  Если правая часть игрока
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
                        if (sherepaView.getY() + Game.playerHeight == platform.getY()) {  //  Если нижняя часть игрока
                            sherepaView.setY(sherepaView.getY() - 1);  //  вступает в коллизию с верхней частью
                            Game.canJump = true;  //  платформы, то игрок переносится на 1 пиксель вверх для того, чтобы
                            return;  //  правильно работала горизонтальная коллизия в прошлом методе, переменная,
                        }  //  отвечающая за возможность совершить прыжок становится true и цикл завершается
                    }
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
}
