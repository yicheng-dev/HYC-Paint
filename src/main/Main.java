package main;

import gui.*;
import io.CliInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public class Main extends Application {

    public static void main(String[] args) {
        if (args.length > 0){
            if (!parseArg(args))
                return;
            GP.CLI = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CliInterpreter.run();
                }
            }).start();
        }else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //redirectErr("err.txt");
        primaryStage.setTitle("HYC-Paint");
        BorderPane root = new BorderPane();
        ImageUtil.canvasUpdate();
        TopConfig.config(root, primaryStage);
        CanvasConfig.config(root);
        BottomConfig.config(root);
        LeftConfig.config(root);
        primaryStage.setScene(new Scene(root, GP.ROOT_WIDTH, GP.ROOT_HEIGHT));
        primaryStage.show();
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
        if (args.length == 1 && !args[0].equals("-c")) {
            System.out.println("Command-Line arguments should be: \"-c\" or \"[input file] [output dir].\"");
            return false;
        }
        else if (args.length == 1) {
            GP.STDIN = true;
            return true;
        }
        if (args.length >= 3) {
            System.out.println("Command-Line arguments should be: \"-c\" or \"[input file] [output dir].\"");
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
