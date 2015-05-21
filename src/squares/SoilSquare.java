package squares;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SoilSquare extends Square{
    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */

    public SoilSquare(){
        this.setColor(Color.DARKGRAY);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
    }

     @Override
    public String toString() {
        return "S";
    }

    /**
     * pri vybuchnuti policka zmenime jeho typ
     */
    @Override
    public void exploding() {
        Square b = new EmptySquare();
        this.world.newSquare( b, row, column);

        b.up.emptiedBelow();
    }

}
