package old;

import javafx.scene.layout.Pane;
import robots.Robot;
import squares.Square;

import java.util.ArrayList;

/** Trieda reprezentujuca svet pozostavajuci zo stvorcekov a robotov.
 */
public class World {

    /** 2D pole stvorcekov */
    protected Square[][] squares;
    /** pocet riadkov a stlpcov v poli stvrocekov */
    protected int nRows, nCols;
    /** poloha vstupneho stvorceka */
    protected int entryRow, entryCol;
    /** zoznam robotov, ktore uz vosli do sveta
     * (niektori z nich mozu byt mrtvi, alebo ukonceni). */
    protected ArrayList<Robot> robots;
    Pane pane;
    int sizeOfSquare;

    /** Konstruktor, ktory dostane pole stvorcekov
     * a inicializuje hernu situaciu s 0 robotmi.
     */
    public World(Square[][] squares, int entryRow, int entryCol, Pane pane_, int s) {
        sizeOfSquare = s;
        pane = pane_;
        robots = new ArrayList<>(); // vytvorime prazdne pole robotov
        this.squares = squares;            // ulozime si pole stvrcekov
        nRows = squares.length;          // zistime rozmery pola
        nCols = squares[0].length;
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



    /** Vykona jeden tah hry, t.j. necha kazdeho aktivneho robota
     * spravit tah a vrati true, ak je aspon jeden aktivny robot. */
    public boolean move() {
        boolean wasMove = false;
        for (Robot robot : robots) {
            if (robot.isActive()) { // ak mame aktivneho robota
                System.out.println("Move of robot " + robot.getId());
                robot.move();    // zavolame tah
                wasMove = true;  // nasli sme aktivneho
                printSituation();         // vypis celu plochu
            }
        }
        return wasMove;
    }

    /** Do hry prida noveho robota. */
    public void addRobot(Robot newRobot) {
        robots.add(newRobot);  // pridame ho do pola robotov

        System.out.println("Adding robot " + newRobot.getId());
        // pridame ho na vstupne policko, ak tam je volno
        boolean received = squares[entryRow][entryCol].receiveRobot(newRobot);
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
}
