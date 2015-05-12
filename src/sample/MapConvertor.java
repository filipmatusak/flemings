package sample;

import javafx.scene.paint.Paint;
import squares.Square;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class MapConvertor {

    Main root;
    public MapConvertor(Main root){
        this.root = root;
    }

    /**ulozi mapu do suboru*/
    public void fromMapEditor(ColoredRectangle[][] map, File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.println(map.length + " " + map[0].length);
        for (ColoredRectangle[] i : map) {
            for (ColoredRectangle j : i) {
                out.print(getSquare(j.getColor()).toString());
            }
            out.println();
        }
        out.close();
    }

    /**pripravy mapu zo suboru pre editor*/
    public ColoredRectangle[][] toMapEditor(File file) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(file));
        int r, c;
        r = in.nextInt();
        c = in.nextInt();
        ColoredRectangle[][] map = new ColoredRectangle[r][c];
        String s;
        s = in.nextLine();
        for(int i = 0; i < r; i++){
            s = in.nextLine();
            for(int j = 0; j < c; j++){
                map[i][j] = new ColoredRectangle(j*root.map.squareSize, i*root.map.squareSize,
                        root.map.squareSize,root.map.squareSize);
                map[i][j].setColor(getSquare(s.charAt(j)).getColor());
            }
        }
        return map;
    }

    /**podla farby zisti typ stvorca*/
    Square getSquare(Paint c){
       for(Square i: new AllSquares().getTypes()){
            if(i.getColor() == c) return i;
        }
        return null;
    }

    /**podla znakovej reprezentacie stvorca zisti typ stvorca*/
    Square getSquare(Character c){
        for(Square i: new AllSquares().getTypes()){
            if(Objects.equals(i.toString(), c.toString())) return i;
        }
        return null;
    }
}
