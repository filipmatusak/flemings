/** Pomocna trieda, ktora reprezentuje tlacidlo na pridavanie robotov do hry. Tlacidlo obsahuje obrazok robota, jeho
 * nazov, informaciu o tom, kolko robotov je este k dispozicii a po stlaceni zaradi prislusneho robota do fronty (aby
 * bol neskor pridany do hry)*/

package engine;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import robots.Robot;

/**
 * tlacidlo na pridavanie robotov
 */
public class ButtonRobot extends HBox {
    Main root;
    Label count;    /** label s informaciou o pocte robotov, ktori su este k dispozicii*/
    Label type;     /** label s nazvom robota */
    Robot robot;    /** robot bez funkcii (viditelny ako obrazok) */
    String style;
    HBox thiz;

    ButtonRobot(Robot robot, Main root, Integer limit){
        this.root = root;
        this.robot = robot;
        this.type = new Label (robot.getType());
        this.count = new Label(limit.toString());
        this.setPrefHeight(30);
        this.setPrefWidth(100);
        this.setStyle("-fx-padding: 5; -fx-background-color: #FFC;");
        this.setActions();
        this.thiz = this;
        this.getChildren().addAll(count, robot, type);
        robot.setOnMouseClicked(null);
    }

    // pozadie tlacidla sa meni podla toho, ci ponad tlacidlo prechadza mys
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
        // Pri kliknuti na tlacidlo prida robota do hry
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getCount() > 0){
                    try {
                        //vytvori noveho robora a prida ho do zoznamu
                        root.game.robots.add(robot.getClass().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //zmensi pocet dostupnych robotov
                    setCount(getCount() - 1);
                }
            }
        });
    }

    /** Aktualizuje informaciu o pocte dostupnych robotov */
    public void setCount(Integer value){
        this.getChildren().removeAll(count,robot,type);
        this.count.setText(value.toString());
        this.getChildren().addAll(count,robot,type);
    }

    /** Vrati pocet dostupnych robotov daneho typu */
    public Integer getCount() {
        return Integer.parseInt(this.count.getText());
    }

}
