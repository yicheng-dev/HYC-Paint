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
}
