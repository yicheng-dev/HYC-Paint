package main;

import io.CuiInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.CanvasView;
import util.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //redirectErr("err.txt");
        primaryStage.setTitle("HYC-Paint");
        BorderPane root = new BorderPane();
        ImageUtil.canvasUpdate();
        String cssBordering = "-fx-border-color:black ; \n" //#090a0c
                + "-fx-border-width:5.0";

        BorderPane p = new BorderPane();
        p.setCenter(CanvasView.getInstance());
        p.setMaxHeight(CanvasView.getInstance().getImage().getHeight());
        p.setMaxWidth(CanvasView.getInstance().getImage().getWidth());
        p.setStyle(cssBordering);
        root.setCenter(p);
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.show();
        Thread cuiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CuiInterpreter.run();
            }
        });
        cuiThread.start();
    }

    private void redirectErr(String filename){
        File file = new File(filename);
        try {
            if (file.exists())
                file.delete();
            file.createNewFile();
            System.setErr(new PrintStream(filename));
        }catch (IOException e){
            System.out.println("Err File not found.");
        }
    }
}
