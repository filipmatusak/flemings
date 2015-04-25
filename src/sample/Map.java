package sample;

import javafx.scene.shape.Rectangle;

public class Map {
    Main root;
    Rectangle[][] map;
    Integer height, width;

    public Map(Main root, int height, int width){
        this.root = root;
        this.height = height;
        this.width = width;
    }
}
