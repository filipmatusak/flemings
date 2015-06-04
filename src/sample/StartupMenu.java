package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class StartupMenu {
    Main root;
    StartupMenu thiz;
    BorderPane pane;
    VBox vbox;
    Label label;
    Stage stage;
    int width;

    public StartupMenu(Main root){
        this.root = root;
        thiz = this;
        stage = new Stage();
        width = 250;
    }

    public final void show() {
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    public void run(){

        pane = new BorderPane();
        pane.setPrefHeight(width);
        pane.setPrefWidth(width);

        //nastavenie pozadia
        Image image = new Image(getClass().getResourceAsStream("../graphics/robots/eva.png"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        Button level = new Button("CHOOSE LEVEL");
        level.setPrefWidth(width);
        Style.setButtonStyle(level);
        level.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                root.level.chooseLevel();
            }
        });

        Button map = new Button("NEW WORLD");
        map.setPrefWidth(width);
        Style.setButtonStyle(map);
        map.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                root.mapEditor.run();
            }
        });
        Button load = new Button("LOAD SAVED");
        load.setPrefWidth(width);
        Style.setButtonStyle(load);
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = root.fileCreator.openFile(stage, false);
                try {
                    Map map = root.mapConvertor.fileToMap(file);
                    root.game = new Game(root, map);
                    stage.close();
                    root.game.run();
                } catch (Exception e){
//                    ExceptionPrinter.print("Illegal Operation!");
                }
            }
        });


        Button exit = new Button("QUIT GAME");
        exit.setPrefWidth(width);
        Style.setButtonStyle(exit);
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
        stage.setTitle("FlemmingZ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }
}
