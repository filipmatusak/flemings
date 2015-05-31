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

    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        // ak uz mame robota, vratime false

        if (myRobot != null) {
            otherRobot.endMoving();
            return false;
        } else {
        //    down.fallingRobot(myRobot, 1, Integer.MAX_VALUE);
            otherRobot.moveTo(this);
            down.fallingRobot(myRobot,0,Integer.MAX_VALUE);
        //    down.fallingRobot(myRobot, 1, Integer.MAX_VALUE);
            if(move) animationMove(otherRobot, true);
            return true;
        }
    }
/*
    @Override
    public boolean actionMove(Direction direction) {
        if (myRobot == null) {
            //     return false;
            throw new RobotException("Cannot move null robot right");
        }

        if(!super.fallingRobot(myRobot,0,Integer.MAX_VALUE)) return super.actionMove(direction);
        return true;
    }*/

}
