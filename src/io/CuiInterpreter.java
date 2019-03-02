package io;

import javafx.stage.Stage;
import model.Canvas;
import model.PaintPen;
import util.ImageUtil;
import util.StringUtil;
import java.util.Scanner;

public class CuiInterpreter {

    private static final String PROMPT = ">> ";
    private static boolean exitFlag = false;


    public static void run(){
        Scanner scanner = new Scanner(System.in);
        do {
            if (exitFlag)
                break;
            System.out.print(PROMPT);
            String command = scanner.nextLine();
            commandProcess(command);
        }while (true);
        System.exit(0);
    }

    public static void commandProcess(String command){
        String[] paras = command.split("\\s+");
        if (paras.length < 1){
            return;
        }
        switch (paras[0]){
            case "resetCanvas":
                if (paras.length != 3 || !StringUtil.isInteger(paras[1]) || !StringUtil.isInteger(paras[2])){
                    usage(paras[0]);
                    return;
                }
                doResetCanvas(Integer.valueOf(paras[1]), Integer.valueOf(paras[2]));
                break;
            case "saveCanvas":
                if (paras.length != 2){
                    usage(paras[0]);
                    return;
                }
                doSaveCanvas(paras[1]);
                break;
            case "setColor":
                if (paras.length != 4 || !StringUtil.isInteger(paras[1]) || !StringUtil.isInteger(paras[2]) || !StringUtil.isInteger(paras[3])){
                    usage(paras[0]);
                    return;
                }
                doSetColor(Integer.valueOf(paras[1]), Integer.valueOf(paras[2]), Integer.valueOf(paras[3]));
                break;
            case "drawLine":
                if (paras.length != 7 || !StringUtil.isInteger(paras[1]) || !StringUtil.isDouble(paras[2]) ||
                        !StringUtil.isDouble(paras[3]) || !StringUtil.isDouble(paras[4]) || !StringUtil.isDouble(paras[5])){
                    usage(paras[0]);
                    return;
                }
                doDrawLine(Integer.valueOf(paras[1]), Double.valueOf(paras[2]), Double.valueOf(paras[3]), Double.valueOf(paras[4]), Double.valueOf(paras[5]), paras[6]);
                break;
            case "exit":
            case "q":
                exitFlag = true;
                break;
            default:
                break;
        }
    }

    private static void usage(String command){
        switch (command){
            case "resetCanvas":
                System.out.println("Usage: " + command + " [width] [height], while 100 <= width, height <= 1000.");
                break;
            case "saveCanvas":
                System.out.println("Usage: " + command + " [filename].");
                break;
            case "setColor":
                System.out.println("Usage: " + command + " [R] [G] [B], while 0 <= R, G, B <= 255.");
                break;
            case "drawLine":
                System.out.println("Usage: " + command + " [id] [beginX] [beginY] [endX] [endY] [algorithm], while id is an integer and four x/y are doubles.");
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

    private static void doSetColor(int r, int g, int b){
        PaintPen.getInstance().setColor(r, g, b);
    }

    private static void doDrawLine(int id, double beginX, double beginY, double endX, double endY, String algorithm){
        Canvas.getInstance().drawLine(id, beginX, beginY, endX, endY, algorithm);
        ImageUtil.canvasUpdate();
    }
}
