package sample;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

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
}
