package squares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import old.Direction;
import robots.Robot;
import old.RobotException;
import old.RobotHolder;

public class GlueSquare extends EmptySquare implements RobotHolder {

    /** Na toto policko zhora pada robot, pricom zatial ide o pad z
     * vysky height. Ak uz mame robota, posleme mu, ze ho zabil pad
     * ineho robota, co rekurzivne spadne padajuceho robota na toto
     * policko.  Z tejto funkcie vsak vratime false, lebo sa nepadalo
     * rovno dalej.  Ked je policko prazdne, prilepime noveho robota.
     */
    public GlueSquare(){
        this.setColor(Color.GREENYELLOW);
    }
    @Override
    public boolean fallingRobot(Robot otherRobot, int height) {
        // ak mame na policku robota
        if (myRobot != null) {
            if (height > 1) {  // ukoncime pad ineho robota
                otherRobot.fell(height - 1);
            }
            myRobot.killed();         // zabijeme nasho robota
            return false;
        } else {
            // toto policko je teraz prazdne, prijmeme noveho robota
            otherRobot.moveTo(this);
            otherRobot.fell(height);
            return true;
        }
    }

    /** Ak sa chce robot z tohto policka pocunut, ma smolu
     */
    public boolean actionMove(Direction direction) {
        if (myRobot == null) {
            throw new RobotException("Cannot move null robot right");
        }
        return false;
    }

    /** Ak sa pod lepidlom vyprazdni policko,
     * nic sa nedeje
     */
    @Override
    protected void emptiedBelow() {}

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    @Override
    public String toString() {
        // ak mame robota, vypiseme robota, inak vypiseme seba
        if (myRobot != null) {
            return myRobot.toString();
        } else {
            return "G";
        }
    }

    /** Na toto policko sa pokusa vojst robot. Ak nemame robota,
    * dovolime mu to */
    @Override
    public boolean receiveRobot(Robot otherRobot) {
        // ak uz mame robota, vratime false
        if (myRobot != null) {
            return false;
        } else {
            // ak nemame robota, tak ho posunime na aktualne policko,
            otherRobot.moveTo(this);
            return true;
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