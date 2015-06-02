package squares;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import old.Direction;
import old.RobotException;
import old.RobotHolder;
import robots.Robot;
import sample.GameTimeLine;

/** Tato trieda reprezentuje prazdne policko, na ktorom moze
 * a nemusi prave byt nejaky robot. Musi sa vyrovnat s tahmi tohto robota
 * a ich dosledkami, ako napr. padanie robotov.
 */
public class EmptySquare extends RobotHolder {

    /** Robot pritomny na policku, alebo null, ak tam ziaden nie je. */
    Robot myRobot;
    RobotHolder thiz;
    //upravuju rychlost animacii
     Double fallingConst = 40.0;
     Double movingConst = 3.0;

    public EmptySquare(){
        super();
        this.setColor(Color.BEIGE);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
        thiz = this;
    }

    public boolean hasRobot(){return myRobot != null;}
    /** Z policka odisiel robot, uprav si podla toho stav a vykonaj
     * dalsie nasledky tejto zmeny, napr. padajuci roboti z horneho
     * suseda.  Tuto metodu spravidla vola robot. */
    @Override
    public void deregisterRobot() {
        // ak nemame robota, hodime vynimku, ako v nadtriede Square */
        if (myRobot == null) {
            return;
          //  throw new RobotException("No robot to deregister");
        }
        // zrusime si robota
        myRobot = null;
        // u horneho suseda sa musia vyrovnat s nasledkami vyprazdneneho policka
        up.emptiedBelow();
    }

    /** Na policko prisiel robot, Uprav si podla toho stav a vykonaj
     * pripadne nasledky tejto zmeny.  Tuto metodu spravidla vola
     * robot. */
    @Override
    public void registerRobot(Robot otherRobot) {
        if (myRobot != null) {
//            throw new RobotException("Two robots cannot be in the same square");
        }
        myRobot = otherRobot;
    }

    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        // ak uz mame robota, vratime false

        if (myRobot != null) {
            otherRobot.endMoving();
            return false;
        } else {
            otherRobot.moveTo(this);
            if(move) animationMove(otherRobot, true);
            return true;
        }
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height, Integer downMax) {
        // ak mame na policku robota
        if(downMax == 0) return false;
        if(myRobot == null || myRobot.isFalling() || myRobot==otherRobot){
            // toto policko je teraz prazdne, prijmeme noveho robota
            if(down != null && down instanceof EmptySquare && ((EmptySquare) down).myRobot != null){
                ((EmptySquare) down).myRobot.killed();
            }
            animationFalling(otherRobot, height, downMax);
            return true;
        }
        else {
            if (height > 1) {  // ukoncime pad ineho robota
                otherRobot.fell(height - 1);
            }
            if(!myRobot.isFalling()){
                otherRobot.endMoving();
                myRobot.killed();
            }
            return false;
        }
    }

    @Override
    public boolean actionMove(Direction direction) {
        if (myRobot == null) {
            throw new RobotException("Cannot move null robot right");
        }

        if (direction == Direction.RIGHT) {
            return right.receiveRobot(myRobot, true);
        } else {
            return left.receiveRobot(myRobot, true);
        }
    }

    @Override
    protected void emptiedBelow() {
        if (myRobot != null && myRobot.isActive()) {
            down.fallingRobot(myRobot, 1, Integer.MAX_VALUE);
        }
    }

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    @Override
    public String toString() {
        // ak mame robota, vypiseme robota, inak vypiseme seba
        if (myRobot != null) {
            return myRobot.toString();
        } else {
            return ".";
        }
    }

    /** Pri vybuchnuti policka zabijeme robota,
     * ktory je na nom
     */
    @Override
    public boolean exploding() {
        if(myRobot != null) myRobot.killed();
        Square b = new EmptySquare();
        this.world.newSquare(b, row, column);
        return true;
    }

    /** Aktualny robot z tohto policka chce kopat.  Bud mu akciu
     * povoli, vykona ju a vsetky jej nasledky a vrati true, alebo
     * akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
    @Override
    public boolean actionDigging(Direction direction) {
        if (myRobot == null) {
            throw new RobotException("Null robot cannot dig");
        }
        if (direction == Direction.RIGHT) return right.digging(this);
        return left.digging(this);
    }

    /** Aktualny robot z tohto policka chce vybuchnut.  Bud mu akciu
     * povoli, vykona ju a vsetky jej nasledky a vrati true, alebo
     * akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
    @Override
    public boolean actionExploding() {
        this.exploding();
        this.up.exploding();
        this.right.exploding();
        this.down.exploding();
        this.left.exploding();
        return true;
    }

    /**
     * animacia pohybu
     * */
    public void animationMove(Robot otherRobot, Boolean canFall){
        otherRobot.setMoving();
        //ktorym smerom sa ideme hybat
        final Double x; if(otherRobot.getDirection() == Direction.LEFT) x = -1.0; else x = 1.0;
        //postupny pohyb robota;
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(GameTimeLine.getPeriod()/movingConst / this.size*2),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.setX(otherRobot.getX()+x*2);
            }
        }));
        tl.setCycleCount(this.size/2);

        //na konci robota skusi nechat spadnut, ak nejde, uvolni tah
        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.moveTo(thiz);
                otherRobot.endMoving();
                if (canFall && !down.fallingRobot(otherRobot, 1, Integer.MAX_VALUE)) {
                }
            }
        });
        tl.play();
    }

    /**
     * animacia padania
     */
    public void animationFalling(Robot otherRobot, int height, Integer downMax){
        otherRobot.setFalling();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis((GameTimeLine.getPeriod())/fallingConst / this.size), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.setY(otherRobot.getY() + 1);
            }
        }));


        tl.setCycleCount(this.size);
        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // skusime, ci moze padnut este nizsie (zvysime height)
                otherRobot.moveTo(thiz);
                otherRobot.endMoving();
                if (downMax > 0) {
                    if (down != null) down.fallingRobot(otherRobot, height + 1, downMax - 1);
                    else{
                        dopad(otherRobot);
                    }
                }
            }
        });
        tl.play();
    }

    void dopad(Robot robot){
        robot.setFalling();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis((GameTimeLine.getPeriod())/fallingConst / this.size), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.setY(robot.getY() + 1);
            }
        }));
        tl.setCycleCount(this.size/2);
        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.killed();
            }
        });
        tl.play();
    }
}
