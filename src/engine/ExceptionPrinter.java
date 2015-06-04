/** Trieda vypise chybove hlasenie so zadanou spravou */

package engine;

import javafx.scene.control.Alert;

public class ExceptionPrinter {
    public static void print(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(text);
        alert.show();
    }

}
