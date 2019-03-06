package util;

import main.GP;
import model.Canvas;
import model.GraphEntity;

public class GUIUtil {
    public static int nextFreeId(){
        for (int id = GP.id; ; id++){
            for (GraphEntity graph : Canvas.getInstance().getGraphs()){
                if (graph.getId() == id){
                    continue;
                }
            }
            return id;
        }
    }
}
