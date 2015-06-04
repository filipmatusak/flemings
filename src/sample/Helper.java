package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import robots.Robot;
import squares.Square;

public class Helper extends Stage {
    Stage thiz;

    public Helper(){
        thiz = this;
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20, 150, 10, 10));
        Scene scene = new Scene(pane);

        Image image = new Image(getClass().getResourceAsStream("../graphics/helper.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UNDECORATED);

        Integer i = 0;
        for(Square square: AllSquares.getTypes()){
            square.setSize(30);
            pane.add(square, 0, i);
            Label label = new Label(square.getType());
            Style.setTextStyle(label);
            pane.add(label, 1, i);
            i++;
        }
        for(Robot robot: AllRobots.getTypes()){
            pane.add(robot, 0, i);
            Label label = new Label(robot.getType());
            Style.setTextStyle(label);
            pane.add(label, 1, i);
            i++;
        }
        Button ok = new Button("Thnx");
        Style.setButtonStyle(ok);
        pane.add(ok, 2, i - 1);

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                thiz.close();
            }
        });

        this.setResizable(false);
        this.setScene(scene);
    }
}
