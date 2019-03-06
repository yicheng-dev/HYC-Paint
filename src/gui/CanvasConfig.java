package gui;

import javafx.scene.layout.BorderPane;

public class CanvasConfig {
    public static void config(BorderPane root){
        String cssBordering = "-fx-border-color:black ; \n" //#090a0c
                + "-fx-border-width:5.0";

        BorderPane p = new BorderPane();
        p.setCenter(CanvasView.getInstance());
        p.setMaxHeight(CanvasView.getInstance().getImage().getHeight());
        p.setMaxWidth(CanvasView.getInstance().getImage().getWidth());
        p.setStyle(cssBordering);
        root.setCenter(p);
    }
}
