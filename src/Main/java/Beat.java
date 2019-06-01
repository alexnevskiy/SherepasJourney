import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Beat {

    ImageView beatView;
    int color;
    boolean right;
    static int reloadTimer;

    public static ArrayList<Beat> beats = new ArrayList<>();

    public Beat(ImageView beatView, int random, boolean right) {
        this.beatView = beatView;
        this.color = random;
        this.right = right;
    }

    public void moveX(double x) {  //  Реализация движения звука по оси х
        boolean movingRight = x > 0;

        for (int i = 0; i < Math.abs(x); i++) {
            for (ImageView platform : BlockProperties.platforms) {  //  Проходит по всем платформам
                if (beatView.getBoundsInParent().intersects(platform.getBoundsInParent())) {  //  Если коллизия, то
                    beats.remove(beatView);  //  звук удаляется из листа со звуками и удаляется с экрана
                    Main.gameLayout.getChildren().remove(beatView);
                    return;
                }
            }
            for (Enemy enemy : Enemy.enemys) {
                if (beatView.getBoundsInParent().intersects(enemy.enemyView.getBoundsInParent())) {
                    Enemy.enemys.remove(enemy);  //  Если возникла коллизия при ударе звука, то враг удаляется из списка
                    Main.gameLayout.getChildren().remove(enemy.enemyView);  //  с врагами и удаляется с экрана
                    beats.remove(beatView);  //  Тоже самое, только для звука
                    Main.gameLayout.getChildren().remove(beatView);
                    return;
                }
            }
            if (beatView.getX() < -15 || beatView.getX() > Game.levelWidth + 15) {  //  Если звук уходит за границы
                beats.remove(beatView);  //  игрового окна
                Main.gameLayout.getChildren().remove(beatView);
                return;
            }
            beatView.setX(beatView.getX() + (movingRight ? 1 : -1));
        }
    }
}
