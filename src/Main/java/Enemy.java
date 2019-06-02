import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Enemy {

    ImageView enemyView;
    int x = 1;

    public static ArrayList<Enemy> enemys = new ArrayList<>();

    public Enemy(ImageView enemyView) {
        this.enemyView = enemyView;
    }

    public void moveX() {  //  Реализация движения врага по оси х
        boolean movingRight = x > 0;

        for (int i = 0; i < Math.abs(x); i++) {
            for (ImageView platform : BlockProperties.platforms) {
                if (enemyView.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (enemyView.getX() + 40 == platform.getX()) {
                            enemyView.setX(enemyView.getX() - 1);
                            x = -1;
                        }
                    }
                    else {
                        if (enemyView.getX() == platform.getX() + 60) {
                            enemyView.setX(enemyView.getX() + 1);
                            x = 1;
                        }
                    }
                }
            }
            enemyView.setX(enemyView.getX() + (movingRight ? 1 : -1));
        }
    }
}