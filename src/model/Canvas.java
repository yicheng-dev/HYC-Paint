package model;

import main.GraphEntityType;
import util.CGAlgorithm;
import util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

public class Canvas{
    private int height;
    private int width;
    private int imageType;
    private Vector<GraphEntity> graphs;
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;
    private static int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;

    private BufferedImage bufferedImage;
    private Vector<Vector<Stack<Integer>>> rgbOfPixels;

    private Canvas(int width, int height, int imageType) {
        bufferedImage = new BufferedImage(width, height, imageType);
        this.graphs = new Vector<>();
        this.rgbOfPixels = new Vector<>();
        for (int i = 0; i < width; i++){
            Vector<Stack<Integer>> rgbOfLine = new Vector<>();
            for (int j = 0; j < height; j++){
                rgbOfLine.add(new Stack<>());
            }
            rgbOfPixels.add(rgbOfLine);
        }
        this.width = width;
        this.height = height;
        this.imageType = imageType;
        setWhileBackground();
    }
    private static Canvas canvas = new Canvas(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_IMAGE_TYPE);
    public static Canvas getInstance() {
        return canvas;
    }

    private void setWidth(int width){
        this.width = width;
    }

    private void setHeight(int height){
        this.height = height;
    }

    private void setWhileBackground(){
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                this.paintPixel(x, y, new Color(255, 255, 255).getRGB());
            }
        }
    }

    public void drawLine(int id, double beginX, double beginY, double endX, double endY, String algorithm){
        if (!(assertXY((int)beginX, (int)beginY) && assertXY((int)endX + 1, (int)endY + 1)))
            return;
        if (!assertId(id))
            return;
        Line line = new Line(id, GraphEntityType.LINE);
        CGAlgorithm.setBeginEndPixel(line, beginX, beginY, endX, endY);
        switch (algorithm){
            case "Bresenham":
                CGAlgorithm.bresenham(line, beginX, beginY, endX, endY);
                break;
            case "DDA":
                CGAlgorithm.dda(line, beginX, beginY, endX, endY);
                break;
            case "MidPoint":
                CGAlgorithm.midPoint(line, beginX, beginY, endX, endY);
                break;
            default:
                System.out.println("Available algorithms: DDA, MidPoint and Bresenham.");
                return;
        }
        graphs.add(line);
        line.draw();
    }

    public void drawPolygon(int id, int n, String algorithm, Vector<Point> points){
        for (Point point : points){
            if (!assertXY((int)point.x, (int)point.y) || !assertXY((int)point.x + 1, (int)point.y + 1)){
                return;
            }
        }
        if (!assertId(id))
            return;
        Polygon polygon = new Polygon(id, GraphEntityType.POLYGON);
        CGAlgorithm.setPolyPointsPixel(polygon, points);
        switch (algorithm){
            case "Bresenham":
                CGAlgorithm.bresenhamPolygon(polygon, n, points);
                break;
            case "DDA":
                CGAlgorithm.ddaPolygon(polygon, n, points);
                break;
            case "MidPoint":
                CGAlgorithm.midPointPolygon(polygon, n, points);
                break;
            default:
                System.out.println("Available algorithms: DDA, MidPoint and Bresenham.");
                return;
        }
        graphs.add(polygon);
        polygon.draw();
    }

    public void drawEllipse(int id, double x, double y, double rx, double ry){
        if (!assertXY((int)(x - rx), (int)y) || !assertXY((int)x, (int)(y - ry))
                || !assertXY((int)(x + rx + 1), (int)y) || !assertXY((int)x, (int)(y + ry + 1))){
            return;
        }
        if (!assertId(id))
            return;
        Ellipse ellipse = new Ellipse(id, GraphEntityType.ELLIPSE);
        CGAlgorithm.setEllipseAttr(ellipse, x, y, rx, ry);
        CGAlgorithm.midPointEllipse(ellipse, x, y, rx, ry);
        graphs.add(ellipse);
        ellipse.draw();
    }

    public void drawCurve(int id, int n, String algorithm, Vector<Point> points){
        for (Point point : points){
            if (!assertXY((int)point.x, (int)point.y) || !assertXY((int)point.x + 1, (int)point.y + 1)){
                return;
            }
        }
        if (!assertId(id))
            return;
        Curve curve = new Curve(id, GraphEntityType.CURVE);
        CGAlgorithm.setCurvePointsPixel(curve, points);
        switch (algorithm){
            case "Bezier":
                CGAlgorithm.bezier(curve, n, points);
                break;
            case "B-spline":
                CGAlgorithm.bSpline(curve, n, points);
                break;
            default:
                System.out.println("Available algorithms: Bezier and B-spline.");
                return;
        }
        graphs.add(curve);
        curve.draw();
    }

    public void resetCanvas(int width, int height){
        if (!(assertWidthHeight(width) && assertWidthHeight(height)))
            return;
        bufferedImage = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
        graphs = new Vector<>();
        rgbOfPixels = new Vector<>();
        for (int i = 0; i < width; i++){
            Vector<Stack<Integer>> rgbOfLine = new Vector<>();
            for (int j = 0; j < height; j++){
                rgbOfLine.add(new Stack<>());
            }
            rgbOfPixels.add(rgbOfLine);
        }
        this.width = width;
        this.height = height;
        setWhileBackground();
    }

    public void saveCanvas(String name){
        File outputFile = new File(name);
        try {
            ImageIO.write(bufferedImage, "bmp", outputFile);
        }catch (IOException e){
            System.out.println("Canvas saving failed.");
        }
    }

    public void paintPixel(int x, int y, int rgb){
        if (!assertXY(x, y))
            return;
        rgbOfPixels.get(x).get(y).push(rgb);
        bufferedImage.setRGB(x, y, rgbOfPixels.get(x).get(y).peek());
    }

    public BufferedImage getImage(){
        return bufferedImage;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    private boolean assertId(int id){
        if (id < 0) {
            System.out.println("Please choose an id with positive value.");
            return false;
        }
        for (GraphEntity graph : graphs){
            if (graph.getId() == id) {
                System.out.println("The id " + id + " has been occupied by another graph entity.");
                return false;
            }
        }
        return true;
    }

    private boolean assertXY(int x, int y){
        if (x >= 0 && x <= width && y >= 0 && y <= height)
            return true;
        System.out.println("X or Y should be in range of width or height.");
        return false;
    }

    private boolean assertWidthHeight(int value){
        if (value >= 100 && value <= 1000)
            return true;
        System.out.println("Height or width of the canvas should range from 100 to 1000.");
        return false;
    }
}
