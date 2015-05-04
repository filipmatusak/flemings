package squares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Tato trieda reprezentuje vstupne policko, co je specialny typ
 * prazdneho policka. Od bezneho prazdneho policka sa lisi len tym,
 * ze sa inak vykresluje. */
public class EntrySquare extends EmptySquare {

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    public EntrySquare(){
        this.setColor(Color.SILVER);
    }

     @Override
    public String toString() {
        if (myRobot != null) {
            return myRobot.toString();
        } else {
            return "#";
        }
    }

    public javafx.scene.shape.Rectangle print(){
        Rectangle r = new Rectangle(size,size,color);
        r.setY(column * size);
        r.setX(row * size);
        System.out.println(row + " " + column);
        return r;
    }
}
