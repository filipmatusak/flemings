package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import old.World;

public class Game {
    Main root;
    World world;
    Map map;
    Stage stage;
    Scene scene;
    Pane pane;

    public Game(Main root, Map map){
        this.root = root;
        this.map = map;
    }
        public void run(){

            pane = new Pane();
            /**
             * Inicializacia hlavnej triedy World, ktora ma na starosti akcie robotov
             */
            world = new World(map,map.getEntryX(), map.getEntryY(),pane,map.getSquareSize(),root);

            /**
             * Vykreslenie mapy na herne platno
             */
            ColoredRectangle[][] gameMap = map.getMapView();
            for (ColoredRectangle[] aMap : gameMap) pane.getChildren().addAll(aMap);
            stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setTitle("FlemmingZ");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
}
