package sample;

import javafx.scene.paint.Color;
import squares.EntrySquare;
import squares.Square;

import java.util.ArrayList;


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
    ArrayList<Integer> limits;
    Integer target;


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
    public Integer getEntryX(){
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
    public Integer getEntryY(){
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

    @Override
    public Map clone(){
        Map clone = new Map(this.root, this.height, this.width);
        clone.map = new Square[height][width];
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                clone.map[i][j] = this.map[i][j];
            }
        }
        return clone;
    }

    public void setTarget(Integer target){ this.target = target;}
    public Integer getTarget(){ return target;}
    public void setLimits(ArrayList<Integer> l){ this.limits = l;}
    public ArrayList<Integer> getLimits(){ return limits;}
}
