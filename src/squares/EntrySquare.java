package squares;

import javafx.scene.paint.Color;

/** Tato trieda reprezentuje vstupne policko, co je specialny typ
 * prazdneho policka. Od bezneho prazdneho policka sa lisi len tym,
 * ze sa inak vykresluje. */
public class EntrySquare extends EmptySquare {

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    public EntrySquare() {
        this.setColor(Color.SILVER);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
    }

     @Override
    public String toString() {
        if (myRobot != null) {
            return myRobot.toString();
        } else {
            return "#";
        }
    }

    /** Pri vybuchnuti policka zabijeme robota,
     * ktory je na nom
     */
    @Override
    public boolean exploding() {
        if(myRobot != null) myRobot.killed();
        return true;
    }

}
