package util;

import model.Line;

public class CGAlgorithm {
    public static void dda(Line line, double beginX, double beginY, double endX, double endY){
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

    public static void bresenham(Line line, double beginX, double beginY, double endX, double endY){
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

    public static void midPoint(Line line, double beginX, double beginY, double endX, double endY){
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

    private static int nearInt(double value){
        if (value - (int)value < (int)value + 1 - value)
            return (int)value;
        return (int)value + 1;
    }
}
