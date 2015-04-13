package old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

/** Trieda obsahujuca hlavny program, ktory nacita popis sveta a popis robotov
 * a simuluje svet.
 */
public class Robots {

    /** Hlavny program. Ak pride k chybe kvoli chybajucemu alebo
     * nespravnemu suboru, vyhodi IOException s popisom chyby. Ak
     * dojde k necakanej chybe pocas simulacie (pravdepodobne chybou v
     * programe), vyhodi RobotException, pripadne nejaky iny typ
     * RunTimeException resp. Error.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // nacitaj mapu do pola riadkov
        ArrayList<String> lines = readLines(in);
        // podla riadkov vytvor stvorceky a z nich svet
        World world = createWorld(lines);

        // nacitaj druhy subor do pola riadkov
        lines = readLines(in);
        // mapa, ktora pre kazdy cas prichodu obsahuje prislusneho robota
        TreeMap<Integer, Robot> robots = createRobots(lines);

        // nacitaj maximalny pocet tahov
        int maxMoves = readInt(in);

        // samotna simulacia
        System.out.println("Initial configuration");
        world.printSituation();
        for (int time = 0; time < maxMoves; time++) {
       //     System.out.println(time + ":");
            boolean wasMove = world.move(); // sprav tah kazdym robotom
            if (robots.containsKey(time)) { // pridaj noveho robota
                world.addRobot(robots.get(time));
                robots.remove(time);        // zmaz pridaneho robota z mapy
            } else {
                // ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime
                if (robots.size() == 0 && !wasMove) {
                    break;
                }
            }
        }
        world.printStats(); // vypiseme celkove statistiky
    }

    /** Zo vstupu in nacita riadky po prazdny riadok alebo koniec vstupu
     * a ulozi ich do struktury ArrayList.
     * @throws IOException
     */
    private static ArrayList<String> readLines(BufferedReader in) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        while (true) {
            String line = in.readLine(); // nacitame riadok do retazca
            if (line == null || line.equals("")) { // skoncime, ked najdeme prazdny riadok
                break;
            } else {  // ulozime riadok
                lines.add(line);
            }
        }
        return lines;
    }

    /** Zo vstupu in nacita riadok s jendym celym cislom, ktore vrati.
     * @throws IOException
     */
    private static int readInt(BufferedReader in) throws IOException {
        String line = in.readLine(); // nacitame riadok do retazca
        if (line == null || line.equals("")) { // skontrolujeme, ze nebol prazdny riadok
            throw new IOException("Empty line instead of integer");
        }
        java.util.Scanner scanner = new java.util.Scanner(line);
        if (!scanner.hasNextInt()) {
            throw new IOException("Bad format of integer");
        }
        return scanner.nextInt();
    }

    /** Z daneho pola riadkov vytvori pole stvorcekov nasho sveta. */
    private static World createWorld(ArrayList<String> lines) throws IOException {
        if (lines.isEmpty() || lines.get(0).length() == 0) {
            throw new IOException("Empty world");
        }
        // zmeraj rozmery matice
        int nRows = lines.size();
        int nCols = lines.get(0).length();
        // alokuj maticu
        Square[][] squares = new Square[nRows][nCols];

        int entryRow = -1;
        int entryCol = -1;
        for (int i = 0; i < nRows; i++) { // prejdeme cez vsetky riadky
            if (lines.get(i).length() != nCols) {  // kontrola dlzky
                throw new IOException("Line " + i + " has different length");
            }
            for (int j = 0; j < nCols; j++) { // vsetky stlpce riadku
                squares[i][j] = createSquare(lines.get(i).charAt(j));
                String squareString = squares[i][j].toString();
                // skontroluj ci su na kraji steny
                if (!squareString.equals("W") && (i == 0 || i == nRows - 1 || j == 0 || j == nCols - 1)) {
                    throw new IOException("World should be surrounded by walls");
                }
                // hladame vstupne policko, ma byt prave jedno
                if (squareString.equals("#")) {  // hladame vstup
                    if (entryRow >= 0) {
                        throw new IOException("Multiple entry squares");
                    }
                    entryRow = i;
                    entryCol = j;
                }
            }
        }
        // skontrolujeme, ze mame vstupne policko
        if (entryRow < 0) {
            throw new IOException("No entry square");
        }

        World world = null;//= new World(squares, entryRow, entryCol);
        return world;
    }

    /** Z daneho pola riadkov vytvori zoznam robotov, pricom vysledok
     * je TreeMap, ktory jednotlivym casom prichodu priraduje robota,
     * ktory ma vtedy prist (ak taky je). */
    private static TreeMap<Integer, Robot> createRobots(ArrayList<String> lines)
    throws IOException {

        TreeMap<Integer, Robot> robots = new TreeMap<Integer, Robot>();
        for (int i = 0; i < lines.size(); i++) {
            // vytvor scanner pre aktualny riadok
            java.util.Scanner scanner = new java.util.Scanner(lines.get(i));

            // zo scannera zistime cas prichodu robota
            if (!scanner.hasNextInt()) {
                throw new IOException("Bad format of time int");
            }
            int entryTime = scanner.nextInt();
            if (entryTime <= 0) {
                throw new IOException("Non-positive entry time for a robot.");
            }
            if (robots.containsKey(entryTime)) {
                throw new IOException("At each time at most one robot can enter.");
            }

            // zo scannera zistime typ robota
            if (!scanner.hasNext()) {
                throw new IOException("Missing robot type");
            }
            String typeString = scanner.next();
            char robotType = typeString.charAt(0);

            // zo scannera zistime argument robota
            if (!scanner.hasNextInt()) {
                throw new IOException("Bad format of robot argument");
            }
            int robotArg = scanner.nextInt();

            // zo scannera zistime argument robota
            if (!scanner.hasNextInt()) {
                throw new IOException("Bad format of robot maximum fall height");
            }
            int robotMaxFall = scanner.nextInt();

            // zo scannera precitame meno robota
            if (!scanner.hasNext()) {
                throw new IOException("Missing robot name");
            }
            String robotId = scanner.next();

            // vytvorime robota prislusneho typu
            Robot robot = createRobot(robotType, robotArg, robotMaxFall, robotId);
            // pridame ho do zoznamu robotov
            robots.put(entryTime, robot);
        }
        return robots;
    }

    /** Vytvori robota prislusneho typu a s prislusnymi argumentami. */
    private static Robot createRobot(char robotType, int robotArg,
                                     int robotMaxHeight, String robotId) throws IOException {
        switch (robotType) {
        case 'S':
            return new Robot(robotArg, robotMaxHeight, robotId);
        case 'D':
            return new DiggingRobot(robotArg, robotMaxHeight, robotId);
        case 'E':
            return new ExplodingRobot(robotArg, robotMaxHeight, robotId);
        default:
            throw new IOException("Unknown type of robot (" + robotType + ")");
        }
    }

    /** Vytvori jeden stvorcek s typom podla zadaneho znaku. */
    private static Square createSquare(char c) throws IOException {
        switch (c) {
        case 'W':
            return new WallSquare();
        case '#':
            return new EntrySquare();
        case '.':
            return new EmptySquare();
        case '*':
            return new ExitSquare();
        case 'S':
            return new SoilSquare();
        case 'G':
            return new GlueSquare();
        default:
            throw new IOException("Unknown type of square (" + c + ")");
        }
    }
}
