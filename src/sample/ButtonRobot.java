package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import robots.Robot;

/**
 * tlacidlo na pridavanie robotov
 */
public class ButtonRobot extends HBox {
    Main root;
    Label count;
    Label type;
    Robot robot;
    String style;
    HBox thiz;

    ButtonRobot(Robot robot, Main root, Integer limit){
        this.root = root;
        this.robot = robot;
        this.type = new Label (robot.getType());
      //  this.count = new Label (robot.getLimit().toString());
        this.count = new Label(limit.toString());
        this.setPrefHeight(30);
        this.setPrefWidth(100);
        this.setStyle("-fx-padding: 5; -fx-background-color: #FFC;");
        this.setActions();
        this.thiz = this;
        this.getChildren().addAll(count, robot, type);
    }

    public void setActions(){
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thiz.setStyle("-fx-padding: 5; -fx-background-color: white;");
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thiz.setStyle("-fx-padding: 5; -fx-background-color: #FFC;");
            }
        });
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getCount() > 0){
                    /** TODO:Pridat robota do hry */
                    try {
                        root.game.robots.add(robot.getClass().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    setCount(getCount() - 1);
                }
            }
        });
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if ((getCount() > 0) && robot.getShortcut() == event.getCode()){
                    /** TODO:Pridat robota do hry */
                    setCount(getCount() - 1);
                }
            }
        });
    }

    public void setCount(Integer value){
        this.getChildren().removeAll(count,robot,type);
        this.count.setText(value.toString());
        this.getChildren().addAll(count,robot,type);
    }

    public Integer getCount() {
        return Integer.parseInt(this.count.getText());
    }
}
