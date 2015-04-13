package old;

public class ExplodingRobot extends Robot{

    public ExplodingRobot(int changeTime, int maxHeight, String id) {
        super(changeTime, maxHeight, id);
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
        return;
    }

}
