package sample;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import old.World;
import robots.Robot;
import squares.Square;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class Game {
    Main root;
    World world;
    Map map;
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
    ArrayList<ButtonRobot> robotsMenu;
    public GameTimeLine timeLine;


    public Game(Main root, Map map){
        this.root = root;
        this.map = map;
        this.maxMoves = 100; //docasne
        this.robots = new LinkedList<>();
        this.time = 0;
        this.pane = new BorderPane();
        this.gamePane = new Pane();
        this.infoPane = new VBox();
        this.currentTime = new Label();
        this.stage = new Stage();
        this.scene = new Scene(pane);
        this.timeLine = new GameTimeLine(root);
        this.world = new World(map,map.getEntryX(), map.getEntryY(),gamePane,map.getSquareSize(),root);
        world.setTimeLine(timeLine);

        //docasne, potom by ich mal dostat ako parameter
        Integer numberOfRobotTypes = new AllRobots().getTypes().size();
        readyRobots = new ArrayList<>();
        for(int i = 0; i < numberOfRobotTypes; i++) readyRobots.add(5);

        /*
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentTime.setText("Current time: " + time.toString());
            }
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();*/

    }

    public void move(){
        System.out.println(time + ":");
        boolean wasMove = world.move(); // sprav tah kazdym robotom
        if(!robots.isEmpty()){
            if(world.canAddRobot()) world.addRobot(robots.remove());
        } else {
            // ak sa uz minuli novi roboti a nikto nie je aktivny, skoncime
            if (robots.size() == 0 && !wasMove && !existReadyRobots()) {
                System.out.println("Nothing to do");
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
      //  robots.add(new Robot("eva"));
     //   robots.add(new Robot("walle"));

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
        currentTime.setText("Current time: " + this.time.toString());
        infoPane.getChildren().remove(currentTime);
        infoPane.getChildren().add(currentTime);
    //    gamePane.getChildren().addAll(world.robots);
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
        infoPane.getChildren().addAll(robotsMenu);
        pane.setRight(infoPane);
        pane.setLeft(gamePane);
        stage.setTitle("FlemmingZ");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * ci sa da este vyrobyt nejaky robot
     */
    boolean existReadyRobots(){
        for(ButtonRobot i: robotsMenu) if(i.getCount()>0) return true;
        return false;
    }


}
