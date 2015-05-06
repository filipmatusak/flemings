package sample;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * praca so subormi
 */

public class FileCreator {
    Main root;

    public FileCreator(Main root){
        this.root = root;
    }

    public File openFile(Window window){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
    //    fileChooser.setInitialDirectory(new File("src/saves"));
        File file;
        file = fileChooser.showSaveDialog(window);
        return file;
    }

}