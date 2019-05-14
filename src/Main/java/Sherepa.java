import javafx.scene.Scene;
import javafx.scene.image.ImageView;


public class Sherepa {

    private boolean up;
    private boolean right;
    private boolean left;

    Scene gameScene;
    ImageView sherepa;

    public Sherepa(Scene gameScene, ImageView sherepa) {
        this.gameScene = gameScene;
        this.sherepa = sherepa;
    }

    public void start() {
        sherepa.setX(100);
        sherepa.setY(100);

    }




}
