package squares;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import old.Direction;
import old.RobotException;
import robots.Robot;
import sample.GameTimeLine;

public class GlueSquare extends EmptySquare {

    /** Na toto policko zhora pada robot, pricom zatial ide o pad z
     * vysky height. Ak uz mame robota, posleme mu, ze ho zabil pad
     * ineho robota, co rekurzivne spadne padajuceho robota na toto
     * policko.  Z tejto funkcie vsak vratime false, lebo sa nepadalo
     * rovno dalej.  Ked je policko prazdne, prilepime noveho robota.
     */
    public GlueSquare(){
        this.setColor(Color.GREENYELLOW);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height, Integer downMax) {
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
            animationFalling(otherRobot, 1, 0);
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
        //     world.timeLine.play();
        myRobot.endMoving();
        world.timeLine.endAct(myRobot);
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
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        // ak uz mame robota, vratime false
        if (myRobot != null) {
            //     world.timeLine.play();
            otherRobot.endMoving();
            world.timeLine.endAct(otherRobot);
            return false;
        } else {
            otherRobot.moveTo(this);
            if(move) animationMove(otherRobot);
            return true;
        }
    }

    /**
     * animacia pohybu
     * */
    public void animationMove(Robot otherRobot){
        //ktorym smerom sa ideme hybat
        final Double x; if(otherRobot.getDirection() == Direction.LEFT) x = -1.0; else x = 1.0;
        //postupny pohyb robota;
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(GameTimeLine.getPeriod() / this.size), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.setX(otherRobot.getX()+x);
            }
        }));
        tl.setCycleCount(this.size);
        tl.play();
        //na konci robota skusi nechat spadnut, ak nejde, uvolni tah
        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //      otherRobot.moveTo(thiz);
                    otherRobot.endMoving();
                    thiz.world.timeLine.endAct(otherRobot);

            }
        });
    }


}
