package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.ArrayList;

public class LevelMenu {
    Main root;
    Integer row = 5, col = 3, space = 20, size = 50;
    Stage stage;
    Scene scene;

    public LevelMenu(Main root){
        this.root = root;
    }

    public void chooseLevel(){
        ArrayList<Button> all = new ArrayList<>();

        GridPane grid = new GridPane();
        grid.setHgap(space);
        grid.setVgap(space);
        grid.setPadding(new Insets(space, space, space, space));
        scene = new Scene(grid,  space*(1+col) + col*size, space*(1+row) + row*size);
        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Image image = new Image(getClass().getResourceAsStream("../graphics/robots/eva.png"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        grid.setBackground(background);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                root.startup.show();
            }
        });


        File folder = new File("src/maps/levels");
   //     System.out.println(folder.getAbsolutePath() + " " + folder.exists() + "\n");
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                Button b = new Button();
                b.setId(fileEntry.getAbsolutePath());
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button b = event.getSource() instanceof Button ? ((Button) event.getSource()) : null;
                        assert b != null;
                        String levelPath = b.getId();
                        try {
                            File level = fileEntry;
                            root.levelFile = level;
                            Map map = root.mapConvertor.fileToMap(new File(levelPath));
                            root.game = new Game(root, map);
                            stage.close();
                            root.game.run();
                        } catch ( Exception e) {
                            ExceptionPrinter.print(e.getMessage());
                            //e.printStackTrace();
                        }

                    }
                });
                Style.setLevelButtonStyle(b);
                all.add(b);
            }
        }
        for(Integer i = 0; i < all.size(); i++){
            int r = i/col;
            int c = i%col;
            Button p = all.get(i);
            p.setPrefWidth(size);
            p.setPrefHeight(size);
            grid.add(p, c, r);
            p.setText(Integer.toString(i + 1));
            p.setPrefSize(size, size);

        }
    }
}
