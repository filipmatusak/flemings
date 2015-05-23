package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Style {
    public static void setButtonStyle(Button btn) {
        String buttonStyle =
                "-fx-background-color: #090a0c,"
                        + "linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),"
                        + "linear-gradient(#20262b, #191d22),"
                        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
                        + "-fx-text-fill: white;"
                        + "-fx-font-family: 'Arial';";

        String buttonFocusStyle =
                "-fx-background-color: black;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-family: 'Arial';";

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

    public static void setLevelButtonStyle(Button btn){
        String buttonStyle =
                "-fx-background-color: white;"
                        + "-fx-text-fill: black;"
                        + "-fx-opacity: 0.3;"
                        + "-fx-font-family: 'Arial';";

        String buttonFocusStyle =
                "-fx-background-color: black;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-family: 'Arial';";

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
