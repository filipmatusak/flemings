package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Level;

public class StartupMenu {
    Main root;
    StartupMenu thiz;
    BorderPane pane;
    VBox vbox;
    Label label;
    Stage stage;
    LevelsMenu levelsMenu;
    int width;

    public StartupMenu(Main root){
        this.root = root;
        thiz = this;
        stage = new Stage();
        width = 250;
    }

    public void run(){

        pane = new BorderPane();
        pane.setPrefHeight(width);
        pane.setPrefWidth(width);
        pane.setId("pane");
        pane.getStylesheets().add("styles/styles.css");


        label = new Label("Welcome to FlemmingZ!!");
        pane.setCenter(label);

        Button level = new Button("CHOOSE LEVEL");
        level.setPrefWidth(width);
        level.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //toto mozno nebude vhodne tu - pripadne presunut do root a zavolat
                levelsMenu = new LevelsMenu(root);
                levelsMenu.run();
                stage.close();
            }
        });

        Button map = new Button("NEW WORLD");
        map.setPrefWidth(width);
        map.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.mapEditor.run();
                stage.close();
            }
        });
        Button load = new Button("LOAD SAVED");
        load.setPrefWidth(width);
        Button exit = new Button("QUIT GAME");
        exit.setPrefWidth(width);
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        vbox = new VBox();
        vbox.getChildren().addAll(level, map, load, exit);
        pane.setBottom(vbox);

        stage = new Stage();
        stage.setWidth(width);
        Scene scene = new Scene(pane);
        scene.getStylesheets().addAll("styles/styles.css");
        stage.setTitle("FlemmingZ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }
}
