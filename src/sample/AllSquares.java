package sample;

import squares.*;

import java.util.ArrayList;

/**
 * ulozene vsetky typy policok
 */

public class AllSquares {
    public ArrayList<Square> getTypes(){
        ArrayList<Square> list = new ArrayList<>();
        list.add(new EmptySquare());
        list.add(new WallSquare());
        list.add(new GlueSquare());
        list.add(new SoilSquare());
        list.add(new EntrySquare());
        list.add(new ExitSquare());
        return list;
    }

}
