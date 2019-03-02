package util;

import model.*;

import java.util.Vector;

public class CGAlgorithm {
    public static void dda(GraphEntity line, double beginX, double beginY, double endX, double endY){
        if (beginX == endX){
            for (int y = nearInt(beginY); y <= nearInt(endY); y++){
                line.addPixel(nearInt(beginX), y);
            }
            return;
        }
        double m = (endY - beginY) / (endX - beginX);
        if (Math.abs(m) <= 1){
            if (beginX < endX){
                double y = beginY;
                for (int x = nearInt(beginX); x <= nearInt(endX); x++){
                    line.addPixel(x, nearInt(y));
                    y += m;
                }
            }
            else{
                double y = beginY;
                for (int x = nearInt(beginX); x >= nearInt(endX); x--){
                    line.addPixel(x, nearInt(y));
                    y -= m;
                }
            }
        }
        else{
            if (beginY < endY){
                double x = beginX;
                for (int y = nearInt(beginY); y <= nearInt(endY); y++){
                    line.addPixel(nearInt(x), y);
                    x += (1/m);
                }
            }
            else{
                double x = beginX;
                for (int y = nearInt(beginY); y >= nearInt(endY); y--){
                    line.addPixel(nearInt(x), y);
                    x -= (1/m);
                }
            }
        }
    }

    public static void bresenham(GraphEntity line, double beginX, double beginY, double endX, double endY){
        if (beginX == endX){
            for (int y = nearInt(beginY); y <= nearInt(endY); y++){
                line.addPixel(nearInt(beginX), y);
            }
            return;
        }
        double m = (endY - beginY) / (endX - beginX);
        if (Math.abs(m) > 1){
            double temp1 = beginX;
            beginX = beginY;
            beginY = temp1;
            double temp2 = endX;
            endX = endY;
            endY = temp2;
        }
        if (beginX > endX){
            double tempX = beginX;
            beginX = endX;
            endX = tempX;
            double tempY = beginY;
            beginY = endY;
            endY = tempY;
        }
        int y = nearInt(beginY);
        int dx = nearInt(endX - beginX);
        int dy = nearInt(endY - beginY);
        if (beginY > endY)
            dy = -dy;
        int p = 2 * dy - dx;
        for (int x = nearInt(beginX); x <= nearInt(endX); x++){
            if (Math.abs(m) <= 1)
                line.addPixel(x, y);
            else
                line.addPixel(y, x);
            if (p >= 0){
                if (beginY < endY)
                    y++;
                else if (beginY > endY)
                    y--;
                p += 2 * (dy - dx);
            }
            else{
                p += 2 * dy;
            }
        }
    }

    public static void midPoint(GraphEntity line, double beginX, double beginY, double endX, double endY){
        if (beginX == endX){
            for (int y = nearInt(beginY); y <= nearInt(endY); y++){
                line.addPixel(nearInt(beginX), y);
            }
            return;
        }
        double m = (endY - beginY) / (endX - beginX);
        if (Math.abs(m) > 1){
            double temp1 = beginX;
            beginX = beginY;
            beginY = temp1;
            double temp2 = endX;
            endX = endY;
            endY = temp2;
        }

        if (beginX > endX) {
            double tempX = beginX;
            beginX = endX;
            endX = tempX;
            double tempY = beginY;
            beginY = endY;
            endY = tempY;
        }
        int yDiff = nearInt(beginY) - nearInt(endY);
        int xDiff = nearInt(endX) - nearInt(beginX);
        int d1 = 2 * yDiff + xDiff;
        int d2 = 2 * yDiff - xDiff;
        int y = nearInt(beginY);
        for (int x = nearInt(beginX); x <= nearInt(endX); x++) {
            if (Math.abs(m) <= 1)
                line.addPixel(x, y);
            else
                line.addPixel(y, x);
            if (beginY < endY) {
                if (d1 < 0) {
                    y++;
                    d1 += 2 * (yDiff + xDiff);
                } else {
                    d1 += 2 * yDiff;
                }
            } else if (beginY > endY) {
                if (d2 > 0) {
                    y--;
                    d2 += 2 * (yDiff - xDiff);
                } else {
                    d2 += 2 * yDiff;
                }
            }
        }
    }

    public static void ddaPolygon(Polygon polygon, int n, Vector<Point> points){
        for (int i = 0; i < n - 1; i ++){
            //System.out.println("DDA: " + points.get(i).x + " " + points.get(i).y + " " + points.get(i + 1).x + " " + points.get(i + 1).y);
            dda(polygon, points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
        if (n > 2)
            dda(polygon, points.get(n - 1).x, points.get(n - 1).y, points.get(0).x, points.get(0).y);
    }

    public static void midPointPolygon(Polygon polygon, int n, Vector<Point> points){
        for (int i = 0; i < n - 1; i ++){
            midPoint(polygon, points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
        if (n > 2)
            midPoint(polygon, points.get(n - 1).x, points.get(n - 1).y, points.get(0).x, points.get(0).y);
    }

    public static void bresenhamPolygon(Polygon polygon, int n, Vector<Point> points){
        for (int i = 0; i < n - 1; i ++){
            bresenham(polygon, points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
        if (n > 2)
            bresenham(polygon, points.get(n - 1).x, points.get(n - 1).y, points.get(0).x, points.get(0).y);
    }

    public static void setBeginEndPixel(Line line, double beginX, double beginY, double endX, double endY){
        line.setBeginPoint(nearInt(beginX), nearInt(beginY), PaintPen.getInstance().getRGB());
        line.setEndPoint(nearInt(endX), nearInt(endY), PaintPen.getInstance().getRGB());
    }

    public static void setPointsPixel(Polygon polygon, Vector<Point> points){
        for (Point point : points){
            polygon.addPoint(nearInt(point.x), nearInt(point.y), PaintPen.getInstance().getRGB());
        }
    }

    private static int nearInt(double value){
        if (value - (int)value < (int)value + 1 - value)
            return (int)value;
        return (int)value + 1;
    }
}
