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

/** Tato trieda reprezentuje prazdne policko, na ktorom moze
 * a nemusi prave byt nejaky robot. Musi sa vyrovnat s tahmi tohto robota
 * a ich dosledkami, ako napr. padanie robotov.
 */
public class EmptySquare extends RobotHolder {

    /** Robot pritomny na policku, alebo null, ak tam ziaden nie je. */
    Robot myRobot;
    RobotHolder thiz;
    Boolean moveFinished;

    public EmptySquare(){
        super();
        this.setColor(Color.BROWN);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
        thiz = this;
    }
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
        // ak uz mame robota, vyhod vynimku
        if (myRobot != null) {
            throw new RobotException("Two robots cannot be in the same square");
        }
        // uloz noveho robota
        myRobot = otherRobot;
    }

    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        // ak uz mame robota, vratime false
        if (myRobot != null) {
            world.timeLine.play();
            return false;
        } else {
            otherRobot.moveTo(this);
            if(move) animationMove(otherRobot);
            return true;
        }
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height) {
        // ak mame na policku robota
        if(myRobot == null || myRobot==otherRobot){
            // toto policko je teraz prazdne, prijmeme noveho robota
         //   System.out.println("down");
            animationFalling(otherRobot, height);
            return true;
        }
        else {
            if (height > 1) {  // ukoncime pad ineho robota
                otherRobot.fell(height - 1);
            }
            myRobot.killed();
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
        if (myRobot != null) {
            down.fallingRobot(myRobot, 1);
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
    public void exploding() {
        if(myRobot != null) myRobot.killed();
    }

    /** Aktualny robot z tohto policka chce kopat.  Bud mu akciu
     * povoli, vykona ju a vsetky jej nasledky a vrati true, alebo
     * akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
    @Override
    public boolean actionDigging(Direction direction) {
        Square a, b;
        if(direction == Direction.LEFT) a = this.left;
        else a = this.right;
        b = new EmptySquare();
        if(!a.toString().equals("S")) return false;
        this.world.newSquare( b, a.row, a.column);
        b.up.emptiedBelow();
        return true;
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
    public void animationMove(Robot otherRobot){
        //ktorym smerom sa ideme hybat
        final Double x; if(otherRobot.getDirection() == Direction.LEFT) x = -1.0; else x = 1.0;
        //postupny pohyb robota;
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(100.0 / this.size), new EventHandler<ActionEvent>() {
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
                if(!down.fallingRobot(otherRobot, 1)) thiz.world.timeLine.play();
            }
        });
    }

    /**
     * animacia padania
     */
    public void animationFalling(Robot otherRobot, int height){
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(100.0 / this.size), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.setY(otherRobot.getY()+1);
            }
        }));
        tl.setCycleCount(this.size);
        tl.play();
        tl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                otherRobot.moveTo(thiz);
                // skusime, ci moze padnut este nizsie (zvysime height)
                if(!down.fallingRobot(otherRobot, height + 1)) thiz.world.timeLine.play();
            }
        });
    }
}
