package squares;

import javafx.scene.paint.Color;
import old.RobotException;
import robots.Robot;

/** Tato trieda reprezentuje koncove policko, co je specialny typ
 * prazdneho policka, ktore ale zachyti vsetkych prichadzajucich a padajucich
 * robotov a vyhlasi si ich za uspesne skoncenych. */
public class ExitSquare extends EmptySquare {


    public ExitSquare() {
        this.setColor(Color.MEDIUMSEAGREEN);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
    }
    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        if (myRobot != null) {
//            throw new RobotException("There should not be a robot in exit");
        }
        otherRobot.finished();
        animationMove(otherRobot, false);
        otherRobot.moveTo(this);


        return true;
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height, Integer downMax) {
        if (myRobot != null) {
            throw new RobotException("There should not be a robot in exit");
        }
        otherRobot.finished();
        animationFalling(otherRobot, height, downMax);

        return true;
    }

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    @Override
    public String toString() {
        if (myRobot != null) {
            return myRobot.toString();
        } else {
            return "*";
        }
    }

    /** Pri vybuchnuti policka zabijeme robota,
     * ktory je na nom
     */
    @Override
    public boolean exploding() {
        return false;
    }
}