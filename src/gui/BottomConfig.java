package gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class BottomConfig {
    public static void config(BorderPane root){
        root.setBottom(WarningText.getInstance());
    }
}
