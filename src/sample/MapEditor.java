package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import old.EmptySquare;
import old.WallSquare;

public class MapEditor {
    Main root;
    Rectangle[][] map;
    int height, width;
    Pane pane;

    public MapEditor(Main root){
        this.root = root;
    }

    public void run(){
        height = root.map.height;
        width = root.map.width;
        map = new Rectangle[height][width];
        pane = new Pane();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                Rectangle r = new Rectangle(j*root.map.squareSize, i*root.map.squareSize,
                        root.map.squareSize,root.map.squareSize);
                r.setFill(new EmptySquare().getColor());
                map[i][j] = r;

            }
            pane.getChildren().addAll(map[i]);
        }
        for(int i = 0; i < height; i++){
            map[i][0].setFill(new WallSquare().getColor());
            map[i][width-1].setFill(new WallSquare().getColor());
        }
        for(int i = 0; i < width; i++){
            map[0][i].setFill(new WallSquare().getColor());
            map[height-1][i].setFill(new WallSquare().getColor());
        }

        Scene scene = new Scene(pane);
        root.mainStage.setScene(scene);
        root.mainStage.show();




    }
}
