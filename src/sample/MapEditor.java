package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import old.EmptySquare;
import old.WallSquare;

public class MapEditor {
    Main root;
    ColoredRectangle[][] map;
    int height, width;
    Pane pane;
    Paint selectedColor, oldColor;

    public MapEditor(Main root){
        this.root = root;
    }

    public void run(){
        height = root.map.height;
        width = root.map.width;
        map = new ColoredRectangle[height][width];
        pane = new Pane();
        selectedColor = Color.AZURE;

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
                });
                r.setOnMousePressed(event -> {
                    r.setFill(selectedColor);
                    r.setFill(r.getColor());
                });
                r.setOnMouseDragged(event -> {
                    if (selectedColor != null) {
                        r.setFill(selectedColor);
                        r.setColor(selectedColor);
                    }
                    Double a = event.getX(), b = event.getY();
                    Integer aa = a.intValue() / root.map.squareSize, bb = b.intValue() / root.map.squareSize;
                    if (aa >= 0 && aa < width &&
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
}
