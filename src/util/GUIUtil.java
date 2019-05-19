package util;

import main.GP;
import model.Canvas;
import model.GraphEntity;

public class GUIUtil {
    public static int nextFreeId(){
        int id = GP.id;
        for (; ; id++){
            boolean findFlag = false;
            for (GraphEntity graph : Canvas.getInstance().getGraphs()){
                if (graph.getId() == id){
                    findFlag = true;
                    break;
                }
            }
            if (!findFlag)
                break;
        }
        GP.id = id;
        return id;
    }

    public static double pointToAngle(double dstX, double dstY, double srcX, double srcY) {
        double radians;
        double degrees;
        if (srcX == dstX && dstY > srcY)
            degrees = -270;
        else if (srcX == dstX && dstY == srcY)
            degrees = 0;
        else if (srcX == dstX && dstY < srcY)
            degrees = -90;
        else if (srcY == dstY && dstX > srcX)
            degrees = 0;
        else if (srcY == dstY && dstX < srcX)
            degrees = -180;
        else {
            radians = Math.atan((srcY - dstY) / (srcX - dstX));
            degrees = Math.toDegrees(radians);
        }
        if ((dstX < srcX && dstY < srcY))
            degrees -= 180;
        else if ((dstX < srcX && dstY > srcY))
            degrees -= 180;
        else if ((dstX > srcX && dstY > srcY))
            degrees -= 360;
        return degrees;
    }

    public static double calDistance(double dstX, double dstY, double srcX, double srcY) {
        return Math.sqrt(Math.pow(dstX - srcX, 2) + Math.pow(dstY - srcY, 2));
    }
}
