package model;

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

    public SelectedFrame(GraphEntity parent) {
        super();
        this.parent = parent;
        this.scaleLabels = new Vector<>();
        buildFrame();
        buildScaleLabel();
        buildRotateLabel();
        buildRotateCenter();
        buildScaleCenter();
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    public void update() {
        this.scaleLabels.clear();
        buildFrame();
        buildScaleLabel();
        buildRotateLabel();
        buildRotateCenter();
        buildScaleCenter();
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

    public void hide() {
        frameEdge.clear();
        frameEdge.clearPixel();
        rotateCenter.clear();
        rotateCenter.clearPixel();
        rotateLabel.clear();
        rotateLabel.clearPixel();
        scaleCenter.clear();
        scaleCenter.clearPixel();
        for (ScaleLabel scaleLabel : scaleLabels) {
            scaleLabel.clear();
            scaleLabel.clearPixel();
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

    private void buildRotateCenter() {
        Vector<Pixel> pixels = new Vector<>();
        pixels.add(new Pixel(centerX, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 1, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 2, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 1, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 2, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX + 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(centerX - 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        rotateCenter = new RotateCenter(-2, pixels);
        rotateCenter.draw();
    }

    private void buildScaleCenter() {
        Vector<Pixel> pixels = new Vector<>();
        pixels.add(new Pixel(minX, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 1, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 2, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 1, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 2, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 1, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 2, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 1, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 2, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 1, centerY - 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX + 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 1, centerY + 4, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 2, centerY + 3, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 3, centerY + 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 4, centerY + 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 4, centerY, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 4, centerY - 1, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 3, centerY - 2, GP.FRAME_COLOR.getRGB()));
        pixels.add(new Pixel(minX - 2, centerY - 3, GP.FRAME_COLOR.getRGB()));
        scaleCenter = new ScaleCenter(-2, pixels);
        scaleCenter.draw();
    }

}
