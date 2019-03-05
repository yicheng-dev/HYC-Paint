package main;

import gui.MenuConfig;
import io.CliInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.CanvasView;
import util.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public class Main extends Application {

    public static void main(String[] args) {
        if (GP.CLI){
            if (!parseArg(args))
                return;
        }
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
        MenuBar menuBar = new MenuBar();
        MenuConfig.config(menuBar, primaryStage);
        p.setCenter(CanvasView.getInstance());
        p.setMaxHeight(CanvasView.getInstance().getImage().getHeight());
        p.setMaxWidth(CanvasView.getInstance().getImage().getWidth());
        p.setStyle(cssBordering);
        root.setTop(menuBar);
        root.setCenter(p);
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.show();
        if (GP.CLI) {
            Thread cuiThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CliInterpreter.run();
                }
            });
            cuiThread.start();
        }
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

    private static boolean parseArg(String[] args){
        if (args.length != 2) {
            System.out.println("Command-Line arguments should be: Path of input file and Path of output directory.");
            return false;
        }
        File inputFile = new File(args[0]);
        if (!inputFile.exists()){
            System.out.println("Input file not found.");
            return false;
        }
        File outputDir = new File(args[1]);
        if (!outputDir.exists()){
            System.out.println("Output dir not found. Create it for you.");
            try {
                outputDir.createNewFile();
            }catch (IOException e){
                System.out.println("Output dir creation failed.");
                return false;
            }
        }
        GP.inputInstrFile = args[0];
        GP.outputDir = args[1];
        return true;
    }
}
