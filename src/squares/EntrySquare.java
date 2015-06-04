package squares;

import javafx.scene.paint.Color;
import robots.Robot;

/** Tato trieda reprezentuje vstupne policko, co je specialny typ
 * prazdneho policka. Od bezneho prazdneho policka sa lisi len tym,
 * ze sa inak vykresluje. */
public class EntrySquare extends EmptySquare {

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    public EntrySquare() {
        this.setColor(Color.PALEGREEN);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
        type = "Entry Square (one per game)";
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

    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        // ak uz mame robota, vratime false

        if (myRobot != null) {
            otherRobot.endMoving();
            return false;
        } else {
            otherRobot.moveTo(this);
            down.fallingRobot(myRobot, 0, Integer.MAX_VALUE);
            if (move) animationMove(otherRobot, true);
            return true;
        }
    }

}
