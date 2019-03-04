package util;

import model.*;

import java.util.Vector;

public class CGAlgorithm {
    public static void dda(GraphEntity line, double beginX, double beginY, double endX, double endY){
        if (beginX == endX){
            for (int y = Math.min(nearInt(beginY), nearInt(endY)); y <= Math.max(nearInt(endY), nearInt(beginY)); y++){
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
            for (int y = Math.min(nearInt(beginY), nearInt(endY)); y <= Math.max(nearInt(endY), nearInt(beginY)); y++){
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
            for (int y = Math.min(nearInt(beginY), nearInt(endY)); y <= Math.max(nearInt(endY), nearInt(beginY)); y++){
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
            dda(polygon, points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
        if (n > 2) {
            dda(polygon, points.get(n - 1).x, points.get(n - 1).y, points.get(0).x, points.get(0).y);
        }
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

    public static void midPointEllipse(Ellipse ellipse, double centerX, double centerY, double rx, double ry){
        double rxSquare = rx * rx;
        double rySquare = ry * ry;
        double d = rySquare + rxSquare * (-ry + 0.25);
        int x = 0, y = nearInt(ry);
        plotEllipse(ellipse, nearInt(centerX), nearInt(centerY), x, y);
        while (rySquare * (x + 1) <= rxSquare * (y - 0.5)){
            if (d < 0)
                d += rySquare * (2 * x + 3);
            else{
                d += rySquare * (2 * x + 3) + rxSquare * (-2 * y + 2);
                y --;
            }
            x ++;
            plotEllipse(ellipse, nearInt(centerX), nearInt(centerY), x, y);
        }
        d = (ry * (x + 0.5)) * 2 + (rx * (y - 1)) * 2 - (rx * ry) * 2;
        while (y >= 0){
            if (d < 0){
                d += rySquare * (2 * x + 2) + rxSquare * (-2 * y + 3);
                x ++;
            }
            else{
                d += rxSquare * (-2 * y + 3);
            }
            y --;
            plotEllipse(ellipse, nearInt(centerX), nearInt(centerY), x, y);
        }
    }

    public static void bezier(Curve curve, int n, Vector<Point> points){
        double step = 0.01;
        Vector<Point> pixels = new Vector<>();
        for (double t = 0; t <= 1; t += step){
            double x = 0.0;
            double y = 0.0;
            for (int i = 0; i <= n - 1; i++){
                x += combination(i, n - 1) * Math.pow(t, i) * Math.pow(1 - t, n - 1 - i) * points.get(i).x;
                y += combination(i, n - 1) * Math.pow(t, i) * Math.pow(1 - t, n - 1 - i) * points.get(i).y;
            }
            pixels.add(new Point(x, y));
        }
        for (int i = 0; i < pixels.size() - 1; i++){
            dda(curve, pixels.get(i).x, pixels.get(i).y, pixels.get(i + 1).x, pixels.get(i + 1).y);
        }
    }

    public static void bSpline(Curve curve, int n, Vector<Point> points){
        double step = 0.01;
        Vector<Point> pixels = new Vector<>();
        for (double t = 0; t <= 1; t += step){
            double x = 0.0;
            double y = 0.0;
            for (int i = 0; i <= n - 1; i++){
                double f = 0.0;
                for (int j = 0; j <= n - 1 - i; j++){
                    f += Math.pow(-1, j) * combination(j, n) * Math.pow(t + n - 1 - i - j, n - 1);
                }
                f /= factorial(n - 1);
                x += points.get(i).x * f;
                y += points.get(i).y * f;
            }
            pixels.add(new Point(x, y));
        }
        for (int i = 0; i < pixels.size() - 1; i++){
            dda(curve, pixels.get(i).x, pixels.get(i).y, pixels.get(i + 1).x, pixels.get(i + 1).y);
        }
    }

    public static void cohenSutherland(Line line, double x1, double y1, double x2, double y2){
        double xMin = x1;
        double xMax = x2;
        double yMin = y1;
        double yMax = y2;
        double beginX = line.getBeginPoint().getX();
        double endX = line.getEndPoint().getX();
        double beginY = line.getBeginPoint().getY();
        double endY = line.getEndPoint().getY();
        int codeBegin = encode(line.getBeginPoint().getX(), line.getBeginPoint().getY(), xMin, xMax, yMin, yMax);
        int codeEnd = encode(line.getEndPoint().getX(), line.getEndPoint().getY(), xMin, xMax, yMin, yMax);
        boolean accept = false;
        while (true){
            if ((codeBegin | codeEnd) == 0){
                accept = true;
                break;
            }
            else if ((codeBegin & codeEnd) != 0){
                break;
            }
            else {
                double x = 0, y = 0;
                int code = codeBegin != 0 ? codeBegin : codeEnd;
                if (code == codeBegin) {
                    if ((code & 8) != 0) {
                        x = beginX + (endX - beginX) * (yMax - beginY) / (endY - beginY);
                        y = yMax;
                    } else if ((code & 4) != 0) {
                        x = beginX + (endX - beginX) * (yMin - beginY) / (endY - beginY);
                        y = yMin;
                    } else if ((code & 2) != 0) {
                        y = beginY + (endY - beginY) * (xMax - beginX) / (endX - beginX);
                        x = xMax;
                    } else if ((code & 1) != 0) {
                        y = beginY + (endY - beginY) * (xMin - beginX) / (endX - beginX);
                        x = xMin;
                    }
                    beginX = x;
                    beginY = y;
                    codeBegin = encode(beginX, beginY, xMin, xMax, yMin, yMax);
                }
                else{
                    if ((code & 8) != 0) {
                        x = endX + (endX - beginX) * (yMax - endY) / (endY - beginY);
                        y = yMax;
                    } else if ((code & 4) != 0) {
                        x = endX + (endX - beginX) * (yMin - endY) / (endY - beginY);
                        y = yMin;
                    } else if ((code & 2) != 0) {
                        y = endY + (endY - beginY) * (xMax - endX) / (endX - beginX);
                        x = xMax;
                    } else if ((code & 1) != 0) {
                        y = endY + (endY - beginY) * (xMin - endX) / (endX - beginX);
                        x = xMin;
                    }
                    endX = x;
                    endY = y;
                    codeEnd = encode(endX, endY, xMin, xMax, yMin, yMax);
                }
            }
        }
        if (accept){
            setBeginEndPixel(line, nearInt(beginX), nearInt(beginY), nearInt(endX), nearInt(endY));
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
        }
    }

    public static void liangBarsky(Line line, double x1, double y1, double x2, double y2){

    }

    public static void setBeginEndPixel(Line line, double beginX, double beginY, double endX, double endY){
        line.setBeginPoint(nearInt(beginX), nearInt(beginY), line.getRgb());
        line.setEndPoint(nearInt(endX), nearInt(endY), line.getRgb());
    }

    public static void setPolyPointsPixel(Polygon polygon, Vector<Point> points){
        for (Point point : points){
            polygon.addPoint(nearInt(point.x), nearInt(point.y), polygon.getRgb());
        }
    }

    public static void setCurvePointsPixel(Curve curve, Vector<Point> points){
        for (Point point : points){
            curve.addPoint(nearInt(point.x), nearInt(point.y), curve.getRgb());
        }
    }

    public static void setEllipseAttr(Ellipse ellipse, double x, double y, double rx, double ry){
        ellipse.setCenter(nearInt(x), nearInt(y));
        ellipse.setRadius(nearInt(rx), nearInt(ry));
    }

    private static void plotEllipse(Ellipse ellipse, int centerX, int centerY, int x, int y){
        ellipse.addPixel(centerX + x, centerY + y);
        ellipse.addPixel(centerX + x, centerY - y);
        ellipse.addPixel(centerX - x, centerY + y);
        ellipse.addPixel(centerX - x, centerY - y);
    }

    public static int nearInt(double value){
        if (value - (int)value < (int)value + 1 - value)
            return (int)value;
        return (int)value + 1;
    }

    private static int encode(double x, double y, double xMin, double xMax, double yMin, double yMax){
        int code = 0;
        if (x < xMin)
            code |= 1;
        else if (x > xMax)
            code |= 2;
        if (y < yMin)
            code |= 4;
        else if (y > yMax)
            code |= 8;
        return code;
    }

    private static int factorial(int a){
        int sum = 1;
        while( a > 0 ) {
            sum = sum * a--;
        }
        return sum;
    }

    private static int combination(int m, int n){
        return m <= n ? factorial(n) / (factorial(m) * factorial((n - m))) : 0;
    }
}
