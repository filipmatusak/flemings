package squares;

import javafx.scene.paint.Color;

/** Trieda reprezentujuca stvorcek typu stena.  Tento typ stvroceka sa
 * nijako nemeni a nemoze na neho prist robot. Dedi z triedy Square,
 * ktora uz ma pozadovane spravanie, treba implementovat len metodu
 * toString.
 */
public class WallSquare extends Square {

    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */
    public WallSquare(){
        setColor(Color.BURLYWOOD);
    }

     @Override
    public String toString() {
        return "W";
    }

//    @Override
//    public void exploding() {
//
//    }

    }
}
