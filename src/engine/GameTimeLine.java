/** Trieda reprezentujuce casovac, ktorym sa spusta nove kolo hry */

package engine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.TreeSet;

public class GameTimeLine {
    Main root;
    static Timeline timeline;
    static Double timePeriod;
    Duration duration;
    Integer actions;
    TreeSet<Integer> movingRobots;

    public GameTimeLine(Main root){
        this.root = root;
        timePeriod = 1000.0;    /** perioda, v akej sa vykresluju jednotlive animacie */
        duration = Duration.millis(timePeriod);
        timeline = new Timeline(new KeyFrame(duration, new Action()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        actions = 0;
        movingRobots = new TreeSet<>();
    }

    /** akcia vyvolana kazdu periodu */
    class Action implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //zavolame game.move(), ktora pohne vsetkymi robotmi v hre
            root.game.move();
        }
    }

    public void start(){
        timeline.play();
    }

    public void stop(){
        timeline.stop();
    }

    /** zmena periody o deltu v milisekundach */
    void changeTimePeriod(Double delta){
        timePeriod += delta;
        timePeriod = Double.max(timePeriod, 1);
        duration = Duration.millis(timePeriod);
        timeline.stop();
        timeline = new Timeline(new KeyFrame(duration, new Action()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pause(){ timeline.stop();}
    public void play(){ timeline.play();}

    /** Funkcia volana z hry na pozastavenie alebo pokracovanie v hre */
    public void change(){
        if (timeline.getStatus().equals(Animation.Status.PAUSED)){
            this.play();
        }
        else{
            this.pause();
        }
    }

    /** Vrati status hry (pozastavena alebo beziaca) */
    public static boolean isPaused(){
        return timeline.getStatus().equals(Animation.Status.PAUSED);
    }

    /** Vrati periodu, v akej su vykonavane akcie (rychlost animacii) */
    public static Double getPeriod(){ return timePeriod; }
}
