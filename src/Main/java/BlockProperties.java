import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BlockProperties extends Pane {

    public static ArrayList<ImageView> platforms = new ArrayList<>();

    public static void createBlocks() throws FileNotFoundException {
        for (int i = 0; i < LevelStructure.LEVEL1.length; i++) {
            String line = LevelStructure.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        ImageView UpLayer = new ImageView(new Image(new FileInputStream("./images/UpLayer.png")));
                        ImageView UpLayerPlatform = Game.create(j * 60, i * 60, 60, 60, UpLayer, Main.gameLayout);
                        platforms.add(UpLayerPlatform);
                        break;
                    case '2':
                        ImageView downLayer = new ImageView(new Image(new FileInputStream("./images/DownLayer.png")));
                        ImageView downLayerPlatform = Game.create(j * 60, i * 60, 60, 60, downLayer, Main.gameLayout);
                        platforms.add(downLayerPlatform);
                        break;
                    case '3':
                        ImageView Block = new ImageView(new Image(new FileInputStream("./images/Block.png")));
                        ImageView BlockPlatform = Game.create(j * 60, i * 60, 60, 60, Block, Main.gameLayout);
                        platforms.add(BlockPlatform);
                        break;
                    case '4':
                        ImageView Dirt = new ImageView(new Image(new FileInputStream("./images/Dirt.png")));
                        ImageView DirtPlatform = Game.create(j * 60, i * 60, 60, 60, Dirt, Main.gameLayout);
                        platforms.add(DirtPlatform);
                        break;
                    case '5':
                        ImageView DirtStone = new ImageView(new Image(new FileInputStream("./images/DirtStone.png")));
                        ImageView DirtStonePlatform = Game.create(j * 60, i * 60, 60, 60, DirtStone, Main.gameLayout);
                        platforms.add(DirtStonePlatform);
                        break;
                    case '6':
                        ImageView Flag = new ImageView(new Image(new FileInputStream("./images/Flag.png")));
                        ImageView FlagPlatform = Game.create(j * 60, i * 60, 60, 60, Flag, Main.gameLayout);
                        platforms.add(FlagPlatform);
                        break;
                    case '9':
                        ImageView mario = new ImageView(new Image(new FileInputStream("./images/Mario.png")));
                        Enemy enemy = new Enemy(Game.create(j * 60, i * 60 + 19, 40, 40, mario, Main.gameLayout));
                        Enemy.enemys.add(enemy);
                        break;
                }
            }
        }
    }
}
