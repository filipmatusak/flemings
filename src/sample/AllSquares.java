package sample;

import old.*;

import java.util.ArrayList;

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
