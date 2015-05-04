package sample;

import javafx.scene.shape.Rectangle;

/**
 * trieda reprezentujuca mapu
 */

public class Map {
    Main root;
    Rectangle[][] map;
    Integer height, width, squareSize = 20;


    public Map(Main root, int height, int width){
        this.root = root;
        this.height = height;
        this.width = width;
    }
}
