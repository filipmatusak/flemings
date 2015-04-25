package sample;

import javafx.scene.shape.Rectangle;

public class MapEditor {
    Main root;
    Rectangle[][] map;
    int height, width;

    public MapEditor(Main root){
        this.root = root;
    }

    public void run(){
        height = root.map.height;
        width = root.map.width;
        map = new Rectangle[height][width];


    }
}
