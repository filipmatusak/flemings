package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import old.Direction;
import old.World;
import robots.Robot;
import squares.EmptySquare;
import squares.ExitSquare;
import squares.Square;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class Game {
    Main root;
    World world;
    Map map;
    Map mapCopy;
    Stage stage;
    Scene scene;
    BorderPane pane;
    Pane gamePane;
    VBox infoPane;
    Integer maxMoves; //maximalne trvanie hry, malo by byt urcene levelom
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
        Integer numberOfRobotTypes = new AllRobots().getTypes().size();
        readyRobots = new ArrayList<>();
        for(int i = 0; i < numberOfRobotTypes; i++) readyRobots.add(1);


    }

    public void move(){
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
      //  this.redraw();
        this.time++;

        world.printStats(); // vypiseme celkove statistiky*/
    }

    public void run() throws InterruptedException {
        this.init();
        System.out.println("Initial configuration");
        world.printSituation();

     //   debug();

//        robots.add(new Robot("eva"));
//        robots.add(new Robot("walle"));

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

     //   System.out.println(); for(Node x: gamePane.getChildren()) System.out.print("* "); System.out.println();

        for (Square[] aMap : world.getSquare()){
            for(Square x: aMap){
                try{
                    x.toBack();
                    gamePane.getChildren().add(x);
                } catch (Exception e){

                }
            }

        }
        for(Robot robot: world.robots) robot.toFront();
    }

    /**
     * Vykresli okno, mapu levelu, tlacidla na pridanie vsetkych moznych robotov a nainicializuje cas
     */
    public void init(){
        this.redraw();
        robotsMenu = new ArrayList<>();
        Integer i = 0;
        for (Robot tmp : root.robotTypes.getTypes()) {
            ButtonRobot hb = new ButtonRobot(tmp, root, readyRobots.get(i++));
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
                    //timeLine.start();
                } catch (InterruptedException e) {

                }
            }
        });

        Button pause = new Button("PAUSE");
        pause.setPrefWidth(100);
        Style.setButtonStyle(pause);

        Button quit = new Button("QUIT");
        Style.setButtonStyle(quit);
        quit.setPrefWidth(100);
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
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
        Pane dpane = new Pane();
        VBox vbox = new VBox();
        Scene dscene = new Scene(dpane);
        dpane.setPrefHeight(dwidth);
        dpane.setPrefWidth(dwidth);
        Stage dstage = new Stage();
        Label finished = new Label("Number of robots finished: " + world.getNumFinished());
        Label killed = new Label("Number of robots killed: "+ world.getNumKilled());
        Label success = new Label();
        if (world.getNumFinished() >= this.target){
            success.setText("Level passed!");
        }
        else{
            success.setText("Level failed!");
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
                    System.out.println(root.levelFile.toPath());
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
                }
                catch (InterruptedException e) {
                }
                catch (FileNotFoundException e) {
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
        vbox.getChildren().addAll(finished,killed,success,btnReplay,btnNext,btnMenu,btnQuit);
        dpane.getChildren().addAll(vbox);

        dstage.setTitle("Game Finished");
        dstage.setScene(dscene);
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
    }

    public void deregisterAll(){
        for (Square[] row : map.getMap()){
            for (Square square : row){
                if (square instanceof EmptySquare){
                    ((EmptySquare) square).deregisterRobot();
                }
                else if (square instanceof ExitSquare){
                    ((ExitSquare) square).deregisterRobot();
                }
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
}
