package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public Pane gamePane;
    public Scene scene;
    public Map map;
    public Pane menu;
    public Stage mainStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        gamePane = new Pane();
        menu = new Pane();
        map = new Map(this, 30, 60);

        scene = new Scene(menu, 100, 100);

        MapEditor mapEditor = new MapEditor(this);
        mapEditor.run();

       // primaryStage.setScene(scene);
       // primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }


}
