package sample;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
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
        setButtonStyle(level);
        level.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                root.level.chooseLevel();
            }
        });

        Button map = new Button("NEW WORLD");
        map.setPrefWidth(width);
        setButtonStyle(map);
        map.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                root.mapEditor.run();
            }
        });
        Button load = new Button("LOAD SAVED");
        load.setPrefWidth(width);
        setButtonStyle(load);
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
                    ExceptionPrinter.print("Illegal Operation!");
                }
            }
        });


        Button exit = new Button("QUIT GAME");
        exit.setPrefWidth(width);
        setButtonStyle(exit);
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

    /** Funkcia nastavi tlacidlu styl - zakladny a pri prechode mysou */
    public void setButtonStyle(Button btn){
        String buttonStyle =
                "-fx-background-color: #090a0c,"
                        +"linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),"
                        +"linear-gradient(#20262b, #191d22),"
                        +"radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
                        +"-fx-text-fill: white;"
                        +"-fx-font-family: 'Arial';";

        String buttonFocusStyle =
                "-fx-background-color: black;"
               +"-fx-text-fill: white;"
               +"-fx-font-family: 'Arial';";

        btn.setStyle(buttonStyle);
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle(null);
                btn.setStyle(buttonFocusStyle);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle(null);
                btn.setStyle(buttonStyle);
            }
        });
    }
}
