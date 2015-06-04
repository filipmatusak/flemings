package squares;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import old.World;
import robots.Robot;

/** Abstraktná trieda reprezentujuca jeden štvorček hracej plochy.
 * Poskytuje základné implementácie jednotlivých metód, v ktorých sa
 * štvorček nijako nemení a nemôže na neho prísť robot.  Vo
 * všeobecnosti štvorčeky vykonávajú akcie robota, ktorý je na nich a
 * v komunikácii so susedmi sa vyrovnajú aj s ich následkami.
 */
public abstract class Square extends Rectangle {

    /**
     * Susedny stvorcek
     */
    protected Square up, down, left, right;
    /**
     * Suradnice tohto stvorceka
     */
    protected int row, column;
    /**
     * Referencia na svet
     */
    protected World world;

    public Color color;
    public int size;
    public String type;


    public void setSize(int s){
        size = s;
        this.setWidth(size);
        this.setHeight(size);
    }

    /** Nastav si novy stvorcek ako suseda */
    public void registerUp(Square newSquare) {
        up = newSquare;
    }

    /** Nastav si novy stvorcek ako suseda */
    public void registerDown(Square newSquare) {
        down = newSquare;
    }

    /** Nastav si novy stvorcek ako suseda */
    public void registerLeft(Square newSquare) {
        left = newSquare;
    }

    /** Nastav si novy stvorcek ako suseda */
    public void registerRight(Square newSquare) {
        right = newSquare;
    }

    /** Nastav si referenciu na svet a svoje suradnice v nom */
    public void registerWorld(World newWorld, int newRow, int newColumn) {
        world = newWorld;
        row = newRow;
        column = newColumn;
    }

    /** Na toto policko sa pokusa vojst robot. Pouziva sa, ked robot
     * prichadza inak ako padom zhora, t.j. tam spravi krok, alebo sa
     * objavi vo vchode.  Ak sa to da, vrati true, inak vrati false. V
     * tejto triede vzdy vracia false.  Metoda musi posunut samotneho
     * robota a vyrovnat sa so vsetkymi nasledkami tejto akcie. Po
     * skonceni metody uz moze byt robot na inom policku (napr
     * dosledkom padu), alebo aj mrtvy. Tuto metodu spravidla vola ine
     * policko, pripadne svet.
     * move: ci sa ma robot pri tom hybat, nechceme sa hybat napriklad,
     * ked tu robot dopadne, alebo je prave vytvoreny*/
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        otherRobot.endMoving();
        return false;
    }

    /** Na toto policko zhora pada robot, pricom zatial ide o pad z
     * vysky height. Ak sa sem da padnut, vrati true, inak vrati
     * false.  Ak vrati false, tiez oznami robotovi, ze padol o
     * height-1.  Metoda musi posunut samotneho robota a vyrovnat sa
     * so vsetkymi nasledkami tejto akcie. Po skonceni metody uz moze
     * byt robot na inom policku alebo aj mrtvy. Tuto metodu spravidla
     * vola ine policko (horny sused).  V tejto triede vzdy vracia
     * false. */
    public boolean fallingRobot(Robot otherRobot, int height, Integer downMax) {
        if(height>1) {
            otherRobot.fell(height-1);
        }
        otherRobot.endMoving();
        if( otherRobot.isKilled()) World.removeRobot(otherRobot);
        return false;
    }

    public boolean digging(Square cell){
        return false;
    }



    /** Policko pod tymto polickom sa vyprazdilo, treba sa vyrovnat s
     * dosledkami, napr. padajuci roboti. V tejto abstraktnej triede
     * netreba robit nic.  Tuto metodu spravidla vola ine policko
     * (spodny sused).
     */
    protected void emptiedBelow() {
    }

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. Abstraktna metoda, ktoru musia
     * dediacie triedy implementovat. */
    @Override
    public abstract String toString();

    /** Akcia vybuchnutia policka
     */
    public boolean exploding(){
        return false;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Color color) {
        super.setFill(color);
        this.color = color;
    }

    public String getType() {
        return type;
    }
}
