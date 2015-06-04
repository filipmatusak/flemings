package engine;

import javafx.scene.paint.Paint;
import robots.Robot;
import squares.Square;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MapConvertor {

    Main root;
    public MapConvertor(Main root){
        this.root = root;
    }

    public Map fileToMap(File file) throws FileNotFoundException {
        try{
            Scanner in = new Scanner(new FileReader(file));
            int r, c;
            r = in.nextInt();
            c = in.nextInt();
            Map map = new Map(root, r, c);
            String s;

            s = in.nextLine();
            for(int i = 0; i < r; i++){
                s = in.nextLine();
                for(int j = 0; j < c; j++){
                    Square square = getSquare(s.charAt(j));
                    square.setSize(root.map.getSquareSize());
                    square.setY(root.map.getSquareSize()*i);
                    square.setX(root.map.getSquareSize()*j);
               //     System.out.println("s> "+ square.getX() + " " + square.getColor().toString());
                    map.map[i][j] = square;
                }
            }

            ArrayList<Integer> limits = new ArrayList<>();
            for(Robot robot: AllRobots.getTypes()){
                Integer x = in.nextInt();
                if(x > Map.getMaxRobotLimit() || x < 0) throw new EditorExeption("");
                limits.add(x);
            }

            Integer target = in.nextInt();
            map.setLimits(limits);
            map.setTarget(target);

            return map;
        } catch (EditorExeption e) {
            throw new EditorExeption("Wrong robot limit");
        } catch (NullPointerException e) {
            throw new EditorExeption("No file");
        } catch (Exception e){
            throw new EditorExeption("Wrong format");
        }
    }

    /**ulozi mapu do suboru*/
    public void fromMapEditor(ColoredRectangle[][] map, ArrayList<Integer> limits, Integer target, File file)
            throws FileNotFoundException {

        PrintWriter out = new PrintWriter(file);
        out.println(map.length + " " + map[0].length);
        for (ColoredRectangle[] i : map) {
            for (ColoredRectangle j : i) {
                out.print(getSquare(j.getColor()).toString());
            }
            out.println();
        }
        for(Integer i: limits) out.print(i + " ");
        out.print(target);
        out.close();
    }

    /**pripravy mapu zo suboru pre editor*/
    public class ToMapEditorResult{
        ColoredRectangle[][] map;
        ArrayList<Integer> limits;
        Integer target;
        public ToMapEditorResult(ColoredRectangle[][] map,
                                 ArrayList<Integer> limits,
                                 Integer target){
            this.map = map;
            this.limits = limits;
            this.target = target;
        }
    }
    public ToMapEditorResult toMapEditor(File file) throws FileNotFoundException {
        try {
            Scanner in = new Scanner(new FileReader(file));
            int r, c;
            r = in.nextInt();
            c = in.nextInt();
            ColoredRectangle[][] map = new ColoredRectangle[r][c];
            String s;
            s = in.nextLine();
            for (int i = 0; i < r; i++) {
                s = in.nextLine();
                for (int j = 0; j < c; j++) {
                    map[i][j] = new ColoredRectangle(j * root.map.squareSize, i * root.map.squareSize,
                            root.map.squareSize, root.map.squareSize);
                    map[i][j].setColor(getSquare(s.charAt(j)).getColor());
                }
            }

            ArrayList<Integer> limits = new ArrayList<>();
            for(Robot robot: AllRobots.getTypes()){
                Integer x = in.nextInt();
                if(x > Map.getMaxRobotLimit() || x < 0) throw new EditorExeption("");
                limits.add(x);
            }

            Integer target = in.nextInt();

            return new ToMapEditorResult(map, limits, target);
        } catch (EditorExeption e){
            throw new EditorExeption("Wrong robot limit");
        } catch (Exception e){
            throw new EditorExeption("Wrong format");
        }
    }

    /**podla farby zisti typ stvorca*/
    Square getSquare(Paint c){
       for(Square i: new AllSquares().getTypes()){
            if(i.getColor() == c) return i;
        }
        return null;
    }

    /**podla znakovej reprezentacie stvorca zisti typ stvorca*/
    Square getSquare(Character c){
        for(Square i: new AllSquares().getTypes()){
            if(Objects.equals(i.toString(), c.toString())){
                return i;
            }
        }
        return null;
    }
}
