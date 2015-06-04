package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import old.RobotHolder;
import old.World;
import robots.Robot;
import squares.EmptySquare;
import squares.EntrySquare;
import squares.ExitSquare;
import squares.Square;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import javafx.scene.input.KeyEvent;


public class Game {
    Main root;
    World world;
    Map map;
    Map mapCopy;
    Stage stage;
    Scene scene;
    BorderPane pane;
    static Pane gamePane;
    VBox infoPane;
    Integer maxMoves;
    Queue<Robot> robots;
    ArrayList<Integer> readyRobots;
    Integer time;
    Label currentTime;
    Label robotCount;
    Label killedRobots;
    Label savedRobots;
    Label targetCount;
    Label activeRobots;
    ArrayList<ButtonRobot> robotsMenu;
    /** ciel hry - pocet robotov, ktorych treba zachranit */
    int target = 0;

    public GameTimeLine timeLine;


    public Game(Main root, Map map){
        this.root = root;
        this.map = map;
        this.mapCopy = map.clone();
        this.maxMoves = 100; //docasne
        this.robots = new LinkedList<>();
        this.time = 0;
        this.pane = new BorderPane();
        this.gamePane = new Pane();
        this.infoPane = new VBox();
        this.currentTime = new Label();
        this.robotCount = new Label();
        this.killedRobots = new Label();
        this.savedRobots = new Label();
        this.targetCount = new Label();
        this.activeRobots = new Label();
        this.stage = new Stage();
        this.target = map.getTarget();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timeLine.stop();
                root.startup.run();
            }
        });
        this.scene = new Scene(pane);
        this.timeLine = new GameTimeLine(root);
        this.world = new World(map,map.getEntryX(), map.getEntryY(),gamePane,map.getSquareSize(),root);
        world.setTimeLine(timeLine);

        //docasne, potom by ich mal dostat ako parameter
        Integer numberOfRobotTypes = AllRobots.getTypes().size();
        readyRobots = new ArrayList<>();
        for(int i = 0; i < numberOfRobotTypes; i++) readyRobots.add(5);


    }

    public void move() {
        System.out.println(time + ":");
        printLabelStats();

        boolean wasMove = world.move(); // sprav tah kazdym robotom
        if(!robots.isEmpty()){
            if(world.canAddRobot()) world.addRobot(robots.remove());
        } else {
            // ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime
            if ((robots.size() == 0 && !existReadyRobots() && (!wasMove || world.getNumActive() == 0))) {
                System.out.println("Nothing to do");
                timeLine.stop();
                stage.close();
                finishedDialog();
                return;
            }
        }
        this.time++;

        world.printStats(); // vypiseme celkove statistiky*/
    }

    public void run() throws InterruptedException {
        this.init();
        System.out.println("Initial configuration");
        world.printSituation();

        timeLine.start();
        }

    /**
     * NEFUNKCNE - funguje len prvy raz
     * Prekresli mapu a updatuje cas
     */
    public void redraw(){
    //    gamePane.getChildren().clear();
        gamePane.getChildren().removeAll(gamePane.getChildren().filtered(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
                return node instanceof Square;
            }
        }));

        for (Square[] aMap : world.getSquare()){
            for(Square x: aMap){
                try{
                    x.toBack();
                    gamePane.getChildren().add(x);
                } catch (Exception e){

                }
            }

        }
        for(Robot robot: World.robots) robot.toFront();
    }

    /**
     * Vykresli okno, mapu levelu, tlacidla na pridanie vsetkych moznych robotov a nainicializuje cas
     */
    public void init(){
        this.redraw();
        robotsMenu = new ArrayList<>();
        Integer i = 0;
        for (Robot tmp : AllRobots.getTypes()) {
            ButtonRobot hb = new ButtonRobot(tmp, root, map.getLimits().get(i++));
            robotsMenu.add(hb);
        }

        Button replay = new Button("REPLAY");
        Style.setButtonStyle(replay);
        replay.setPrefWidth(100);
        replay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    timeLine.stop();
                    stage.close();
                    root.replayLevel(mapCopy);
                } catch (InterruptedException e) {

                }
            }
        });

        Button pause = new Button("PAUSE");
        pause.setPrefWidth(100);
        Style.setButtonStyle(pause);
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeLine.change();
                if (pause.getText() == "PAUSE"){
                    pause.setText("RESUME");
                }
                else{
                    pause.setText("PAUSE");
                }
            }
        });

        Button quit = new Button("QUIT");
        Style.setButtonStyle(quit);
        quit.setPrefWidth(100);
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                timeLine.stop();
                root.startup.run();
            }
        });

        printLabelStats();
        targetCount.setText("TARGET: " + target);

        infoPane.getChildren().addAll(robotsMenu);
        infoPane.getChildren().addAll(replay, pause, quit);
        infoPane.getChildren().addAll(targetCount, currentTime, robotCount, killedRobots, savedRobots, activeRobots);
        pane.setRight(infoPane);
        pane.setLeft(gamePane);
        stage.setTitle("FlemmingZ");
        stage.setScene(scene);
        stage.show();

//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                int i = 0;
//                for (Robot robot : AllRobots.getTypes()) {
//                    if (robot.getShortcut() == event.getCode()) {
//                        robotsMenu.get(i).getOnMouseClicked().handle();
//                    }
//                    i++;
//                }
//            }
//        });

    }

    /**
     * ci sa da este vyrobit nejaky robot
     */
    boolean existReadyRobots(){
        for(ButtonRobot i: robotsMenu) if(i.getCount()>0) return true;
        return false;
    }

    private void finishedDialog(){
        int dwidth = 250;
        BorderPane dpane = new BorderPane();

        Image image = new Image(getClass().getResourceAsStream("../graphics/robots/walle.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        dpane.setBackground(background);

        VBox vbox = new VBox();
        VBox vbox2 = new VBox();
        Scene dscene = new Scene(dpane);
        dpane.setPrefHeight(dwidth);
        dpane.setPrefWidth(dwidth);
        Stage dstage = new Stage();
        Label finished = new Label("Finished: " + world.getNumFinished());
        Label wasTarget = new Label("Target: "+ map.getTarget());
        Style.setTextStyle(finished);
        if (world.getNumFinished() >= this.target){
            dstage.setTitle("Level passed!");
            Style.setColor(finished, Color.GREEN);
        }
        else{
            dstage.setTitle("Level failed!");
            Style.setColor(finished, Color.RED);
        }
        Button btnReplay = new Button("REPLAY");
        Style.setButtonStyle(btnReplay);
        btnReplay.setPrefWidth(dwidth);
        Button btnNext = new Button("NEXT LEVEL");
        Style.setButtonStyle(btnNext);
        btnNext.setPrefWidth(dwidth);
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dstage.close();
                File nextLevel = AllLevels.getLevels().get(0);
                if (root.levelFile != null) {
                    File next = AllLevels.getNextLevel(root.levelFile);
                    if (next != null) nextLevel = next;
                }
                Map map = null;
                try {
                    map = root.mapConvertor.fileToMap(nextLevel);
                    root.levelFile = nextLevel;
                    root.game = new Game(root, map);
                    stage.close();
                    root.game.run();
                } catch (InterruptedException | FileNotFoundException e) {
                }
            }
        });
        Button btnMenu = new Button("MENU");
        Style.setButtonStyle(btnMenu);
        btnMenu.setPrefWidth(dwidth);
        btnMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dstage.close();
                root.startup.run();
            }
        });
        Button btnQuit = new Button("QUIT");
        Style.setButtonStyle(btnQuit);
        btnQuit.setPrefWidth(dwidth);
        btnQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dstage.close();
            }
        });
        vbox2.getChildren().addAll(wasTarget, finished);
        vbox.getChildren().addAll(btnReplay, btnNext, btnMenu, btnQuit);
        dpane.setBottom(vbox);
        dpane.setTop(vbox2);

        dstage.initModality(Modality.APPLICATION_MODAL);
        dstage.setAlwaysOnTop(true);
        dstage.setScene(dscene);
//        dstage.setResizable(false);
        dstage.show();

        btnReplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dstage.close();
                try {
                    root.replayLevel(mapCopy);
                } catch (InterruptedException e) {

                }
            }
        });
    };

    public void deregisterAll(){
        for (Square[] row : map.getMap()){
            for (Square square : row){
                if (square instanceof RobotHolder){
                    ((RobotHolder) square).deregisterRobot();
                }
//                if (square instanceof EmptySquare){
//                    ((EmptySquare) square).deregisterRobot();
//                }
//                else if (square instanceof EntrySquare){
//                    ((EntrySquare) square).deregisterRobot();
//                }
//                else if (square instanceof ExitSquare){
//                    ((ExitSquare) square).deregisterRobot();
//                }
            }
        }
    }

    public void printLabelStats(){
        currentTime.setText("Current time: " + this.time.toString());
        robotCount.setText("Used: " + world.getNumRobots());
        killedRobots.setText("Killed: " + world.getNumKilled());
        savedRobots.setText("Saved: " + world.getNumFinished());
        activeRobots.setText("Active: " + world.getNumActive());
    }

    public static Pane getGamePane(){ return gamePane; }

    public static void explosion(Node node){
        gamePane.getChildren().add(node);
    }
}