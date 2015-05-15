package squares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import old.Direction;
import robots.Robot;
import old.RobotException;
import old.RobotHolder;

/** Tato trieda reprezentuje prazdne policko, na ktorom moze
 * a nemusi prave byt nejaky robot. Musi sa vyrovnat s tahmi tohto robota
 * a ich dosledkami, ako napr. padanie robotov.
 */
public class EmptySquare extends Square implements RobotHolder {

    /** Robot pritomny na policku, alebo null, ak tam ziaden nie je. */
    Robot myRobot;

    public EmptySquare(){
        super();
        this.setColor(Color.BROWN);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
    }
    /** Z policka odisiel robot, uprav si podla toho stav a vykonaj
     * dalsie nasledky tejto zmeny, napr. padajuci roboti z horneho
     * suseda.  Tuto metodu spravidla vola robot. */
    @Override
    public void deregisterRobot() {
        // ak nemame robota, hodime vynimku, ako v nadtriede Square */
        if (myRobot == null) {
            throw new RobotException("No robot to deregister");
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
    public boolean receiveRobot(Robot otherRobot) {
        // ak uz mame robota, vratime false
        if (myRobot != null) {
            return false;
        } else {
            // ak nemame robota, tak ho posunime na aktualne policko,
            // skusime ci padne nizsie a vratime true.
            otherRobot.moveTo(this);
            down.fallingRobot(otherRobot, 1);
            return true;
        }
    }

    @Override
    public boolean fallingRobot(Robot otherRobot, int height) {
        // ak mame na policku robota
        if (myRobot != null) {
            if (height > 1) {  // ukoncime pad ineho robota
                otherRobot.fell(height - 1);
            }
            myRobot.killed();
            return false;
        } else {
            // toto policko je teraz prazdne, prijmeme noveho robota
            otherRobot.moveTo(this);
            // skusime, ci moze padnut este nizsie (zvysime height)
            boolean success = down.fallingRobot(otherRobot, height + 1);
            return true;
        }
    }

    @Override
    public boolean actionMove(Direction direction) {
        if (myRobot == null) {
            throw new RobotException("Cannot move null robot right");
        }
        if (direction == Direction.RIGHT) {
            return right.receiveRobot(myRobot);
        } else {
            return left.receiveRobot(myRobot);
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

    public javafx.scene.shape.Rectangle print(){
        Rectangle r = new Rectangle(size,size, getColor());
        r.setY(column * size);
        r.setX(row * size);
        System.out.println(row + " " + column);
        return r;
    }
}
