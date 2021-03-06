package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.TreeSet;

/**
 * Casovas, ktorym spusta nove kolo hry
 */
public class GameTimeLine {
    Main root;
    Timeline timeline;
    static Double timePeriod;
    Duration duration;
    Integer actions;
    TreeSet<Integer> movingRobots;


    public GameTimeLine(Main root){
        this.root = root;
        timePeriod = 1000.0;
        duration = Duration.millis(timePeriod);
        timeline = new Timeline(new KeyFrame(duration, new Action()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        actions = 0;
        movingRobots = new TreeSet<>();
    }

    /**
     * akcia vyvolana kazdu periodu
     */
    class Action implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("actions: " + actions);
            root.game.move();
        }
    }

    public void start(){
        timeline.play();
    }

    public void stop(){
        timeline.stop();
    }

    /**
     * zmena periody o deltu v milisekundach
     */
    void changeTimePeriod(Double delta){
        timePeriod += delta;
        timePeriod = Double.max(timePeriod, 1);
        duration = Duration.millis(timePeriod);
        timeline.stop();
        timeline = new Timeline(new KeyFrame(duration, new Action()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void faster(){
        changeTimePeriod(-10.0);
    }
    public void slower(){
        changeTimePeriod(10.0);
    }
    public void pause(){ timeline.stop();}
    public void play(){ timeline.play();}

    public static Double getPeriod(){ return timePeriod; }
}
