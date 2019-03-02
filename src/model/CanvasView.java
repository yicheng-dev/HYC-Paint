package model;

import javafx.scene.image.ImageView;

public class CanvasView extends ImageView {
    private CanvasView(){
        super();
    }
    private static CanvasView canvasView = new CanvasView();
    public static CanvasView getInstance(){
        return canvasView;
    }
}
