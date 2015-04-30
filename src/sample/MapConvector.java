package sample;

import javafx.scene.paint.Paint;
import old.Square;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MapConvector {

    Main root;
    public MapConvector(Main root){
        this.root = root;
    }

    public void formMapEditor(ColoredRectangle[][] map, File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.println(map.length + " " + map[0].length);
        for (ColoredRectangle[] i : map) {
            for (ColoredRectangle j : i) {
                out.print(getSquare(j.getColor()).toString());
            }
            out.println();
        }
    }

    Square getSquare(Paint c){
       for(Square i: new AllSquares().getTypes()){
            if(i.getColor() == c) return i;
        }
        return null;
    }
}
