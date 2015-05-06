package robots;

import old.Direction;

public class DiggingRobot extends Robot {

    public DiggingRobot(int changeTime, int maxHeight, String id) {
        super(changeTime, maxHeight, id);
    }

    /** Spravi alternativny tah robota, t.j. skusi kopat.
     * Ak nejde, chce sa otocit. aaaa*/
    @Override
    protected void alternativeMove() {
        if(this.mySquare.actionDigging(this.direction)) {
        }
        else normalMove();
    }

    /** Vytvori jednoznakovu textovu reprezentaciu robota podla stavu */
    @Override
    public String toString() {
        if (time >= changeTime) { // kopajuci robot je d
            return "d";
        } else if (direction == Direction.RIGHT) { // inak sipocka
            return ">";
        } else {
            return "<";
        }
    }
}
