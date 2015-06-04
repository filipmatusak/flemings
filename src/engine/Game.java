/** Trieda reprezentujuca samotnu hru. Moze vzniknut nacitanim levelu zo zoznamu alebo konvertovanim vlastnej mapy.
 * Obsahuje hracie pole, tlacidla na pridavanie robotov, ovladaci panel na kontrolovanie hry a informacny panel so
 * statistikami o pocte robotov*/

package engine;

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
import squares.Square;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;


public class Game {
    Main root;      /** odkaz na hlavnu triedu*/
    World world;    /** trieda, ktora obsahuje robotov a spravuje ich pohyb */
    Map map;        /** hracie pole - mapa, ktora sa prekresluje pri zmene typu policok */
    Map mapCopy;    /** kopia hracieho pola - rekonstrukcia povodnej mapy, pouzivana pri restartovani levelu*/
    Stage stage;
    Scene scene;
    BorderPane pane;
    static Pane gamePane;
    VBox infoPane;          /** obsahuje labely so statistikami o priebehu hry*/
    Integer maxMoves;
    Queue<Robot> robots;    /** zoznam robotov, ktori pridu do hry (rad)*/
    ArrayList<Integer> readyRobots;
    Integer time;           /** aktualny cas */
    Label currentTime;      /** info o aktualnom case */
    Label robotCount;       /** info o pocte pouzitych robotov */
    Label killedRobots;     /** info o pocte zabitych robotov */
    Label savedRobots;      /** info o pocte zachranenych robotov */
    Label targetCount;      /** info o targete pre dany level*/
    Label activeRobots;     /** info o pocte aktivnych robotov - takych, co ziju a mozu sa hybat */
    ArrayList<ButtonRobot> robotsMenu;
    int target = 0;         /** ciel hry - pocet robotov, ktorych treba zachranit */
    public GameTimeLine timeLine; /** casovac, ktory sa stara o znazornovanie pohybu robotov a vsetky animacie */

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
    }

    /** Funkcia sa vola v kazdom casovom okamihu - vykona pohyb kazdym robotom a aktualizuje stav */
    public void move() {
        /** aktualizuje herne statistiky a vypise ich na infopanel */
        printLabelStats();

        boolean wasMove = world.move(); // sprav tah kazdym robotom
        if(!robots.isEmpty()){
            if(world.canAddRobot()) world.addRobot(robots.remove());
        } else {
            /** ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime */
            if ((robots.size() == 0 && !existReadyRobots() && (!wasMove || world.getNumActive() == 0))) {
                timeLine.stop();
                stage.close();
                finishedDialog();
                return;
            }
        }
        this.time++;
    }

    /** Funkcia nastartuje hru */
    public void run() throws InterruptedException {
        this.init();
        timeLine.start();
        }

    /** Funkcia vykresli mapu na hraci plan a prida do hry robotov */
    public void redraw(){
        gamePane.getChildren().removeAll(gamePane.getChildren().filtered(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
                return node instanceof Square;
            }
        }));

        /** vykresli vsetky stvorceky z mapy na hraci plan */
        for (Square[] aMap : world.getSquare()){
            for(Square x: aMap){
                try{
                    x.toBack();
                    gamePane.getChildren().add(x);
                } catch (Exception e){

                }
            }

        }
        /** ulozi vsetkych robotov, ktori maju prist do hry, do radu */
        for(Robot robot: World.robots) robot.toFront();
    }

    /** Vykresli okno, mapu levelu, tlacidla na pridanie vsetkych moznych robotov a nainicializuje cas */
    public void init(){
        this.redraw();

        /** vykreslenie tlacidiel na pridavanie robotov */
        robotsMenu = new ArrayList<>();
        Integer i = 0;
        for (Robot tmp : AllRobots.getTypes()) {
            ButtonRobot hb = new ButtonRobot(tmp, root, map.getLimits().get(i++));
            robotsMenu.add(hb);
        }

        /** tlacidlo na restartovanie levelu */
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

        /** tlacidlo na pozastavenie, resp. pokracovanie v hre */
        Button pause = new Button("PAUSE");
        pause.setPrefWidth(100);
        Style.setButtonStyle(pause);
        /** po stlaceni sa zmeni stav hry (beziaca, pozastavena) a text na tlacidle */
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

        /** tlacidlo na ukoncenie aktualnej hry - zobrazi sa startovaci dialog */
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

        /** aktualizuje statistiky a vypise ciel hry */
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

    /** Funkcia vrati true, ak sa da este vyrobit nejaky robot */
    boolean existReadyRobots(){
        for(ButtonRobot i: robotsMenu) if(i.getCount()>0) return true;
        return false;
    }

    /** Funkcia spusti dialog po ukonceni hry, ktory vypise informaciu o tom, ci bol level uspesne prejdeny, kolko bolo
     * zachranenych robotov a dalsie menu s tlacidlami na navrat do menu, restartovanie levelu, spustenie nasledujuceho
     * levelu a ukoncenie hry */
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

        /** Tlacidlo na restartovanie levelu. Spusti hru s tou istou povodnou mapou (ulozenou na zaciatku), pricom zo
         * vsetkych policok najprv odregistruje robotov */
        Button btnReplay = new Button("REPLAY");
        Style.setButtonStyle(btnReplay);
        btnReplay.setPrefWidth(dwidth);
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

        /** Tlacidlo na spustenie dalsieho levelu. Ak je aktualny level posledny alebo ide o pouzivatelom vytvorenu mapu,
         * spusti prvy level */
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

        /** Tlacidlo na navrat do hlavneho menu */
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

        /** Tlacidlo na definitivne ukoncenie hry */
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
        dstage.show();
    };

    /** Funkcia odregistruje robotov zo vsetkych policok mapy*/
    public void deregisterAll(){
        for (Square[] row : map.getMap()){
            for (Square square : row){
                if (square instanceof RobotHolder){
                    ((RobotHolder) square).deregisterRobot();
                }
            }
        }
    }

    /** Funkcia vypise statistiky o poctoch robotov na infopanel */
    public void printLabelStats(){
        currentTime.setText("Current time: " + this.time.toString());
        robotCount.setText("Used: " + world.getNumRobots());
        killedRobots.setText("Killed: " + world.getNumKilled());
        savedRobots.setText("Saved: " + world.getNumFinished());
        activeRobots.setText("Active: " + world.getNumActive());
    }

    public static Pane getGamePane(){ return gamePane; }
}