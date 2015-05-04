package squares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SoilSquare extends Square{
    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */

    public SoilSquare(){
        this.setColor(Color.DARKGRAY);
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

    public javafx.scene.shape.Rectangle print(){
        Rectangle r = new Rectangle(size,size,color);
        r.setY(column * size);
        r.setX(row * size);
        System.out.println(row + " " + column);
        return r;
    }
}
