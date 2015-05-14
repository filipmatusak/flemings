package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * okno pre vyber levelu
 * treba dorobit, co presne sa stane, ked kliknes na konkretny level - mal by sa nacitat svet podla suboru, ktory je v nom
 */
public class LevelsMenu {
    Main root;
    LevelsMenu thiz;
    AllLevels levels;
    BorderPane pane;
    FlowPane flowpane;
    Label label;
    Stage stage;
    int width;

    public LevelsMenu(Main root){
        this.root = root;
        thiz = this;
        stage = new Stage();
        width = 250;
    }

    //Funkcia nacita level s cestou src do hlavneho okna
    public void loadLevel (String src){

    }
    public void run(){

        pane = new BorderPane();
        pane.setPrefHeight(width);
        pane.setPrefWidth(width);
    //    pane.setId("pane");
    //    pane.getStylesheets().add("styles/styles.css");

        label = new Label("Choose a level from the given options:");
        pane.setCenter(label);

        flowpane = new FlowPane();

        //vypyta si zoznam vsetkych levelov a pre kazdy z nich vytvori tlacidlo s jeho nazvom a prepojenim na ten level
        levels = new AllLevels();
        ArrayList<String> levelsList = levels.getLevels();
        Button[] buttonsLevels = new Button[levelsList.size()];

        int i=0;
        for (Iterator it = levelsList.iterator(); it.hasNext(); i++){
            String src = (String) it.next();
            buttonsLevels[i] = new Button(src);
            buttonsLevels[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadLevel(src);
                }
            });
            flowpane.getChildren().add(buttonsLevels[i]);
        }

        pane.setBottom(flowpane);

        stage = new Stage();
        stage.setWidth(width);
        Scene scene = new Scene(pane);
    //    scene.getStylesheets().addAll("styles/styles.css");
        stage.setTitle("FlemmingZ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }
}
