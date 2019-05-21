package model;

import gui.CanvasView;
import gui.WarningText;
import main.GP;
import util.CGAlgorithm;
import util.GUIUtil;
import util.ImageUtil;

import java.util.Vector;

public class SelectedFrame extends GraphEntity{
    private GraphEntity parent;
    private Polygon frameEdge;
    private RotateCenter rotateCenter;
    private RotateLabel rotateLabel;
    private ScaleCenter scaleCenter;
    private Vector<ScaleLabel> scaleLabels;
    private int centerX;
    private int centerY;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private int rotateCenterX;
    private int rotateCenterY;
    private int scaleCenterX;
    private int scaleCenterY;

    private boolean pressed;
    private OperationStat operationStat;

    private double lastTransX;
    private double lastTransY;
    private double lastScaleDistance;
    private double lastDegree;

    public SelectedFrame(GraphEntity parent) {
        super();
        this.parent = parent;
        this.pressed = false;
        this.scaleLabels = new Vector<>();
        this.operationStat = OperationStat.FREE;
        buildFrame();
        buildScaleLabel();
        buildRotateLabel();
        buildRotateCenter(centerX, centerY);
        buildScaleCenter(minX, centerY);

        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    private void frameClear(int option) {
        for (ScaleLabel scaleLabel : scaleLabels) {
            scaleLabel.clear();
            scaleLabel.clearPixel();
        }
        scaleLabels.clear();
        if (option != 2) {
            scaleCenter.clear();
            scaleCenter.clearPixel();
        }
        rotateLabel.clear();
        rotateLabel.clearPixel();
        if (option != 1) {
            rotateCenter.clear();
            rotateCenter.clearPixel();
        }
        frameEdge.clear();
        frameEdge.clearPixel();
    }

    private void transUpdate() {
        frameClear(0);
        int oldCenterX = centerX, oldCenterY = centerY;
        buildFrame();
        buildScaleLabel();
        buildRotateLabel();
        buildRotateCenter(centerX + (rotateCenterX - oldCenterX), centerY + (rotateCenterY - oldCenterY));
        buildScaleCenter(centerX + (scaleCenterX - oldCenterX), centerY + (scaleCenterY - oldCenterY));
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    private void rotateUpdate() {
        frameClear(1);
        int oldCenterX = centerX, oldCenterY = centerY;
        buildFrame();
        buildScaleLabel();
        buildScaleCenter(centerX + (scaleCenterX - oldCenterX), centerY + (scaleCenterY - oldCenterY));
        buildRotateLabel();
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    private void scaleUpdate() {
        frameClear(2);
        int oldCenterX = centerX, oldCenterY = centerY;
        buildFrame();
        buildScaleLabel();
        buildRotateLabel();
        buildRotateCenter(centerX + (rotateCenterX - oldCenterX), centerY + (rotateCenterY - oldCenterY));
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    public void show() {
        frameEdge.draw();
        rotateCenter.draw();
        rotateLabel.draw();
        scaleCenter.draw();
        for (ScaleLabel scaleLabel : scaleLabels) {
            scaleLabel.draw();
        }
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    public void hide() {
        frameEdge.clear();
        rotateCenter.clear();
        rotateLabel.clear();
        scaleCenter.clear();
        for (ScaleLabel scaleLabel : scaleLabels) {
            scaleLabel.clear();
        }
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    private void buildFrame() {
        Vector<Pixel> pixels = parent.getPixels();
        if (pixels == null || pixels.isEmpty()) {
            WarningText.getInstance().setWarningText("Frame cannot be built.");
            return;
        }
        Pixel firstPixel = pixels.get(0);
        double minX = firstPixel.getX();
        double maxX = firstPixel.getX();
        double maxY = firstPixel.getY();
        double minY = firstPixel.getY();
        for (Pixel pixel : pixels) {
            double x = pixel.getX();
            double y = pixel.getY();
            minX = Math.min(x, minX);
            maxX = Math.max(x, maxX);
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
        }
        this.minX = (int)minX - 1;
        this.maxX = (int)maxX + 1;
        this.minY = (int)minY - 1;
        this.maxY = (int)maxY + 1;
        //System.out.println(minX + " " + minY + " " + maxX + " " + maxY);
        frameEdge = new Polygon(-2, GraphEntityType.POLYGON);
        Vector<Point> points = new Vector<>();
        points.add(new Point(minX, minY));
        points.add(new Point(minX, maxY));
        points.add(new Point(maxX, maxY));
        points.add(new Point(maxX, minY));
        centerX = (int)((minX + maxX) / 2);
        centerY = (int)((minY + maxY) / 2);
        CGAlgorithm.setPolyPointsPixel(frameEdge, points);
        CGAlgorithm.bresenhamPolygon(frameEdge, 4, points);
        frameEdge.setAlgorithm("Bresenham");
        frameEdge.draw();
    }

    private void buildScaleLabel() {
        int [] pointsX = {minX, minX, maxX, maxX};
        int [] pointsY = {minY, maxY, minY, maxY};
        for (int i = 0; i < 4; i ++) {
            Vector<Pixel> pixels = new Vector<>();
            pixels.add(new Pixel(pointsX[i] - 1, pointsY[i] - 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i], pointsY[i] - 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 1, pointsY[i] - 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 2, pointsY[i] - 3, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 3, pointsY[i] - 2, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 4, pointsY[i] - 1, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 4, pointsY[i], GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 4, pointsY[i] + 1, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 3, pointsY[i] + 2, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 2, pointsY[i] + 3, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] + 1, pointsY[i] + 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i], pointsY[i] + 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 1, pointsY[i] + 4, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 2, pointsY[i] + 3, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 3, pointsY[i] + 2, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 4, pointsY[i] + 1, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 4, pointsY[i], GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 4, pointsY[i] - 1, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 3, pointsY[i] - 2, GP.FRAME_COLOR.getRGB()));
            pixels.add(new Pixel(pointsX[i] - 2, pointsY[i] - 3, GP.FRAME_COLOR.getRGB()));
            ScaleLabel scaleLabel = new ScaleLabel(-2, pixels);
            scaleLabel.draw();
            scaleLabels.add(scaleLabel);
        }
    }

    private void buildRotateLabel() {
        Vector<Pixel> pixels = new Vector<>();
        pixels.add(new Pixel(maxX, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 1, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 2, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 1, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 2, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 1, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 2, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 1, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 2, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX + 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(maxX - 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        rotateLabel = new RotateLabel(-2, pixels);
        rotateLabel.draw();
    }

    private void buildRotateCenter(int x, int y) {
        Vector<Pixel> pixels = new Vector<>();
        pixels.add(new Pixel(x, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y - 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 3, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 3, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 3, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 3, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y - 3, GP.FRAME_COLOR.getRGB()));
        this.rotateCenterX = x;
        this.rotateCenterY = y;
        rotateCenter = new RotateCenter(-2, pixels);
        rotateCenter.draw();
    }

    private void buildScaleCenter(int x, int y) {
        Vector<Pixel> pixels = new Vector<>();
        pixels.add(new Pixel(x, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y - 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 3, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 4, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 3, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 2, y + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x + 1, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 1, y + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 3, y + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 4, y - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 3, y - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(x - 2, y - 3, GP.FRAME_COLOR.getRGB()));
        this.scaleCenterX = x;
        this.scaleCenterY = y;
        // System.out.println("scale center changed to: (" + scaleCenterX + ", " + scaleCenterY + ")");
        scaleCenter = new ScaleCenter(-2, pixels);
        scaleCenter.draw();
    }

    private boolean inFrame(double eventX, double eventY) {
        return (eventX >= minX - 4 && eventX <= maxX + 4 && eventY >= minY - 4 && eventY <= maxY + 4);
    }

    private boolean inRotateLabel(double eventX, double eventY) {
        return (eventX >= maxX - 4 && eventX <= maxX + 4 && eventY >= centerY - 4 && eventY <= centerY + 4);
    }

    private int inScaleLabel(double eventX, double eventY) {
        if (eventX >= minX - 4 && eventX <= minX + 4 && eventY >= minY - 4 && eventY <= minY + 4)
            return 0;
        if (eventX >= minX - 4 && eventX <= minX + 4 && eventY >= maxY - 4 && eventY <= maxY + 4)
            return 1;
        if (eventX >= maxX - 4 && eventX <= maxX + 4 && eventY >= minY - 4 && eventY <= minY + 4)
            return 2;
        if (eventX >= maxX - 4 && eventX <= maxX + 4 && eventY >= maxY - 4 && eventY <= maxY + 4)
            return 3;
        return -1;
    }

    private boolean inRotateCenter(double eventX, double eventY) {
        return (eventX >= rotateCenterX - 4 && eventX <= rotateCenterX + 4 && eventY >= rotateCenterY - 4 && eventY <= rotateCenterY + 4);
    }

    private boolean inScaleCenter(double eventX, double eventY) {
        return (eventX >= scaleCenterX - 4 && eventX <= scaleCenterX + 4 && eventY >= scaleCenterY - 4 && eventY <= scaleCenterY + 4);
    }

    public void processPressed(double eventX, double eventY) {
        // System.out.println("event: (" + eventX + ", " + eventY + ")");
        /*
        if (!inFrame(eventX, eventY))
            return;*/
        pressed = true;
        int scaleLabelId = inScaleLabel(eventX, eventY);
        if (scaleLabelId != -1) {
            parent.savePixel();
            operationStat = OperationStat.SCALING;
            lastScaleDistance = GUIUtil.calDistance(eventX, eventY, scaleCenterX, scaleCenterY);
        }
        else if (inRotateLabel(eventX, eventY)) {
            if (parent.getType() != GraphEntityType.ELLIPSE) {
                operationStat = OperationStat.ROTATING;
                lastDegree = GUIUtil.pointToAngle(eventX, eventY, rotateCenterX, rotateCenterY);
            }
            else {
                parent.savePixel();
                operationStat = OperationStat.FREE;
                WarningText.getInstance().setWarningText("Rotation of ellipse is not supported.");
                return;
            }
        }
        else if (inScaleCenter(eventX, eventY))
            operationStat = OperationStat.MOVING_SCALE;
        else if (inRotateCenter(eventX, eventY))
            operationStat = OperationStat.MOVING_ROTATE;
        else if (inFrame(eventX, eventY)){
            parent.savePixel();
            operationStat = OperationStat.TRANSLATING;
            lastTransX = eventX;
            lastTransY = eventY;
        }
        else {
            pressed = false;
        }
        WarningText.getInstance().setWarningText(operationStat.toString());
    }

    public void processReleased() {
        if (!pressed)
            return;
        operationStat = OperationStat.FREE;
    }

    public void processDragged(double eventX, double eventY) {
        if (!pressed)
            return;
        if (operationStat == OperationStat.TRANSLATING) {
            parent.loadPixel();
            Vector<Double> vars = new Vector<>();
            vars.add(eventX - lastTransX);
            vars.add(eventY - lastTransY);
            Canvas.getInstance().transform(parent.getId(), TransformType.TRANSLATE, vars);
            transUpdate();
//            lastTransX = eventX;
//            lastTransY = eventY;
        }
        else if (operationStat == OperationStat.ROTATING) {
            parent.loadPixel();
            double rotateAngle = GUIUtil.pointToAngle(eventX, eventY, rotateCenterX, rotateCenterY);
            Vector<Double> vars = new Vector<>();
            vars.add((double)rotateCenterX);
            vars.add((double)rotateCenterY);
            vars.add(rotateAngle - lastDegree);
            Canvas.getInstance().transform(parent.getId(), TransformType.ROTATE, vars);
            rotateUpdate();
//            lastDegree = rotateAngle;
        }
        else if (operationStat == OperationStat.SCALING) {
            parent.loadPixel();
            double distance = GUIUtil.calDistance(eventX, eventY, scaleCenterX, scaleCenterY);
            Vector<Double> vars = new Vector<>();
            vars.add((double)scaleCenterX);
            vars.add((double)scaleCenterY);
            vars.add(distance / lastScaleDistance);
            Canvas.getInstance().transform(parent.getId(), TransformType.SCALE, vars);
            scaleUpdate();
//            lastScaleDistance = distance;
        }
        else if (operationStat == OperationStat.MOVING_ROTATE) {
            rotateCenter.clear();
            rotateCenter.clearPixel();
            buildRotateCenter(CGAlgorithm.nearInt(eventX), CGAlgorithm.nearInt(eventY));
            if (!GP.CLI)
                ImageUtil.canvasUpdate();
        }
        else if (operationStat == OperationStat.MOVING_SCALE) {
            scaleCenter.clear();
            scaleCenter.clearPixel();
            buildScaleCenter(CGAlgorithm.nearInt(eventX), CGAlgorithm.nearInt(eventY));
            if (!GP.CLI)
                ImageUtil.canvasUpdate();
        }
    }

}
