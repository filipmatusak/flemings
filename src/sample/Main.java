package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import old.EmptySquare;
import old.Square;
import old.WallSquare;
import old.World;

public class Main extends Application {

    int height = 30, width = 60;
    int sizeOfSquare = 20;
    Square[][] mapa = new Square[width][height];
    World world;


    @Override
    public void start(Stage primaryStage) throws Exception{
        initializeMap();
        Pane gamePane = new Pane();
        world = new World(mapa, width, height ,gamePane, sizeOfSquare);
        Scene scene = new Scene(gamePane, width*sizeOfSquare, height*sizeOfSquare);
        primaryStage.setScene(scene);
        primaryStage.show();
        world.print();
      //  gamePane.getChildren().add(mapa[50][28].print());
     //   Rectangle r = new Rectangle(20,20, Color.BLACK);
     //   gamePane.getChildren().add(r);
     //   r.setX(300);
     //   r.setY(100);
     //   System.out.println(gamePane.getHeight() + " " + gamePane.getWidth());

    }


    public static void main(String[] args) {
        launch(args);
    }

    void initializeMap(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(i!=0 && j!=0 && i!=width-1 && j!=height-1) mapa[i][j] = new EmptySquare();
                else mapa[i][j] = new WallSquare();
            }
        }
    }
}
