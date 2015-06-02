package robots;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import old.Direction;

public class DiggingRobot extends Robot {

    public DiggingRobot(int changeTime, int maxHeight, String id) {
        super(changeTime, maxHeight, id);
        image = new Image(getClass().getResourceAsStream("../graphics/robots/walle.png"));
        super.setImage(image);
        this.shortcut = KeyCode.D;
        this.type = "Digging";
        this.limit = 5;
    }

    public DiggingRobot() {
        this(10,10,"");
    }

    public DiggingRobot(String id){
        this(10,10,id);
    }

    /** Spravi alternativny tah robota, t.j. skusi kopat.
     * Ak nejde, chce sa otocit.*/
    @Override
    protected void alternativeMove() {
        if(this.mySquare.actionDigging(this.direction)) {
        }
        else normalMove();
    }

    @Override
    protected void normalMove() {
        if(this.mySquare.actionDigging(this.direction)) {
            endMoving();
        }
        else{
            boolean success = mySquare.actionMove(direction);
            if (!success) {
                direction = direction.otherDirection();
            }
        }
     //   endMoving();
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
