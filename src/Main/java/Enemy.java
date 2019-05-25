import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Enemy {
    ImageView enemyView;
    int x = 1;
    int y = 1;

    public static ArrayList<Enemy> enemys = new ArrayList<>();

    public Enemy(javafx.scene.image.ImageView enemyView) {
        this.enemyView = enemyView;
    }

    public void moveX() {  //  Реализация движения персонажа по оси х
        boolean movingRight = x > 0;

        for (int i = 0; i < Math.abs(x); i++) {  //  Проходит по каждому пикселю при заданной скорости
            for (ImageView platform : BlockProperties.platforms) {  //  Проходит по всем платформам
                if (enemyView.getBoundsInParent().intersects(platform.getBoundsInParent())) {  //  Обнаружение коллизии
                    if (movingRight) {
                        if (enemyView.getX() + 40 == platform.getX()) {//  Если правая часть игрока
                            enemyView.setX(enemyView.getX() - 1);
                            x = -1;//  вступает в коллизию с левой частью платформы,
                            //  то игрок переносится на 1 пиксель влево для того, чтобы правильно работала
                        }  // вертикальная коллизия в следующем методе и цикл завершается
                    }
                    else {
                        if (enemyView.getX() == platform.getX() + 60) {  //  То же самое, только для левой части
                            enemyView.setX(enemyView.getX() + 1);
                            x = 1;//  игрока и правой части платформы

                        }
                    }
                }
            }
            enemyView.setX(enemyView.getX() + (movingRight ? 1 : -1));  //  Если переменная movingRight = true, то
        }  //  передвигаем персонажа на 1 пиксель вправо иначе на 1 пиксель влево. Это служит для того, чтобы правильно
    }  //  обработать коллизию при совпадении правой и левой грани игрока и платформы
}
