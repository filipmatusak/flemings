package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import robots.Robot;
import squares.EmptySquare;
import squares.EntrySquare;
import squares.ExitSquare;
import squares.WallSquare;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * funkcie a okno MapEditora
 */

public class MapEditor {
    Main root;
    MapEditor thiz;
    ColoredRectangle[][] map;
    Integer height, width;
    BorderPane pane;
    Pane drawingPane;
    Paint selectedColor;
    Stage stage;
    Map m;
    Boolean close;
    Scene scene;
    Settings settings;
    SquareChoiceMenu squareChoiceMenu;

    public MapEditor(Main root){
        this.root = root;
        thiz = this;
        squareChoiceMenu = new SquareChoiceMenu(this);
    }

    public void run(){
        getMapSize();
        if(close){
            root.startup.show();
            return;
        }
        m = new Map(root, height, width);
        map = new ColoredRectangle[height][width];
        open();
        setClearMap();
    }

    void open(){
        if(stage!=null) stage.close();
        height = m.height;
        width = m.width;
        pane = new BorderPane();
        drawingPane = new Pane();
        stage = new Stage();
        selectedColor = null;

        scene = new Scene(pane);
        settings = new Settings();

        initializeSquares();
        setSquares();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        Menu edit = new Menu("Edit");
        MenuItem menuPlay = new MenuItem("Play");
        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuClear = new MenuItem("Clear");
        MenuItem menuOpen = new MenuItem("Open");
        MenuItem menuExit = new MenuItem("Exit");
        MenuItem menuRobots = new MenuItem("Robot Setting");
        menu.getItems().addAll(menuPlay, menuOpen, menuSave, menuExit);
        edit.getItems().addAll(menuClear, menuRobots);
        menuBar.getMenus().addAll(menu, edit);
        menuPlay.setOnAction(event1 -> playThis());
        menuClear.setOnAction(event -> setClearMap());
        menuSave.setOnAction(event -> saveMap());
        menuOpen.setOnAction(event -> openMap());
        menuExit.setOnAction(event -> exit());
        menuRobots.setOnAction(event -> settings.show());
        pane.setTop(menuBar);
        pane.setCenter(drawingPane);

        stage.setResizable(false);

        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                root.startup.show();
            }
        });

        pane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                squareChoiceMenu.show(event.getX() + stage.getX(),
                        event.getY() + stage.getY());
            }
        });

        pane.setOnMouseReleased(event -> {
            if (!event.isSecondaryButtonDown() && squareChoiceMenu != null) squareChoiceMenu.close();
        });
        stage.show();
    }

    class Settings extends Stage{

        ArrayList<Spinner<Integer>> spinners;
        Spinner<Integer> targetSpinner;



        public Settings(){
            spinners = new ArrayList<>();
            GridPane pane = new GridPane();

            Double space = 10.0;
            pane.setHgap(space);
            pane.setVgap(space);
            pane.setPadding(new Insets(space, space, space, space));

            pane.add(new Label("Robots"), 1,1);

            ArrayList<Robot> robots = AllRobots.getTypes();

            for(int i = 0; i < robots.size(); i++){
                Robot robot = robots.get(i);
                robot.setOnMouseClicked(null);
                Label label = new Label(robot.getType());
                Spinner<Integer> spinner = new Spinner<Integer>(0, 100, 5);
                spinner.setPrefWidth(77);
                spinners.add(spinner);

                pane.add(label, 1, 2+i);
                pane.add(robot, 2, 2+i);
                pane.add(spinner, 3, 2 + i);

            }

            Label target = new Label("Target");
            targetSpinner = new Spinner<Integer>(0,1000/*getSum()*/,5);

            targetSpinner.setPrefWidth(77);

            pane.add(target, 6, 2);
            pane.add(targetSpinner, 6, 3);

            Scene scene = new Scene(pane);
            this.setScene(scene);
        }

        void refresh(ArrayList<Integer> limits,Integer target ){
            for(int i = 0; i < limits.size(); i++){
                Spinner<Integer> spinner = spinners.get(i);
                Integer a = limits.get(i);
                Integer b = spinner.getValue();
                if( a < b ) spinner.decrement(b - a);
                else spinner.increment(a-b);
            }
            Spinner<Integer> spinner = targetSpinner;
            Integer a = target;
            Integer b = spinner.getValue();
            if( a < b ) spinner.decrement(b-a);
            else spinner.increment(a-b);
        }

        Integer getSum(){
            Integer sum = 0;
            for(Integer i: getRobotsLimits()) sum += i;
            return sum;
        }

        ArrayList<Integer> getRobotsLimits(){
            ArrayList<Integer> list = new ArrayList<>();
            for(Spinner<Integer> spinner: spinners) list.add(spinner.getValue());
            return list;
        }

        Integer getTarget(){ return targetSpinner.getValue(); }

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
            if(file != null ) root.mapConvertor
                    .fromMapEditor(map, settings.getRobotsLimits(), settings.getTarget(), file);
        }
        catch (FileNotFoundException e) {
          //  e.printStackTrace();
        }
    }

    void playThis(){
        stage.close();
        try {
            root.game.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void exit(){
        root.startup.show();
        stage.close();
    }

    void openMap(){
        File file = root.fileCreator.openFile(stage, false);
        try{
            MapConvertor.ToMapEditorResult result = root.mapConvertor.toMapEditor(file);
            map = result.map;
            m = new Map(root, map.length, map[0].length);
            open();
            setSquares();
            refreshMap();
            settings.refresh(result.limits, result.target);
        } catch (Exception e) {
            ExceptionPrinter.print(e.getMessage());
        }
    }

    /**kontroly pre vyrobenu mapu*/
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

    /**vyrobi policka pre map*/
    void initializeSquares(){
        pane.getChildren().clear();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ColoredRectangle r;
                if(map[i][j]==null) r = new ColoredRectangle(j*m.squareSize, i*m.squareSize,
                        m.squareSize,m.squareSize);
                else r = map[i][j];
                map[i][j] = r;
            }
            drawingPane.getChildren().addAll(map[i]);
        }
    }

    /**vyrobu prazdnu mapu s okrajovymi polickami*/
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

    /**zobrazi mapu z map do editora*/
    void refreshMap(){
        drawingPane.getChildren().clear();
        for (ColoredRectangle[] aMap : map) drawingPane.getChildren().addAll(aMap);
    }

    /**nastavy listenery a okraje pre stvorce*/
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
                    Integer aa = a.intValue() / m.squareSize, bb = b.intValue() / m.squareSize;
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

    void getMapSize(){
        close = false;
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("New Map");
        dialog.setHeaderText("Set size of map");
        Integer maxW = 60, maxH = 30, minW = 5, minH = 5;

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField heightField = new TextField();
        heightField.setPromptText(+minH + " - " + maxH);
        TextField widthField = new TextField();
        widthField.setPromptText(minW + " - " + maxW);

        grid.add(new Label("Height:"), 0, 0);
        grid.add(heightField, 1, 0);
        grid.add(new Label("Width:"), 0, 1);
        grid.add(widthField, 1, 1);

        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        class MyTextListener implements ChangeListener<String> {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Objects.equals(heightField.getText(), "") || Objects.equals(widthField.getText(), "")) return;
                Integer h, w;
                try {
                    h = Integer.parseInt(heightField.getText());
                    w = Integer.parseInt(widthField.getText());
                    createButton.setDisable(newValue.trim().isEmpty() || h < minH || h > maxH || w < minW || w > maxW);
                } catch (Exception e){
                    createButton.setDisable(true);
                }
            }
        }

        heightField.textProperty().addListener(new MyTextListener());
        widthField.textProperty().addListener(new MyTextListener());

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Pair<>(Integer.parseInt(heightField.getText()), Integer.parseInt(widthField.getText()));
            }
            if (dialogButton == ButtonType.CANCEL) {
                close = true;
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(size -> {
            height = size.getKey();
            width = size.getValue();
        });


    }


}
