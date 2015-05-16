package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class GameTimeLine {
    Main root;
    Timeline timeline;
    Double timePerior;
    Duration duration;

    public GameTimeLine(Main root){
        this.root = root;
        timePerior = 1000.0;

        duration = Duration.millis(timePerior);
        timeline = new Timeline(new KeyFrame(duration, new Action()));

        timeline.setCycleCount(Timeline.INDEFINITE);


    }

    class Action implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("mal by ist, cas: " + duration);
            root.game.move();
        }
    }

    public void start(){
        timeline.play();
    }

    public void stop(){
        timeline.stop();
    }

    void changeTimePeriod(Double d){
        timePerior += d;
        timePerior = Double.max(timePerior, 1);
        duration = Duration.millis(timePerior);
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


}
