/** Pomocna trieda na zobrazenie  dialogu pre vyber typu policka v MapEditore. Tymto typom polica sa potom do mapy kresli*/

package engine;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import squares.Square;

import java.util.ArrayList;

public class SquareChoiceMenu {

    MapEditor mapEditor;
    Stage stage;
    Scene scene;
    ArrayList<Square> allTypes;
    ArrayList<Rectangle> allRectangles = new ArrayList<>();
    Pane pane;
    int row, col = 3;
    int size = 40, space = 10;
    Paint selected;
    Boolean isIn;

    public SquareChoiceMenu(MapEditor mapEditor){
        this.mapEditor = mapEditor;
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        pane = new Pane();
        allTypes = new AllSquares().getTypes();
        row = (allTypes.size()+1)/col;
        scene = new Scene(pane, col*size + (col+1)*space, row*size + (row+1)*space);
        stage.setScene(scene);
        initializeTypes();
        printSquares();

        scene.setOnMouseExited(event -> {
            if(isIn){
                close();
            }
        });

        scene.setOnMouseClicked(event -> {
            close();
        });

        scene.setOnMouseMoved(event -> {
            isIn = true;
            event.consume();
        });

    }

    /** Funkcia prida na panel policko kazdeho typu */
    void printSquares(){
        for(int i = 0; i < allTypes.size(); i++){
            pane.getChildren().add(allRectangles.get(i));
        }
    }

    void initializeTypes(){

        for(int i = 0; i < allTypes.size(); i++){
            Rectangle s = new Rectangle(size,size,allTypes.get(i).getColor());
            int r = i/col;
            int c = i%col;
            s.setX(space * (c + 1) + c * size);
            s.setY(space * (r + 1) + r * size);
            allRectangles.add(s);

            s.setOnMouseEntered(event -> {
                s.setStroke(Color.AQUA);
                s.setStrokeWidth(5);

            });

            s.setOnMouseExited(event -> {
                if (!stage.isShowing()) return;
                s.setStroke(null);
                selected = null;
                isIn = true;
            });

            s.setOnMouseClicked(event -> {
                s.setStroke(null);
                selected = s.getFill();
            });

        }
    }

    /** Funkcia zobrazi dialog na vyber policka */
    public void show(Double x, Double y){
        stage.setAlwaysOnTop(true);
        stage.setX(x-5);
        stage.setY(y + 20-5);
        stage.show();
        isIn = false;
    }

    public boolean isVisible(){
        return stage.isShowing();
    }

    public void close(){
        mapEditor.selectedColor = selected;
        stage.close();
        for (Rectangle allRectangle : allRectangles) allRectangle.setStroke(null);
    }
}
