import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class Sherepa {

    ImageView sherepaView;

    public Sherepa(ImageView sherepaView) {
        this.sherepaView = sherepaView;
    }

    public void moveX(double x) {  //  Реализация движения персонажа
        Platform.runLater(() -> sherepaView.setX(sherepaView.getX() + x));
    }

    public void moveY(double y) {  //  Реализация движения персонажа
        Platform.runLater(() -> sherepaView.setY(sherepaView.getY() + y));
    }
}
