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
    public MapConvertor mapConvertor;
    public MapEditor mapEditor;
    public StartupMenu startup;


    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        gamePane = new Pane();
        menu = new Pane();
        map = new Map(this, 30, 60);

        scene = new Scene(menu);

        startup = new StartupMenu(this);
        startup.run();

        mapEditor = new MapEditor(this);

        fileCreator = new FileCreator(this);
        mapConvertor = new MapConvertor(this);






    }


    public static void main(String[] args) {
        launch(args);
    }


}
