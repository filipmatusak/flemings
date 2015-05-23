package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Main extends Application {

    public Scene scene;
    public Map map;
    public Pane menu;
    public Stage mainStage;
    public FileCreator fileCreator;
    public MapConvertor mapConvertor;
    public MapEditor mapEditor;
    public StartupMenu startup;
    public LevelMenu level;
    public Game game;
    AllRobots robotTypes;


    @Override
    public void start(Stage primaryStage) throws Exception{
        robotTypes = new AllRobots();
        mainStage = primaryStage;
        mainStage.initStyle(StageStyle.UTILITY);
        menu = new Pane();
        map = new Map(this, 30, 60);

        scene = new Scene(menu);

        startup = new StartupMenu(this);
        startup.run();

        mapEditor = new MapEditor(this);

        fileCreator = new FileCreator(this);
        mapConvertor = new MapConvertor(this);
        level = new LevelMenu(this);



    }

    public void replayLevel(Map map) throws InterruptedException{
        game.deregisterAll();
        game = new Game(this,map);
        try {
            game.run();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
