package squares;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import robots.Robot;

public class SoilSquare extends Square{
    /** Vrati jednoznakovu textovu reprezentaciu policka
     * a pripadneho robota na nom. */

    public SoilSquare(){
        this.setColor(Color.DARKGRAY);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(0.05);
        type = "Soil Square (can be dug)";
    }

     @Override
    public String toString() {
        return "S";
    }

    /**
     * Metoda sa postara o dosledky akcie digging na tomto policku. Policko sa
     * zmeni na prazdne a policku nad nim odosle informaciu o tom, ze sa uvolnilo.
     * Moze nan pripadne spadnut robot zhora.
     */
    @Override
    public boolean digging(Square cell){
        EmptySquare tmp = new EmptySquare();
        world.newSquare(tmp, row, column);
        up.emptiedBelow();
        return true;
    }

    /**
     * Metoda sa vyrovnava s dosledkami vybuchu policka. Policko sa
     * zmeni na prazdne a policku nad nim odosle informaciu o tom, ze sa uvolnilo.
     * Moze nan pripadne spadnut robot zhora.
     */
    @Override
    public boolean exploding(){
        EmptySquare tmp = new EmptySquare();
        world.newSquare(tmp, row, column);
        up.emptiedBelow();
        return true;
    }

    /**
     * Metoda vracia false, pretoze na policko s hlinou nesmie vojst robot.
     */
    @Override
    public boolean receiveRobot(Robot otherRobot, Boolean move) {
        return false;
    }

    /**
     * Metoda vracia vzdy false, pretoze na policko s hlinou nemoze dopadnut robot.
     * Oznami padajucemu robotovi, ze sem sa padat neda a padol z vysky height-1.
     */
    @Override
    public boolean fallingRobot(Robot otherRobot, int height, Integer downMax) {
        if (height>1) otherRobot.fell(height - 1);
        return false;
    }



}
