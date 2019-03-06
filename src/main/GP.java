package main;

import gui.ToggleType;

import java.io.File;
import java.util.Vector;

public class GP {
    public final static boolean CLI = false;
    public final static boolean STDIN = false;
    public final static int ROOT_WIDTH = 1080;
    public final static int ROOT_HEIGHT = 720;
    public static ToggleType choosenToggle = ToggleType.CHOOSE;
    public static String inputInstrFile = "input.txt";
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
