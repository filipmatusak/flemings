/** Trieda reprezentujuca mapu sveta FlemingZ. Mapa sa sklada z matice policok typov popisanych v AllSquares. Pamata si
 * konfiguraciu pre level spustany z nej - pocet robotov kazdeho typu, ktory sa do hry moze pridat a ciel hry - pocet
 * robotov, ktorych treba zachranit. */

package engine;

import javafx.scene.paint.Color;
import squares.EntrySquare;
import squares.Square;

import java.util.ArrayList;

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
    static Integer maxRobotLimit = 100;     /** maximalny pocet robotov pridanych do hry pre kazdy typ*/

    public Map(Main root, int height, int width){
        this.root = root;
        this.height = height;
        this.width = width;
        map = new Square[height][width];
    }

    public Square[][] getMap(){ return map;}
    public Integer getSquareSize(){ return squareSize;}

    /** Vrati X-ovu suradnicu vstupneho policka v mape alebo -1, ak sa tam take policko nenachadza */
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

    /** Vrati Y-ovu suradnicu vstupneho policka v mape alebo -1, ak sa tam take policko nenachadza*/
    public Integer getEntryY() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] instanceof EntrySquare) {
                    return j;
                }
            }
        }
        return -1;
    }

    /** Funkcia vrati klon povodnej mapy - ukazuje na povodnu mapu pred hrou (pred menenim typov policok a akcii robotov) */
    @Override
    public Map clone(){
        Map clone = new Map(this.root, this.height, this.width);
        clone.map = new Square[height][width];
        clone.limits = this.limits;
        clone.target = this.target;
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
    public static Integer getMaxRobotLimit(){ return maxRobotLimit; }
}
