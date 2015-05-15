package robots;

import javafx.scene.input.KeyCode;
import old.Direction;

public class ExplodingRobot extends Robot {

    public ExplodingRobot(int changeTime, int maxHeight, String id) {
        super(changeTime, maxHeight, id);
        this.shortcut = KeyCode.E;
        this.type = "Exploding";
        this.limit = 5;
    }

    public ExplodingRobot() {
        this(10,10,"");
    }

    public ExplodingRobot(String id){
        this(10,10,id);
    }


    /** Vytvori jednoznakovu textovu reprezentaciu robota podla stavu */
    @Override
    public String toString() {
        if (time >= changeTime) { // zastaveny robot je !
            return "e";
        } else if (direction == Direction.RIGHT) { // inak sipocka
            return ">";
        } else {
            return "<";
        }
    }

    /** Spravi alternativny tah robota, t.j. ide vybuchnut */
    protected void alternativeMove() {
        System.out.println("Robot " + id + " exploding");
        this.mySquare.actionExploding();
    }

}
