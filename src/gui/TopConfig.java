package gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopConfig {
    public static void config(BorderPane root, Stage primaryStage){
        VBox vBox = new VBox();
        MenuConfig.config(root, primaryStage, vBox);
        ToolConfig.config(vBox);
        vBox.setSpacing(10);
        root.setTop(vBox);
    }
}
