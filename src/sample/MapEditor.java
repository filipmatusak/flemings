package sample;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import squares.EmptySquare;
import squares.EntrySquare;
import squares.ExitSquare;
import squares.WallSquare;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * funkcie a okno MapEditora
 */

public class MapEditor {
    Main root;
    MapEditor thiz;
    ColoredRectangle[][] map;
    int height, width;
    BorderPane pane;
    Pane drawingPane;
    Paint selectedColor;
    Stage stage;
    SquareChoiceMenu squareChoiceMenu;

    public MapEditor(Main root){
        this.root = root;
        thiz = this;
        squareChoiceMenu = new SquareChoiceMenu(this);
    }

    public void run(){
        height = root.map.height;
        width = root.map.width;
        map = new ColoredRectangle[height][width];
        pane = new BorderPane();
        drawingPane = new Pane();
        stage = new Stage();
        selectedColor = null;

        pane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                squareChoiceMenu.show(event.getX() + stage.getX(),
                        event.getY() + stage.getY());
            }
        });

        pane.setOnMouseReleased(event -> {
            if (!event.isSecondaryButtonDown() && squareChoiceMenu != null) squareChoiceMenu.close();
        });

        initializeSquares();
        setSquares();

        Scene scene = new Scene(pane);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuClear = new MenuItem("Clear");
        MenuItem menuOpen = new MenuItem("Open");
        menu.getItems().add(menuOpen);
        menu.getItems().add(menuSave);
        menu.getItems().add(menuClear);
        menuBar.getMenus().add(menu);
        menuClear.setOnAction(event -> setClearMap());
        menuSave.setOnAction(event -> saveMap());
        menuOpen.setOnAction(event -> openMap());

        pane.setTop(menuBar);
        pane.setCenter(drawingPane);

        stage.setScene(scene);
        stage.show();

    }

    void saveMap(){
        try {
            isMapCorrect();
        } catch (EditorExeption e){
            ExceptionPrinter.print(e.getMessage());
            return;
        }
        File file = root.fileCreator.openFile(stage, true);
        try {
            if(file != null ) root.mapConvector.fromMapEditor(map, file);
        }
        catch (FileNotFoundException e) {
          //  e.printStackTrace();
        }
    }

    void openMap(){
        File file = root.fileCreator.openFile(stage, false);
        try{
            ColoredRectangle[][] m = root.mapConvector.toMapEditor(file);
            map = m;
            setSquares();
            refreshMap();
        } catch (Exception e) {
            ExceptionPrinter.print(new EditorExeption("Wrong format!").getMessage());
        }
    }

    boolean isMapCorrect() throws EditorExeption{
        //ci obsahuje 1 vstupne policko
        int in = 0;
        for (ColoredRectangle[] i : map) {
            for (ColoredRectangle j : i) if(j.getColor() == new EntrySquare().getColor()) in++;
        }
        if(in != 1) throw new EditorExeption("There must be 1 entry square!");

        //ci ma cielove policko
        boolean ok = false;
        for (ColoredRectangle[] i : map) {
            for (ColoredRectangle j : i) {
                if (j.getColor() == new ExitSquare().getColor()) {
                    ok = true;
                    break;
                }
            }
            if(ok) break;
        }
        if(!ok) throw new EditorExeption("There must be some exit square!");

        return true;
    }

    void initializeSquares(){
        pane.getChildren().clear();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ColoredRectangle r = new ColoredRectangle(j*root.map.squareSize, i*root.map.squareSize,
                        root.map.squareSize,root.map.squareSize);
                map[i][j] = r;
            }
            drawingPane.getChildren().addAll(map[i]);
        }
        setClearMap();
    }

    void setClearMap(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                map[i][j].setColor(new EmptySquare().getColor());
            }
        }
        for(int i = 0; i < height; i++){
            map[i][0].setColor(new WallSquare().getColor());
            map[i][width-1].setColor(new WallSquare().getColor());
        }
        for(int i = 0; i < width; i++){
            map[0][i].setColor(new WallSquare().getColor());
            map[height-1][i].setColor(new WallSquare().getColor());
        }
    }

    void refreshMap(){
        drawingPane.getChildren().clear();
        for(int i = 0; i < map.length; i++) drawingPane.getChildren().addAll(map[i]);
    }

    void setSquares(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ColoredRectangle r = map[i][j];
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(0.05);
                r.setOnMouseExited(event -> r.setFill(r.getColor()));
                r.setOnMouseMoved(event -> {
                    if (selectedColor != null) r.setFill(selectedColor);
                    if (event.isSecondaryButtonDown()) r.setBack();
                });
                r.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown() && selectedColor != null ) r.setColor(selectedColor);
                });
                r.setOnMouseDragged(event -> {
                    if(event.isSecondaryButtonDown()) return;
                    if (selectedColor != null) {
                        r.setFill(selectedColor);
                        if(event.isPrimaryButtonDown()) r.setColor(selectedColor);
                    }
                    Double a = event.getX(), b = event.getY();
                    Integer aa = a.intValue() / root.map.squareSize, bb = b.intValue() / root.map.squareSize;
                    if ( !event.isSecondaryButtonDown() && aa >= 0 && aa < width &&
                            bb >= 0 && bb < height &&
                            map[bb][aa] != r) {
                        r.setFill(r.getColor());
                        map[bb][aa].fireEvent(event);
                    }

                });
                r.setOnDragOver(event -> r.setFill(r.getColor()));
            }
        }
    }
}
