package model;

import gui.WarningText;
import main.GP;
import util.CGAlgorithm;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Canvas{
    private int height;
    private int width;
    private int imageType;
    private Vector<GraphEntity> graphs;
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;
    private static int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;
    private static Color BACKGROUND_COLOR = new Color(255, 255, 255);

    private BufferedImage bufferedImage;
    private Vector<Vector<Vector<Integer>>> idOfPixels;

    private Canvas(int width, int height, int imageType) {
        bufferedImage = new BufferedImage(width, height, imageType);
        this.graphs = new Vector<>();
        this.idOfPixels = new Vector<>();
        for (int i = 0; i < width; i++){
            Vector<Vector<Integer>> idOfLine = new Vector<>();
            for (int j = 0; j < height; j++){
                idOfLine.add(new Vector<>());
            }
            idOfPixels.add(idOfLine);
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
                this.paintPixel(x, y, -1);
            }
        }
    }

    public void drawLine(int id, double beginX, double beginY, double endX, double endY, String algorithm){
        if (!assertId(id, true))
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
                WarningText.getInstance().setWarningText("Available algorithms: DDA, MidPoint and Bresenham.");
                return;
        }
        line.setAlgorithm(algorithm);
        graphs.add(line);
        GP.graphList.add(id);
        line.draw();
    }

    public void drawPolygon(int id, int n, String algorithm, Vector<Point> points){
        if (!assertId(id, true))
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
                WarningText.getInstance().setWarningText("Available algorithms: DDA, MidPoint and Bresenham.");
                return;
        }
        polygon.setAlgorithm(algorithm);
        graphs.add(polygon);
        GP.graphList.add(id);
        polygon.draw();
    }

    public void drawEllipse(int id, double x, double y, double rx, double ry){
        if (!assertId(id, true))
            return;
        Ellipse ellipse = new Ellipse(id, GraphEntityType.ELLIPSE);
        CGAlgorithm.setEllipseAttr(ellipse, x, y, rx, ry);
        CGAlgorithm.midPointEllipse(ellipse, x, y, rx, ry);
        graphs.add(ellipse);
        GP.graphList.add(id);
        ellipse.draw();
    }

    public void drawCurve(int id, int n, String algorithm, Vector<Point> points){
        if (!assertId(id, true))
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
                WarningText.getInstance().setWarningText("Available algorithms: Bezier and B-spline.");
                return;
        }
        curve.setAlgorithm(algorithm);
        graphs.add(curve);
        GP.graphList.add(id);
        curve.draw();
    }

    public void transform(int id, TransformType type, Vector<Double> vars){
        if (assertId(id, false))
            return;
        for (GraphEntity graph : graphs){
            if (id == graph.getId()){
                graph.clear();
                if (graph.getType() == GraphEntityType.LINE){
                    graph.clearPixel();
                    lineTransform(graph, type, vars);
                }
                else if (graph.getType() == GraphEntityType.POLYGON){
                    graph.clearPixel();
                    polygonTransform(graph, type, vars);
                }
                else if (graph.getType() == GraphEntityType.ELLIPSE){
                    ellipseTransform(graph, type, vars);
                }
                else if (graph.getType() == GraphEntityType.CURVE){
                    graph.clearPixel();
                    curveTransform(graph, type, vars);
                }
                break;
            }
        }
    }

    private void lineTransform(GraphEntity graph, TransformType type, Vector<Double> vars){
        Line line = (Line)graph;
        double beginX = 0, endX = 0, beginY = 0, endY = 0;
        if (type == TransformType.TRANSLATE) {
            beginX = line.getBeginPoint().getX() + vars.get(0);
            endX = line.getEndPoint().getX() + vars.get(0);
            beginY = line.getBeginPoint().getY() + vars.get(1);
            endY = line.getEndPoint().getY() + vars.get(1);
        }
        if (type == TransformType.ROTATE){
            double radians = Math.toRadians(vars.get(2));
            beginX = vars.get(0) + (line.getBeginPoint().getX() - vars.get(0)) * Math.cos(radians) - (line.getBeginPoint().getY() - vars.get(1)) * Math.sin(radians);
            endX = vars.get(0) + (line.getEndPoint().getX() - vars.get(0)) * Math.cos(radians) - (line.getEndPoint().getY() - vars.get(1)) * Math.sin(radians);
            beginY = vars.get(1) + (line.getBeginPoint().getX() - vars.get(0)) * Math.sin(radians) + (line.getBeginPoint().getY() - vars.get(1)) * Math.cos(radians);
            endY = vars.get(1) + (line.getEndPoint().getX() - vars.get(0)) * Math.sin(radians) + (line.getEndPoint().getY() - vars.get(1)) * Math.cos(radians);
        }
        if (type == TransformType.SCALE){
            beginX = line.getBeginPoint().getX() * vars.get(2) + vars.get(0) * (1 - vars.get(2));
            endX = line.getEndPoint().getX() * vars.get(2) + vars.get(0) * (1 - vars.get(2));
            beginY = line.getBeginPoint().getY() * vars.get(2) + vars.get(1) * (1 - vars.get(2));
            endY = line.getEndPoint().getY() * vars.get(2) + vars.get(1) * (1- vars.get(2));
        }
        switch (line.getAlgorithm()){
            case "Bresenham":
                CGAlgorithm.bresenham(line, beginX , beginY, endX, endY);
                break;
            case "DDA":
                CGAlgorithm.dda(line, beginX, beginY, endX, endY);
                break;
            case "MidPoint":
                CGAlgorithm.midPoint(line, beginX, beginY, endX, endY);
                break;
            default:
                break;
        }
        CGAlgorithm.setBeginEndPixel(line, beginX, beginY, endX, endY);
        line.draw();
    }

    private void polygonTransform(GraphEntity graph, TransformType type, Vector<Double> vars){
        Polygon polygon = (Polygon)graph;
        Vector<Point> points = new Vector<>();
        if (type == TransformType.TRANSLATE) {
            for (Pixel point : polygon.getPoints()){
                points.add(new Point(point.getX() + vars.get(0), point.getY() + vars.get(1)));
            }
        }
        if (type == TransformType.ROTATE){
            double radians = Math.toRadians(vars.get(2));
            for (Pixel point : polygon.getPoints()){
                points.add(new Point(vars.get(0) + (point.getX() - vars.get(0)) * Math.cos(radians) - (point.getY() - vars.get(1)) * Math.sin(radians),
                        vars.get(1) + (point.getX() - vars.get(0)) * Math.sin(radians) + (point.getY() - vars.get(1)) * Math.cos(radians)));
            }
        }
        if (type == TransformType.SCALE){
            for (Pixel point : polygon.getPoints()){
                points.add(new Point(point.getX() * vars.get(2) + vars.get(0) * (1 - vars.get(2)),
                        point.getY() * vars.get(2) + vars.get(1) * (1 - vars.get(2))));
            }
        }
        polygon.clearPoints();
        CGAlgorithm.setPolyPointsPixel(polygon, points);
        switch (polygon.getAlgorithm()){
            case "Bresenham":
                CGAlgorithm.bresenhamPolygon(polygon, points.size(), points);
                break;
            case "DDA":
                CGAlgorithm.ddaPolygon(polygon, points.size(), points);
                break;
            case "MidPoint":
                CGAlgorithm.midPointPolygon(polygon, points.size(), points);
                break;
            default:
                WarningText.getInstance().setWarningText("Available algorithms: DDA, MidPoint and Bresenham.");
                return;
        }
        polygon.draw();
    }

    private void ellipseTransform(GraphEntity graph, TransformType type, Vector<Double> vars){
        Ellipse ellipse = (Ellipse)graph;
        double centerX = 0, centerY = 0, rx = 0, ry = 0;
        if (type == TransformType.TRANSLATE) {
            for (Pixel point : ellipse.getPixels()){
                int originX = point.getX();
                int originY = point.getY();
                point.setX(CGAlgorithm.nearInt(originX + vars.get(0)));
                point.setY(CGAlgorithm.nearInt(originY + vars.get(1)));
            }
            centerX = vars.get(0) + ellipse.getCenter().x;
            centerY = vars.get(1) + ellipse.getCenter().y;
            rx = ellipse.getRx();
            ry = ellipse.getRy();
        }
        if (type == TransformType.ROTATE){
            double radians = Math.toRadians(vars.get(2));
            for (Pixel point : ellipse.getPixels()){
                int originX = point.getX();
                int originY = point.getY();
                point.setX(CGAlgorithm.nearInt(vars.get(0) + (originX - vars.get(0)) * Math.cos(radians) - (originY - vars.get(1)) * Math.sin(radians)));
                point.setY(CGAlgorithm.nearInt(vars.get(1) + (originX - vars.get(0)) * Math.sin(radians) + (originY - vars.get(1)) * Math.cos(radians)));
            }
            centerX = vars.get(0) + (ellipse.getCenter().x - vars.get(0)) * Math.cos(radians) - (ellipse.getCenter().y - vars.get(1)) * Math.sin(radians);
            centerY = vars.get(1) + (ellipse.getCenter().x - vars.get(0)) * Math.sin(radians) + (ellipse.getCenter().y - vars.get(1)) * Math.cos(radians);
            rx = ellipse.getRx();
            ry = ellipse.getRy();
        }
        if (type == TransformType.SCALE){
            for (Pixel point : ellipse.getPixels()){
                int originX = point.getX();
                int originY = point.getY();
                point.setX(CGAlgorithm.nearInt(originX * vars.get(2) + vars.get(0) * (1 - vars.get(2))));
                point.setY(CGAlgorithm.nearInt(originY * vars.get(2) + vars.get(1) * (1 - vars.get(2))));
            }
            centerX = ellipse.getCenter().x * vars.get(2) + vars.get(0) * (1 - vars.get(2));
            centerY = ellipse.getCenter().y * vars.get(2) + vars.get(1) * (1 - vars.get(2));
            rx = ellipse.getRx() * vars.get(2);
            ry = ellipse.getRy() * vars.get(2);
        }/*
        for (int i = 0; i < ellipse.getPixels().size() - 1; i++){
            CGAlgorithm.dda(ellipse, ellipse.getPixels().get(i).getX(), ellipse.getPixels().get(i).getY(),
                    ellipse.getPixels().get(i + 1).getX(), ellipse.getPixels().get(i + 1).getY());
        }
        CGAlgorithm.dda(ellipse, ellipse.getPixels().get(ellipse.getPixels().size() - 1).getX(), ellipse.getPixels().get(ellipse.getPixels().size() - 1).getY(),
                ellipse.getPixels().get(0).getX(), ellipse.getPixels().get(0).getY());*/
        CGAlgorithm.setEllipseAttr(ellipse, centerX, centerY, rx, ry);
        ellipse.draw();
    }

    private void curveTransform(GraphEntity graph, TransformType type, Vector<Double> vars){
        Curve curve = (Curve)graph;
        Vector<Point> points = new Vector<>();
        if (type == TransformType.TRANSLATE) {
            for (Pixel point : curve.getPoints()) {
                points.add(new Point(point.getX() + vars.get(0), point.getY() + vars.get(1)));
            }
        }
        if (type == TransformType.ROTATE){
            double radians = Math.toRadians(vars.get(2));
            for (Pixel point : curve.getPoints()){
                points.add(new Point(vars.get(0) + (point.getX() - vars.get(0)) * Math.cos(radians) - (point.getY() - vars.get(1)) * Math.sin(radians),
                        vars.get(1) + (point.getX() - vars.get(0)) * Math.sin(radians) + (point.getY() - vars.get(1)) * Math.cos(radians)));
            }
        }
        if (type == TransformType.SCALE){
            for (Pixel point : curve.getPoints()){
                points.add(new Point(point.getX() * vars.get(2) + vars.get(0) * (1 - vars.get(2)),
                        point.getY() * vars.get(2) + vars.get(1) * (1 - vars.get(2))));
            }
        }
        curve.clearPoints();
        CGAlgorithm.setCurvePointsPixel(curve, points);
        switch (curve.getAlgorithm()){
            case "Bezier":
                CGAlgorithm.bezier(curve, points.size(), points);
                break;
            case "B-spline":
                CGAlgorithm.bSpline(curve, points.size(), points);
                break;
            default:
                break;
        }
        curve.draw();
    }

    public void clip(int id, double x1, double y1, double x2, double y2, String algorithm){
        if (assertIdLine(id)){
            return;
        }
        if (x1 > x2 || y1 > y2){
            WarningText.getInstance().setWarningText("You should firstly input the left-bottom one and then the right-top one.");
            return;
        }
        for (GraphEntity graph : graphs){
            if (graph.getId() == id){
                graph.clear();
                graph.clearPixel();
                if (graph.getType() == GraphEntityType.LINE){
                    lineClip((Line)graph, x1, y1, x2, y2, algorithm);
                }
                break;
            }
        }
    }

    private void lineClip(Line line, double x1, double y1, double x2, double y2, String algorithm){
        switch (algorithm){
            case "Cohen-Sutherland":
                CGAlgorithm.cohenSutherland(line, x1, y1, x2, y2);
                break;
            case "Liang-Barsky":
                CGAlgorithm.liangBarsky(line, x1, y1, x2, y2);
                break;
            default:
                WarningText.getInstance().setWarningText("Available clipping algorithms: Cohen-Sutherland and Liang-Barsky");
                return;
        }
        line.draw();
    }

    public void resetCanvas(int width, int height){
        if (!(assertWidthHeight(width) && assertWidthHeight(height)))
            return;
        bufferedImage = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
        graphs = new Vector<>();
        idOfPixels = new Vector<>();
        for (int i = 0; i < width; i++){
            Vector<Vector<Integer>> idOfLine = new Vector<>();
            for (int j = 0; j < height; j++){
                idOfLine.add(new Vector<>());
            }
            idOfPixels.add(idOfLine);
        }
        this.width = width;
        this.height = height;
        setWhileBackground();
    }

    public void saveCanvas(String name){
        String filename = GP.CLI ? GP.outputDir + name : name;
        File outputFile = new File(filename);
        try {
            ImageIO.write(bufferedImage, "bmp", outputFile);
        }catch (IOException e){
            WarningText.getInstance().setWarningText("Canvas saving failed.");
        }
    }

    public void paintPixel(int x, int y, int id){
        if (!assertXY(x, y))
            return;
        idOfPixels.get(x).get(y).add(id);
        if (id == -1)
            bufferedImage.setRGB(x, y, BACKGROUND_COLOR.getRGB());
        else {
            for (GraphEntity graph : graphs){
                if (graph.getId() == id){
                    bufferedImage.setRGB(x, y, graph.getRgb());
                    break;
                }
            }
        }
    }

    public void clearPixel(int x, int y, int id){
        if (!assertXY(x, y))
            return;
        int nowRgb = BACKGROUND_COLOR.getRGB();
        if (!idOfPixels.get(x).get(y).isEmpty()){
            for (int id1 : idOfPixels.get(x).get(y)){
                if (id1 == id){
                    idOfPixels.get(x).get(y).removeElement(id1);
                    break;
                }
            }
            for (GraphEntity graph : graphs){
                if (graph.getId() == idOfPixels.get(x).get(y).get(idOfPixels.get(x).get(y).size() - 1)){
                    nowRgb = graph.getRgb();
                    break;
                }
            }
        }
        bufferedImage.setRGB(x, y, nowRgb);
    }

    public BufferedImage getImage(){
        return bufferedImage;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Vector<GraphEntity> getGraphs() {
        return graphs;
    }

    private boolean assertId(int id, boolean create){
        if (id < 0) {
            WarningText.getInstance().setWarningText("Please choose an id with positive value.");
            if (create)
                return false;
            return true;
        }
        for (GraphEntity graph : graphs){
            if (graph.getId() == id) {
                if (create)
                    WarningText.getInstance().setWarningText("The id " + id + " has been occupied by another graph entity.");
                return false;
            }
        }
        return true;
    }

    private boolean assertIdLine(int id){
        if (id < 0){
            WarningText.getInstance().setWarningText("Please choose an id with positive value.");
            return true;
        }
        for (GraphEntity graph : graphs){
            if (graph.getId() == id){
                if (graph.getType() != GraphEntityType.LINE){
                    WarningText.getInstance().setWarningText("Clip is only available for Line at current stage.");
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private boolean assertXY(int x, int y){
        if (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1)
            return true;
        return false;
    }

    private boolean assertWidthHeight(int value){
        if (value >= 100 && value <= 1000)
            return true;
        WarningText.getInstance().setWarningText("Height or width of the canvas should range from 100 to 1000.");
        return false;
    }
}
