package sample;

import javafx.scene.paint.Color;
import squares.EntrySquare;
import squares.Square;


/**
 * trieda reprezentujuca mapu
 */

public class Map {
    Main root;
    Square[][] map;

    /**Rozmery mapy*/
    Integer height = 20;
    Integer width = 20;
    /**Rozmery jedneho stvorceka na mape*/
    Integer squareSize = 20;


    public Map(Main root, int height, int width){
        this.root = root;
        this.height = height;
        this.width = width;
        map = new Square[height][width];
    }

    public Square[][] getMap(){ return map;}
    public Integer getSquareSize(){ return squareSize;}

    /**
     * Vrati X-ovu suradnicu vstupneho policka v mape
     * @return
     */
    public int getEntryX(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                if(map[i][j] instanceof EntrySquare){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Vrati Y-ovu suradnicu vstupneho policka v mape
     * @return
     */
    public int getEntryY(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                if(map[i][j] instanceof EntrySquare){
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * Mapa - verzia pre hru (bez listenerov)
     * @return
     */
    public ColoredRectangle[][] getMapView(){
        ColoredRectangle[][] mapView = new ColoredRectangle[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ColoredRectangle r = new ColoredRectangle(j*squareSize, i*squareSize, squareSize,squareSize);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.05);
                r.setColor(map[i][j].getColor());
                mapView[i][j] = r;
            }
        }
        return mapView;
    }
}
