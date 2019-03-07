package gui;

import io.CliInterpreter;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import main.GP;
import model.Canvas;
import model.GraphEntity;
import model.Point;
import util.GUIUtil;

import java.util.Vector;

public class CanvasView extends ImageView {

    private CanvasView(){
        super();
        GP.chosenPoints = new Vector<>();

        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GP.drawing){
                    if (GP.chosenToggle == ToggleType.LINE){
                        int id = GUIUtil.nextFreeId();
                        GraphEntity graph = Canvas.getInstance().getGraph(id);
                    }
                }
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    if (GP.chosenToggle == ToggleType.LINE || GP.chosenToggle == ToggleType.POLYGON || GP.chosenToggle == ToggleType.CURVE) {
                        if (!GP.drawing) {
                            GP.chosenPoints.add(new Point(event.getX(), Canvas.getInstance().getHeight() - event.getY()));
                            GP.drawing = true;
                        } else {
                            if (GP.chosenToggle == ToggleType.LINE) {
                                GP.chosenPoints.add(new Point(event.getX(), Canvas.getInstance().getHeight() - event.getY()));
                                String command = "drawLine " + GUIUtil.nextFreeId() + " " + GP.chosenPoints.get(0).x +
                                        " " + GP.chosenPoints.get(0).y + " " + GP.chosenPoints.get(1).x +
                                        " " + GP.chosenPoints.get(1).y + " " + GP.lineAlgorithm;
                                CliInterpreter.commandProcess(command);
                                GP.chosenPoints.clear();
                                GP.drawing = false;
                            }
                            if (GP.chosenToggle == ToggleType.POLYGON || GP.chosenToggle == ToggleType.CURVE) {
                                GP.chosenPoints.add(new Point(event.getX(), Canvas.getInstance().getHeight() - event.getY()));
                            }
                        }
                    }
                    else if (GP.chosenToggle == ToggleType.ELLIPSE){
                        String command = "drawEllipse " + GUIUtil.nextFreeId() + " " + event.getX() + " " + (Canvas.getInstance().getHeight() - event.getY())
                                + " " + GP.DEFAULT_ELLIPSE_AX + " " + GP.DEFAULT_ELLIPSE_BX;
                        CliInterpreter.commandProcess(command);
                    }
                }
                else if (event.getButton() == MouseButton.SECONDARY){
                    if (GP.drawing){
                        if (GP.chosenToggle == ToggleType.POLYGON){
                            String command = "drawPolygon " + GUIUtil.nextFreeId() + " " + GP.chosenPoints.size() + " " + GP.lineAlgorithm;
                            for (Point chosenPoint : GP.chosenPoints){
                                command += " " + chosenPoint.x + " " + chosenPoint.y;
                            }
                            CliInterpreter.commandProcess(command);
                        }
                        if (GP.chosenToggle == ToggleType.CURVE){
                            String command = "drawCurve " + GUIUtil.nextFreeId() + " " + GP.chosenPoints.size() + " " + GP.curveAlgorithm;
                            for (Point chosenPoint : GP.chosenPoints){
                                command += " " + chosenPoint.x + " " + chosenPoint.y;
                            }
                            CliInterpreter.commandProcess(command);
                        }
                    }
                    GP.chosenPoints.clear();
                    GP.drawing = false;
                }

            }
        });
    }
    private static CanvasView canvasView = new CanvasView();
    public static CanvasView getInstance(){
        return canvasView;
    }
/*
    private boolean findNearGraphEntity(int currentX, int currentY, GraphEntity graphEntity){
        int [] direction1 = {1, 0};
        int [] direction2 = {-1, 0};
        int [] direction3 = {0, 1};
        int [] direciton4 = {0, -1};
        int originX = currentX;
        int originY = currentY;
        while ()
    }*/
}
