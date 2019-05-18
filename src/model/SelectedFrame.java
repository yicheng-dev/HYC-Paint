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
    private ScaleLabel scaleLabel;

    public SelectedFrame(GraphEntity parent) {
        super();
        rotateCenter = new RotateCenter();
        rotateLabel = new RotateLabel();
        scaleCenter = new ScaleCenter();
        scaleLabel = new ScaleLabel();
        this.parent = parent;
        buildFrame();
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
        System.out.println(minX + " " + minY + " " + maxX + " " + maxY);
        frameEdge = new Polygon(-2, GraphEntityType.POLYGON);
        Vector<Point> points = new Vector<>();
        points.add(new Point(minX, minY));
        points.add(new Point(minX, maxY));
        points.add(new Point(maxX, maxY));
        points.add(new Point(maxX, minY));
        CGAlgorithm.setPolyPointsPixel(frameEdge, points);
        CGAlgorithm.bresenhamPolygon(frameEdge, 4, points);
        frameEdge.setAlgorithm("Bresenham");
        frameEdge.draw();
        if (!GP.CLI)
            ImageUtil.canvasUpdate();
    }

}
