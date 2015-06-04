/** Trieda reprezentuje zoznam vsetkych policok v hre*/

package engine;

import squares.*;

import java.util.ArrayList;

/**
 * Vrati zoznam policok - instanciu policka kazdeho typu
 */

public class AllSquares {
    public static ArrayList<Square> getTypes(){
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
