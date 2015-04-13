package old;

/** Trieda reprezentujuca jednoduchu vynimku pouzivanu na
 * ohlasovanie chyb pocas simulacie. Tieto chyby by pri spravnej
 * implementacii nemali nastat. */
public class RobotException extends RuntimeException {

    /** Konstruktor, ktory dostane text spravy o chybe. */
    public RobotException(String message) {
        super(message);
    }

}
