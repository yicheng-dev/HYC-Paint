package main;

import io.CuiInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Canvas;
import model.CanvasView;
import util.ImageUtil;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HYC-Paint");
        BorderPane root = new BorderPane();
        ImageUtil.canvasUpdate();
        root.setCenter(CanvasView.getInstance());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        CuiInterpreter.run();
    }
}
