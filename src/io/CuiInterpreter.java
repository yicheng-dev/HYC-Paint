package io;

import model.Canvas;
import model.CanvasView;
import util.ImageUtil;
import util.StringUtil;

import java.awt.*;
import java.util.Scanner;
import java.util.Vector;

public class CuiInterpreter {

    private static final String PROMPT = ">> ";
    private static boolean exitFlag = false;


    public static void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                do {
                    System.out.print(PROMPT);
                    String command = scanner.nextLine();
                    commandProcess(command);
                }while (true);
            }
        }).start();
    }

    public static void commandProcess(String command){
        String[] paras = command.split("\\s+");
        if (paras.length < 1){
            return;
        }
        switch (paras[0]){
            case "resetCanvas":
            case "reset_canvas":
                if (paras.length != 3 || !StringUtil.isInteger(paras[1]) || !StringUtil.isInteger(paras[2])){
                    usage(paras[0]);
                    return;
                }
                doResetCanvas(Integer.valueOf(paras[1]), Integer.valueOf(paras[2]));
                break;
            case "saveCanvas":
            case "save_canvas":
                if (paras.length != 2){
                    usage(paras[0]);
                    return;
                }
                doSaveCanvas(paras[1]);
                break;
            default:
                break;
        }
    }

    private static void usage(String command){
        switch (command){
            case "resetCanvas":
            case "reset_canvas":
                System.out.println("Usage: " + command + " [width] [height], while 100 <= width, height <= 1000.");
                break;
            case "saveCanvas":
            case "save_canvas":
                System.out.println("Usage: " + command + " [filename].");
                break;
            default:
                break;
        }
    }

    private static void doSaveCanvas(String filename){
        Canvas.getInstance().saveCanvas(filename);
    }

    private static void doResetCanvas(int width, int height){
        Canvas.getInstance().resetCanvas(width, height);
        ImageUtil.canvasUpdate();
    }
}
