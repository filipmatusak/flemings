package sample;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * policko v mapEditore s funciami na prefarbenie policka
 */

public class ColoredRectangle extends Rectangle {
    private Paint color;
    public ColoredRectangle(Integer x, Integer y, Integer h, Integer w){
        super(x,y,h,w);
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
        this.setFill(color);
    }

    public void setBack(){
        this.setFill(color);
    }
}
