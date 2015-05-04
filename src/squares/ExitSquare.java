package squares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import robots.Robot;
import old.RobotException;

/** Tato trieda reprezentuje koncove policko, co je specialny typ
 * prazdneho policka, ktore ale zachyti vsetkych prichadzajucich a padajucich
 * robotov a vyhlasi si ich za uspesne skoncenych. */
public class ExitSquare extends EmptySquare {


    public ExitSquare(){
        this.setColor(Color.MEDIUMSEAGREEN);
    }
    @Override
    public boolean receiveRobot(Robot otherRobot) {
        if (myRobot != null) {
            throw new RobotException("There should not be a robot in exit");
        }
        otherRobot.moveTo(this);
        otherRobot.finished();
        return true;
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height) {
        if (myRobot != null) {
            throw new RobotException("There should not be a robot in exit");
        }
        otherRobot.moveTo(this);
        otherRobot.finished();
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

    public javafx.scene.shape.Rectangle print(){
        Rectangle r = new Rectangle(size,size,color);
        r.setY(column * size);
        r.setX(row * size);
        System.out.println(row + " " + column);
        return r;
    }
}