package old;

/** Mozne natocenia robota  (dolava alebo doprava). */
public enum Direction {

    LEFT, RIGHT;

    /** Metoda vrati opacne natocenie, teda ak this je LEFT, vrati RIGHT a naopak */
    Direction otherDirection() {
        if (this == LEFT) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }
}
