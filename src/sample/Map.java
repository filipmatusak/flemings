package sample;

import squares.Square;


/**
 * trieda reprezentujuca mapu
 */

public class Map {
    Main root;
    Square[][] map;

    Integer height = 20;
    Integer width = 20;
    Integer squareSize = 20;


    public Map(Main root, int height, int width){
        this.root = root;
        this.height = height;
        this.width = width;
    }

    public Square[][] getMap(){ return map;}
    public Integer getSquareSize(){ return squareSize;}
}
