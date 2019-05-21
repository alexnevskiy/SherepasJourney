import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;


public class Sherepa extends Pane {
//    Image sherepaImage;
//
//    {
//        try {
//            sherepaImage = new Image(new FileInputStream("./images/Shrek.png"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
    ImageView sherepaView;
    Node sherepaViewToNode;
    boolean up;
    boolean right;
    boolean left;
    public Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    static final int BLOCK_SIZE = 45;
    static final int SHEREPA_SIZE = 40;
    Scene gameScene;
    int offsetX = 0;
    int offsetY = 0;
    int width = 192;
    int height = 192;
    Pane gameLayout;


    public Sherepa(ImageView sherepaView) {
        this.sherepaView = sherepaView;
    }

    public void move(double x) {  //  Реализация движения персонажа
        Platform.runLater(() -> sherepaView.setLayoutX(sherepaView.getLayoutX() + x));
    }

    public void down(double y) {  //  Реализация движения персонажа
        Platform.runLater(() -> sherepaView.setLayoutY(sherepaView.getLayoutY() + y));
    }

//    public void moveX(int value){
//        boolean movingRight = value > 0;
//        //for(int i = 0; i < Math.abs(value); i++) {
//            for (Node platform : BlockProperties.platforms) {
//                if(this.sherepaViewToNode.getBoundsInParent().intersects(platform.getBoundsInParent())) {
//                    if (movingRight) {
//                        if (this.sherepaViewToNode.getTranslateX() + SHEREPA_SIZE == platform.getTranslateX()){
//                            this.sherepaViewToNode.setTranslateX(this.sherepaViewToNode.getTranslateX() - 1);
//                        }
//                    } else {
//                        if (this.sherepaViewToNode.getTranslateX() == platform.getTranslateX() + BLOCK_SIZE) {
//                            this.sherepaViewToNode.setTranslateX(this.sherepaViewToNode.getTranslateX() + 1);
//                        }
//                    }
//                }
//            }
//            this.sherepaViewToNode.setTranslateX(this.sherepaViewToNode.getTranslateX() + (movingRight ? 1 : -1));
//       // }
//    }
//    public void moveY(int value){
//        boolean movingDown = value > 0;
//        for(int i = 0; i < Math.abs(value); i++){
//            for(Node platform : BlockProperties.platforms){
//                if(getBoundsInParent().intersects(platform.getBoundsInParent())){
//                    if(movingDown){
//                        if(this.getTranslateY() + SHEREPA_SIZE == platform.getTranslateY()){
//                            this.setTranslateY(this.getTranslateY() - 1);
//                            canJump = true;
//                        }
//                    }
//                    else{
//                        if(this.getTranslateY() == platform.getTranslateY()+ BLOCK_SIZE){
//                            this.setTranslateY(this.getTranslateY() + 1);
//                            playerVelocity = new Point2D(0,10);
//                        }
//                    }
//                }
//            }
//            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
////            if(this.getTranslateY()>640){
////                this.setTranslateX(0);
////                this.setTranslateY(400);
////                Main.gameLayout.setLayoutX(0);
////            }
//        }
//    }
//
//    public void jumpPlayer(){
//        if(canJump){
//            playerVelocity = playerVelocity.add(0,-30);
//            canJump = false;
//        }
//    }

//    public void start() {
//
//        gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
//            KeyCode keycode = key.getCode();
//            if (keycode.equals(KeyCode.W)) up = true;
//            if (keycode.equals(KeyCode.A)) left = true;
//            if (keycode.equals(KeyCode.D)) right = true;
//        });
//
//        gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
//            KeyCode keycode = key.getCode();
//            if (keycode.equals(KeyCode.W)) up = false;
//            if (keycode.equals(KeyCode.A)) left = false;
//            if (keycode.equals(KeyCode.D)) right = false;
//        });
//    }

    public void start() {

        Main.gameScene.setOnKeyPressed(key -> {  //  Если кнопка нажата, то переменная становится true
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = true;
            if (keycode.equals(KeyCode.A)) left = true;
            if (keycode.equals(KeyCode.D)) right = true;
        });

        Main.gameScene.setOnKeyReleased(key -> {  //  Если кнопка отжата, то переменная становится false
            KeyCode keycode = key.getCode();
            if (keycode.equals(KeyCode.W)) up = false;
            if (keycode.equals(KeyCode.A)) left = false;
            if (keycode.equals(KeyCode.D)) right = false;

//            Timer timer = new Timer();
//
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    Sherepa player = new Sherepa();
//                    if(up && player.getTranslateY() >= 5){
//                        player.jumpPlayer();
//                    }
//                    if(left && player.getTranslateX() >= 5){
//                        player.setScaleX(-1);
//                        //player.animation.play();
//                        player.moveX(-5);
//                    }
//                    if(right && player.getTranslateX()+40 <= LevelStructure.levelWidth - 5){
//                        player.setScaleX(1);
//                        //player.animation.play();
//                        player.moveX(5);
//                    }
//                    if(player.playerVelocity.getY() < 10){
//                        player.playerVelocity = player.playerVelocity.add(0,1);
//                    }
//                    player.moveY((int)player.playerVelocity.getY());
//                }
//            };


            Timer timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Sherepa player = new Sherepa(sherepaView);
                    player.setTranslateX(100);
                    player.setTranslateY(100);
                    if(up){
                        //player.jumpPlayer();
                    }
                    if(left && player.sherepaView.getTranslateX() >= 5){
                        //player.setScaleX(-1);
                        //player.animation.play();
                        //player.moveX(-5);
                    }
                    if(right && player.getTranslateX() + 40 <= LevelStructure.levelWidth - 5){
                        //player.setScaleX(1);
                        //player.animation.play();
                        //player.moveX(5);
                    }
                    if(player.playerVelocity.getY() < 10){
                        player.playerVelocity = player.playerVelocity.add(0,1);
                    }
                    //player.moveY((int)player.playerVelocity.getY());
                }
            };

            timer.schedule(timerTask, 0, 5);
        });
    }
}
