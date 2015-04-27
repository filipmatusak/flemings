package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import old.Square;

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

    public SquareChoiceMenu(MapEditor mapEditor){
        this.mapEditor = mapEditor;
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        pane = new Pane();
        allTypes = new AllSquares().getTypes();
        row = (allTypes.size()+1)/col;
        scene = new Scene(pane, col*size + (col+1)*space, row*size + (row+1)*space);
        stage.setScene(scene);
        initializeTypes();
        printSquares();

        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

    }

    void printSquares(){
        for(int i = 0; i < allTypes.size(); i++){
            pane.getChildren().add(allRectangles.get(i));
        }
    }

    void initializeTypes(){

        for(int i = 0; i < allTypes.size(); i++){
            Rectangle s = new Rectangle(size,size,allTypes.get(i).getColor());
            int r = i/3;
            int c = i%3;
            s.setX(space * (c + 1) + c * size);
            s.setY(space * (r + 1) + r * size);
            allRectangles.add(s);

            s.setOnMouseEntered(event -> {
                s.setStroke(Color.AQUA);
                s.setStrokeWidth(5);
                selected = s.getFill();
            });

            s.setOnMouseExited(event -> {
                if (!stage.isShowing()) return;
                s.setStroke(null);
                selected = null;
            });

            s.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    s.setStroke(null);
                }
            });

        }
    }

    public void show(Double x, Double y){
        stage.setX(x);
        stage.setY(y+20);
        stage.show();
    }

    public void close(){
        mapEditor.selectedColor = selected;
        stage.close();
        for (Rectangle allRectangle : allRectangles) allRectangle.setStroke(null);
    }
}
