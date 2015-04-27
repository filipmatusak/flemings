package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import old.EmptySquare;
import old.WallSquare;

public class MapEditor {
    Main root;
    MapEditor thiz;
    ColoredRectangle[][] map;
    int height, width;
    Pane pane;
    Paint selectedColor;
    SquareChoiceMenu squareChoiceMenu;

    public MapEditor(Main root){
        this.root = root;
        thiz = this;
        squareChoiceMenu = new SquareChoiceMenu(this);
    }

    public void run(){
        height = root.map.height;
        width = root.map.width;
        map = new ColoredRectangle[height][width];
        pane = new Pane();
        selectedColor = null;

        pane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                squareChoiceMenu.show(event.getX() + root.mainStage.getX(),
                        event.getY() + root.mainStage.getY());
            }
        });

        pane.setOnMouseReleased(event -> {
            if (!event.isSecondaryButtonDown() && squareChoiceMenu != null) squareChoiceMenu.close();
        });

        initializeSquares();

        for(int i = 0; i < height; i++){
            map[i][0].setColor(new WallSquare().getColor());
            map[i][width-1].setColor(new WallSquare().getColor());
        }
        for(int i = 0; i < width; i++){
            map[0][i].setColor(new WallSquare().getColor());
            map[height-1][i].setColor(new WallSquare().getColor());
        }

        Scene scene = new Scene(pane);
        root.mainStage.setScene(scene);
        root.mainStage.show();


    }

    void initializeSquares(){
        pane.getChildren().clear();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ColoredRectangle r = new ColoredRectangle(j*root.map.squareSize, i*root.map.squareSize,
                        root.map.squareSize,root.map.squareSize);
                r.setColor(new EmptySquare().getColor());
                r.setColor(r.getFill());
                map[i][j] = r;
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.05);
                r.setOnMouseExited(event -> r.setFill(r.getColor()));
                r.setOnMouseMoved(event -> {
                    if (selectedColor != null) r.setFill(selectedColor);
                    if(event.isSecondaryButtonDown()) r.setBack();
                });
                r.setOnMousePressed(event -> {
                    r.setFill(selectedColor);
                    r.setFill(r.getColor());
                });
                r.setOnMouseDragged(event -> {
                    if(event.isSecondaryButtonDown()) return;
                    if (selectedColor != null) {
                        r.setFill(selectedColor);
                        if(event.isPrimaryButtonDown()) r.setColor(selectedColor);
                    }
                    Double a = event.getX(), b = event.getY();
                    Integer aa = a.intValue() / root.map.squareSize, bb = b.intValue() / root.map.squareSize;
                    if ( !event.isSecondaryButtonDown() && aa >= 0 && aa < width &&
                            bb >= 0 && bb < height &&
                            map[bb][aa] != r) {
                        r.setFill(r.getColor());
                        map[bb][aa].fireEvent(event);
                    }

                });
                r.setOnDragOver(event -> r.setFill(r.getColor()));
            }
            pane.getChildren().addAll(map[i]);
        }
    }
}
