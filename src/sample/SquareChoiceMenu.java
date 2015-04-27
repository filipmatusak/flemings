package sample;

public class SquareChoiceMenu {

}

/*
public class SquareChoiceMenu {
    Stage stage;
    ArrayList<Square> allTypes = new ArrayList<>();
    ArrayList<Rectangle> allRectangles = new ArrayList<>();
    Pane pane;
    public Scene scene;
    Parent root;
    Double posX=50.0, posY=50.0;
    int row, col;
    int size = 40, space = 10;
    Square selected = null;

    public SquareChoiceMenu(Parent parent) throws IOException {
        root = parent;
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        initializeTypes();
        pane = new Pane();
        col = 3;
        row = (allTypes.size()+1)/3;
        scene = new Scene(pane, col*size + (col+1)*space, row*size + (row+1)*space);
        stage.setScene(scene);
        printSquares();
    }

    void initializeTypes(){
        allTypes.add(new EmptySquare());
        allTypes.add(new GlueSquare());
        allTypes.add(new SoilSquare());
        allTypes.add(new EntrySquare());
        allTypes.add(new ExitSquare());
        allTypes.add(new WallSquare());
        for(int i = 0; i < allTypes.size(); i++){
            Rectangle s = new Rectangle(size,size,allTypes.get(i).color);
            int r = i/3;
            int c = i%3;
            s.setX(space * (c + 1) + c * size);
            s.setY(space * (r + 1) + r * size);
            allRectangles.add(s);
            Square sq = allTypes.get(i);
            s.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    SendSelected sendSelected = new SendSelected(null);
                    sendSelected.selected = selected;
                    root.fireEvent(sendSelected);
                }
            });
            s.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    s.setStroke(Color.AQUA);
                    s.setStrokeWidth(5);
                    selected = sq;
                    event.copyFor(event.getSource(),root);
                }

            });
            s.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    s.setStroke(null);
                    selected = null;
                    event.clone();
                    event.copyFor(event.getSource(), root);
                }
            });
        }
    }

    public void setVisible(Boolean b){
        if(b) stage.show();
        else stage.close();
    }

    void printSquares(){
        for(int i = 0; i < allTypes.size(); i++){
            pane.getChildren().add(allRectangles.get(i));
        }
    }

    public Square getSelected(){
        return selected;
    }

    public void setPosition(Double x, Double y){
        posX = x;
        posY = y;
        stage.setX(posX);
        stage.setY(posY);
    }

}*/