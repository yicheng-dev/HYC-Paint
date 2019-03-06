package main;

import gui.ToggleType;
import model.Canvas;
import model.Point;

import java.io.File;
import java.util.Vector;

public class GP {
    public final static boolean CLI = false;
    public final static boolean STDIN = false;
    public final static int ROOT_WIDTH = 1080;
    public final static int ROOT_HEIGHT = 720;
    public static int id = 0;
    public static int DEFAULT_ELLIPSE_AX = Canvas.getInstance().getWidth() / 10;
    public static int DEFAULT_ELLIPSE_BX = Canvas.getInstance().getHeight() / 10;

    public static Vector<Point> chosenPoints = new Vector<>();
    public static boolean drawing = false;
    public final static int NEAR_BOUND = 10;

    public static String lineAlgorithm = "DDA";
    public static String curveAlgorithm = "Bezier";
    public static ToggleType chosenToggle = ToggleType.CHOOSE;
    public static String inputInstrFile = "shit.txt";
    public static String outputDir = "." + File.separator;
    public static Vector<String> operations = new Vector<>();

    static {
        operations.add("resetCanvas");
        operations.add("saveCanvas");
        operations.add("setColor");
        operations.add("drawLine");
        operations.add("drawPolygon");
        operations.add("drawEllipse");
        operations.add("drawCurve");
        operations.add("translate");
        operations.add("rotate");
        operations.add("scale");
        operations.add("clip");
        operations.add("exit");
        operations.add("q");
    }
}
