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
    public FileCreator fileCreator;
    public MapConvector mapConvector;
    public MapEditor mapEditor;


    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        gamePane = new Pane();
        menu = new Pane();
        map = new Map(this, 30, 60);

        scene = new Scene(menu);

        StartupMenu startup = new StartupMenu(this);
        startup.run();

        mapEditor = new MapEditor(this);
    //    mapEditor.run();

        fileCreator = new FileCreator(this);
        mapConvector = new MapConvector(this);






    }


    public static void main(String[] args) {
        launch(args);
    }


}
