/** Trieda na pracu so subormi */

package engine;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileCreator {
    Main root;

    public FileCreator(Main root){
        this.root = root;
    }

    /** Funkcia vytvori dialog na otvorenie alebo ulozenie suboru */
    public File openFile(Window window, Boolean save){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(new File("src/maps"));
        File file;
        if(save) file = fileChooser.showSaveDialog(window);
        else file = fileChooser.showOpenDialog(window);
        return file;
    }

}
