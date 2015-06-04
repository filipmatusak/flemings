package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

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
                "-fx-background-color: red;"
                        + "-fx-text-fill: black;"
                        + "-fx-opacity: 0.1;"
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

    public static class MapSize{
        public static void setMainLabel(Label label){
            String labelStyle =
                    "-fx-text-fill: black;"
                            + "-fx-font-size: 20;"
                            + "-fx-font-weight: bold;"
                            + "-fx-effect: dropshadow( gaussian , derive(cadetblue, -20%) , 0,0,0,1 );"
                            + "-fx-font-family: 'Roboto';";
            label.setStyle(labelStyle);
        }
        public static void setLabel(Label label){
            String labelStyle =
                    "-fx-text-fill: black;"
                            + "-fx-font-size: 15;"
                            + "-fx-font-weight: bold;"
                            + "-fx-effect: dropshadow( gaussian , derive(cadetblue, -20%) , 0,0,0,1 );";
            label.setStyle(labelStyle);
        }
    }

    public static void setTextStyle(Label label){

        label.setStyle("-fx-text-fill: black;"+
                        "-fx-font-weight: bold;"+
                "-fx-font-family: Meiryo;"
        );
        label.setTextAlignment(TextAlignment.CENTER);
    }

    public static void setColor(Label label, Color col){
        if (col.equals(Color.RED)){
            label.setStyle("-fx-text-fill: red;");
        }
        else{
            label.setStyle("-fx-text-fill: green;");
        }
    }
}
