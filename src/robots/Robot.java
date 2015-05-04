package robots;

import old.Direction;
import old.RobotException;
import old.RobotHolder;

/** Trieda Robot predstavuje zakladneho robota. Robot si pamata
 * policko, na ktorom stoji, ale okrem toho o svete nic nevie. Robot
 * si udrzuje vlastny stav (aktivny, zabity, uspesne ukonceny), ako aj
 * natocenie a pocita si pocet tahov, ktore uz spravil. Pocas tahu sa
 * robot rozhodne, aku akciu chce vyskusat a zavola metodu svojho
 * policka prisluchajucu zvolenej akcii. Policko akciu bud vykona,
 * alebo ak sa neda, vrati false. Tento zakladny typ robota v prvych
 * tahoch sa pokusa chodit, a po zadanom pocte tahov zastane. */
public class Robot{

    /** Mozne stavy robota */
    protected enum Status {

        ACTIVE, FINISHED, KILLED
    }

    /** Stvorcek, na ktorom robot prave je, alebo null, ak je mimo plochy */
    protected RobotHolder mySquare;
    /** Meno robota */
    protected String id;
    /** Status robota (aktivny, mrtvy, uspesny) */
    protected Status status;
    /** Natocenie robota (dolava, doprava) */
    protected Direction direction;
    /** Kolko robot spravil tahov */
    protected int time;
    /** Po kolkych tahoch (od zaciatku) sa meni spravanie */
    protected int changeTime;
    /** Aka je maximalna vyska padu, pri ktorej sa nezabije */
    protected int maxHeight;

    /** Konstruktor, ktory dostane pocet tahov, po ktorych
     * sa zmeni spravanie robota, maximalnu vysku, s ktorej moze spadnut a prezit
     * a meno robota.
     */
    public Robot(int changeTime, int maxHeight, String id) {
        mySquare = null;
        this.id = id;
        this.maxHeight = maxHeight;
        status = Status.ACTIVE;
        direction = Direction.RIGHT;
        time = 0;
        this.changeTime = changeTime;
    }

    /** Spravi jeden tah robota a vsetky z
     * toho vyplyvajuce dosledky. Tuto metodu spravidla vola svet. */
    public void move() {
        // skontrolujem ze robot je aktivny
        if (status != Status.ACTIVE) {
            throw new RobotException("Inactive robot cannot move");
        }
        // zvysime pocitadlo tahov
        time++;
        // spravime bud standardny alebo alternativny typ tahu
        if (time > changeTime) {
            alternativeMove();
        } else {
            normalMove();
        }
    }

    /** Vytvori jednoznakovu textovu reprezentaciu robota podla stavu */
    @Override
    public String toString() {
        if (time >= changeTime) { // zastaveny robot je !
            return "!";
        } else if (direction == Direction.RIGHT) { // inak sipocka
            return ">";
        } else {
            return "<";
        }
    }

    /** Presunie robota na zadane policko
     * (bez dalsej kontroly, ci je to policko vhodne).
     * Ak robot nema policko, jednoducho sa prida na plochu.
     * Moze vyvolat dalsie zmeny, ak nieco zacne padat
     * do uvolneneho policka.
     * Tuto metodu spravidla vola policko, na ktore sa ma robot
     * posunut, ked vyhodnotilo vhodnost tejto akcie.
     */
    public void moveTo(RobotHolder newSquare) {
        // ak uz sme na ploche, odregistrujme sa
        if (mySquare != null) {
            mySquare.deregisterRobot();
        }
        // zaregistrujme nove policko
        mySquare = newSquare;
        mySquare.registerRobot(this);
    }

    /** Oznami robotovi, ze prave padol z danej vysky.
     * Ak je vyska prilis velka, robot umiera.
     * Tuto metodu spravidla vola policko, na ktore
     * robot dopadol. */
    public void fell(int height) {
        System.out.println("Robot " + id + " fell from height " + height);
        if (height > maxHeight) {
            killed();
        }
    }

    /** Oznami robotovi, ze umrel, napr. tym, ze ho niekto pripucil,
     * Zrusi sa z aktualneho policka a vypise oznam o svojom umrti.
     * Status odteraz bude KILLED. Tuto metodu spravidla
     * vola policko, na ktorom robot prave je, pripadne robot
     * sam po vyhodnoteni inych udalosti alebo svet, ak ho nevie
     * pri prichode vlozit na vstupne policko. */
    public void killed() {
        System.out.println("Robot " + id + " killed");
        if (mySquare != null) {
            mySquare.deregisterRobot();
        }
        status = Status.KILLED;
    }

    /** Oznami robotovi, ze dosiahol koncove policko.
     * Zrusi sa z aktualneho policka, vypise oznam
     * a zmeni status na FINISHED. Tuto metodu spravidla
     * vola koncove policko, ked na neho robot pride. */
    public void finished() {
        if (mySquare != null) {
            mySquare.deregisterRobot();
        }
        System.out.println("Robot " + id + " finished");
        status = Status.FINISHED;
    }

    /** Vrati, ci je robot este aktivny na ploche */
    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    /** Vrati, ci je robot mrtvy */
    public boolean isKilled() {
        return status == Status.KILLED;
    }

    /** Vrati, ci je robot uspesne skonceny */
    public boolean isFinished() {
        return status == Status.FINISHED;
    }

    /** Vrati meno robota */
    public String getId() {
        return id;
    }

    /** Spravi normalny tah robota, t.j. skusa ist dopredu
     * a ak sa neda, otoci sa. */
    protected void normalMove() {
        boolean success = mySquare.actionMove(direction);
        if (!success) {
            direction = direction.otherDirection();
        }
    }

    /** Spravi alternativny tah robota, t.j. nerobi nic. */
    protected void alternativeMove() {
    }
}
