package old;

import javafx.scene.layout.Pane;
import robots.Robot;
import sample.GameTimeLine;
import sample.Main;
import sample.Map;
import squares.EntrySquare;
import squares.Square;

import java.util.ArrayList;

/** Trieda reprezentujuca svet pozostavajuci zo stvorcekov a robotov.
 */
public class World {

    public Main root;
    /** 2D pole stvorcekov */
    public Square[][] squares;
    /** pocet riadkov a stlpcov v poli stvrocekov */
    protected int nRows, nCols;
    /** poloha vstupneho stvorceka */
    protected int entryRow, entryCol;
    /** zoznam robotov, ktore uz vosli do sveta
     * (niektori z nich mozu byt mrtvi, alebo ukonceni). */
    public ArrayList<Robot> robots;
    public GameTimeLine timeLine;
    protected EntrySquare entrySquare;
    Pane pane;
    int sizeOfSquare;
    Map map;

    /** Konstruktor, ktory dostane pole stvorcekov
     * a inicializuje hernu situaciu s 0 robotmi.
     */
    public World(Map map, int entryRow, int entryCol, Pane pane_, int s, Main root) {
        this.root = root;
        sizeOfSquare = s;
        this.map = map;
        pane = pane_;
        robots = new ArrayList<>(); // vytvorime prazdne pole robotov
        this.squares = map.getMap();            // ulozime si pole stvrcekov
        nRows = squares.length;          // zistime rozmery pola
        nCols = squares[0].length;
        entrySquare = (EntrySquare)squares[entryRow][entryCol];
        this.entryRow = entryRow;          // ulozime vstupny stvorcek
        this.entryCol = entryCol;
        registerSquares();        // kazdemu stvorceku oznamime svet a susedov
        setSize();
    }

    void setSize(){
        for(int i = 0; i < entryCol; i++){
            for(int j = 0; j < entryRow; j++){
                squares[j][i].setSize(sizeOfSquare);
            }
        }
    }

    public Square[][] getSquare() {
        return squares;
    }

    /** Vykona jeden tah hry, t.j. necha kazdeho aktivneho robota
     * spravit tah a vrati true, ak je aspon jeden aktivny robot. */
    public boolean move() {
        boolean wasMove = false;
        for (Robot robot : robots) {
            // zastavime casovac kym sa nevykona tah robota, vsetky akcie,
            // ktore hybu robotmi si ho musia opat spustit
        //    root.game.timeLine.pause();
            System.out.println(robot.getType());
            if (robot.isActive()) { // ak mame aktivneho robota
                robot.setMoving();
                System.out.println("Move of robot " + robot.getName());
                robot.move();    // zavolame tah
                wasMove = true;  // nasli sme aktivneho
                printSituation();         // vypis celu plochu
            } else if(robot.isMoving()) {
                wasMove = true;
            } else  {
                pane.getChildren().remove(robot);
                robot.endMoving();
                //     root.game.timeLine.play();
            }

        }
        return wasMove;
    }

    /** Do hry prida noveho robota. */
    public void addRobot(Robot newRobot) {
        robots.add(newRobot);  // pridame ho do pola robotov

        newRobot.setY(entryRow * map.getSquareSize());
        newRobot.setX(entryCol * map.getSquareSize());

        pane.getChildren().add(newRobot);

        System.out.println("Adding robot " + newRobot.getId());
        // pridame ho na vstupne policko, ak tam je volno
        boolean received = squares[entryRow][entryCol].receiveRobot(newRobot, false);
        // ak nebolo volno, robot umiera
        if (!received) {
            newRobot.killed();
        }
        printSituation();
    }

    /** Vypise aktualnu situaciu hry na konzolu */
    public void printSituation() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                System.out.print(squares[i][j].toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    /** Na konzolu vypise statistiky o pocte vsetkych,
     * mrtvych a uspesnych robotov. */
    public void printStats() {
        int numKilled = 0;
        int numFinished = 0;
        for (Robot robot : robots) { // spocitaj mrtvych a uspesnych
            if (robot.isKilled()) {
                numKilled++;
            }
            if (robot.isFinished()) {
                numFinished++;
            }
        }
        System.out.println("The total number of robots: " + robots.size());
        System.out.println("The number of robots killed: " + numKilled);
        System.out.println("The number of robots finished: " + numFinished);
    }

    /** Metoda vrati pocet uspesnych robotov */
    public int getNumFinished(){
        int numFinished = 0;
        for (Robot robot : robots) { // spocitaj uspesnych
            if (robot.isFinished()) {
                numFinished++;
            }
        }
        return numFinished;
    }

    /** Metoda vrati pocet zabitych robotov */
    public int getNumKilled(){
        int numKilled = 0;
        for (Robot robot : robots) { // spocitaj uspesnych
            if (robot.isKilled()) {
                numKilled++;
            }
        }
        return numKilled;
    }

    /** Pomocna metoda pre konstruktor, kazdemu stvorceku oznami
     * svet a suradnice */
    private void registerSquares() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                squares[i][j].registerWorld(this, i, j);
                if (i > 0) {
                    squares[i][j].registerUp(squares[i - 1][j]);
                }
                if (i < nRows - 1) {
                    squares[i][j].registerDown(squares[i + 1][j]);
                }
                if (j > 0) {
                    squares[i][j].registerLeft(squares[i][j - 1]);
                }
                if (j < nCols - 1) {
                    squares[i][j].registerRight(squares[i][j + 1]);
                }
            }
        }
    }

    /* Metoda, ktora do riadku row a stlpca col ulozi nove
     * policko square. Spravne nastavi susedov novemu policku
     * aj susedom oznami nove policko. Na policku by vsak nemal
     * stat robot.
     */
    public void newSquare(Square square, int row, int col) {
        pane.getChildren().remove(squares[row][col]);
        pane.getChildren().add(square);
        square.toBack();
        square.setSize(sizeOfSquare);
        square.setY(sizeOfSquare * row);
        square.setX(sizeOfSquare*col);
        squares[row][col] = square;
        square.registerWorld(this, row, col);
        if (row > 0) {
            square.registerUp(squares[row - 1][col]);
            squares[row - 1][col].registerDown(square);
        }
        if (row < nRows - 1) {
            square.registerDown(squares[row + 1][col]);
            squares[row + 1][col].registerUp(square);
        }
        if (col > 0) {
            square.registerLeft(squares[row][col - 1]);
            squares[row][col - 1].registerRight(square);
        }
        if (col < nCols - 1) {
            square.registerRight(squares[row][col + 1]);
            squares[row][col + 1].registerLeft(square);
        }
    }

    public void setTimeLine(GameTimeLine t){ timeLine = t;}

    public boolean canAddRobot(){return !entrySquare.hasRobot();}
}
