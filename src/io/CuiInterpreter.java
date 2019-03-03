package io;

import javafx.stage.Stage;
import main.GP;
import model.Canvas;
import model.PaintPen;
import model.Point;
import util.ImageUtil;
import util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

public class CuiInterpreter {

    private static final String PROMPT = ">> ";
    private static boolean exitFlag = false;


    public static void run(){
        try {
            InputStream inputStream = new FileInputStream(new File("input.txt"));
            if (GP.STDIN)
                inputStream = System.in;
            Scanner scanner = new Scanner(inputStream);
            do {
                if (exitFlag)
                    break;
                if (GP.STDIN)
                    System.out.print(PROMPT);
                String command = scanner.nextLine();
                commandProcess(command);
            } while (true);
            System.exit(0);
        }catch (IOException e){
            System.out.println("Input file not found.");
        }
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
            case "drawCurve":
            case "drawPolygon":
                if (paras.length <= 5 || !StringUtil.isInteger(paras[1]) || !StringUtil.isInteger(paras[2]) ||
                    !(Integer.valueOf(paras[2]) * 2 + 4 == paras.length)){
                    usage(paras[0]);
                    return;
                }
                int n = Integer.valueOf(paras[2]);
                for (int i = 4; i <= 3 + 2 * n; i ++){
                    if (!StringUtil.isDouble(paras[i])){
                        usage(paras[0]);
                        return;
                    }
                }
                Vector<Point> points = new Vector<>();
                for (int i = 4; i <= 3 + 2 * n; i += 2){
                    points.add(new Point(Double.valueOf(paras[i]), Double.valueOf(paras[i + 1])));
                }
                if (paras[0].equals("drawPolygon"))
                    doDrawPolygon(Integer.valueOf(paras[1]), Integer.valueOf(paras[2]), paras[3], points);
                else if (paras[0].equals("drawCurve"))
                    doDrawCurve(Integer.valueOf(paras[1]), Integer.valueOf(paras[2]), paras[3], points);
                break;
            case "drawEllipse":
                if (paras.length != 6 || !StringUtil.isInteger(paras[1]) || !StringUtil.isDouble(paras[2])
                        || !StringUtil.isDouble(paras[3]) || !StringUtil.isDouble(paras[4]) || !StringUtil.isDouble(paras[5])) {
                    usage(paras[0]);
                    return;
                }
                doDrawEllipse(Integer.valueOf(paras[1]), Double.valueOf(paras[2]), Double.valueOf(paras[3]),
                        Double.valueOf(paras[4]), Double.valueOf(paras[5]));
                break;
            case "translate":
                if (paras.length != 4 || !StringUtil.isInteger(paras[1]) || !StringUtil.isDouble(paras[2])
                    || !StringUtil.isDouble(paras[3])){
                    usage(paras[0]);
                    return;
                }
                doTranslate(Integer.valueOf(paras[1]), Double.valueOf(paras[2]), Double.valueOf(paras[3]));
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
            case "drawPolygon":
                System.out.println("Usage: " + command + " [id] [n] [algorithm] ([X] [Y] ...).");
                break;
            case "drawEllipse":
                System.out.println("Usage: " + command + " [id] [x] [y] [rx] [ry].");
                break;
            case "drawCurve":
                System.out.println("Usage: " + command + " [id] [n] [algorithm] ([X] [Y] ...).");
                break;
            case "translate":
                System.out.println("Usage: " + command + " [id] [dx] [dy].");
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

    private static void doDrawPolygon(int id, int n, String algorithm, Vector<Point> points){
        Canvas.getInstance().drawPolygon(id, n, algorithm, points);
        ImageUtil.canvasUpdate();
    }

    private static void doDrawEllipse(int id, double x, double y, double rx, double ry){
        Canvas.getInstance().drawEllipse(id, x, y, rx, ry);
        ImageUtil.canvasUpdate();
    }

    private static void doDrawCurve(int id, int n, String algorithm, Vector<Point> points){
        Canvas.getInstance().drawCurve(id, n, algorithm, points);
        ImageUtil.canvasUpdate();
    }

    public static void doTranslate(int id, double dx, double dy){
        Canvas.getInstance().translate(id, dx, dy);
        ImageUtil.canvasUpdate();
    }
}
