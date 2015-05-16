package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import old.World;
import robots.Robot;
import squares.Square;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    Main root;
    World world;
    Map map;
    Stage stage;
    Scene scene;
    BorderPane pane;
    Pane gamePane;
    VBox infoPane;
    int maxMoves; //maximalne trvanie hry, malo by byt urcene levelom
    //TreeMap<Integer, Robot> robots;
    Queue<Robot> robots;
    Integer time;
    Label currentTime;
    GameTimeLine timeLine;


    public Game(Main root, Map map){
        this.root = root;
        this.map = map;
        this.maxMoves = 100; //docasne
      //  this.robots = new TreeMap<>();
        this.robots = new LinkedList<>();
        this.time = 0;
        this.pane = new BorderPane();
        this.gamePane = new Pane();
        this.infoPane = new VBox();
        this.currentTime = new Label();
        this.stage = new Stage();
        this.scene = new Scene(pane);
        this.world = new World(map,map.getEntryX(), map.getEntryY(),gamePane,map.getSquareSize(),root);

        timeLine = new GameTimeLine(root);

       /* gamePane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if(event.) timeLine.slower();
                else timeLine.faster();
            }
        });*/
    }

    public void move(){
        //     System.out.println(time + ":");
        boolean wasMove = world.move(); // sprav tah kazdym robotom
    /*    if (robots.containsKey(time)) {
            world.addRobot(robots.get(time));
            robots.remove(time);  */
        if(!robots.isEmpty()){
            world.addRobot(robots.remove());
        } else {
            // ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime
            if (robots.size() == 0 && !wasMove) {
                timeLine.stop();
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
     //   robots.put(1, new Robot("eva"));
     //   robots.put(3, new DiggingRobot("walle"));
        robots.add(new Robot("eva"));
        robots.add(new Robot("walle"));

        timeLine.start();

        /**
         * Samotna hra - v cykle vykonava akcie robotov a pridava novych. Namiesto podmienky time < 5 musi byt ina,
         * asi cas + kym je nazive nejaky robot + kym je aktivny nejaky robot alebo neaktivny a je co pridavat
         * Teraz to nefunguje, preto je podmienka 0<0, vid NOTES
         *//*
        while (this.time < 0) {
                //     System.out.println(time + ":");
                boolean wasMove = world.move(); // sprav tah kazdym robotom
                if (robots.containsKey(time)) { // pridaj noveho robota
                    world.addRobot(robots.get(time));
                    robots.remove(time);        // zmaz pridaneho robota z mapy
                } else {
                    // ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime
                    if (robots.size() == 0 && !wasMove) {
                        break;
                    }
                }
                this.redraw();
                //toto by malo pockat a prekreslit situaciu, ale nefunguje
                sleep(1000);
                this.time++;
            }
            world.printStats(); // vypiseme celkove statistiky*/
        }


    /**
     * NEFUNKCNE - funguje len prvy raz
     * Prekresli mapu a updatuje cas
     */
    public void redraw(){
        gamePane.getChildren().clear();
        for (Square[] aMap : map.getMap()) gamePane.getChildren().addAll(aMap);
        currentTime.setText("Current time: " + this.time.toString());
        infoPane.getChildren().remove(currentTime);
        infoPane.getChildren().add(currentTime);
    }

    /**
     * Vykresli okno, mapu levelu, tlacidla na pridanie vsetkych moznych robotov a nainicializuje cas
     */
    public void init(){
        this.redraw();
        ArrayList<HBox> robotsMenu = new ArrayList<>();
        for (Iterator it = root.robotTypes.getTypes().iterator(); it.hasNext(); ){
            Robot tmp = (Robot) it.next();
            HBox hb = new ButtonRobot(tmp,root);
            robotsMenu.add(hb);
        }
        infoPane.getChildren().addAll(robotsMenu);
        pane.setRight(infoPane);
        pane.setLeft(gamePane);
        stage.setTitle("FlemmingZ");
        stage.setScene(scene);
        stage.show();

    }


}
