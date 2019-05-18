package gui;

import io.CliInterpreter;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import main.GP;
import model.*;
import util.CGAlgorithm;
import util.GUIUtil;
import util.ImageUtil;

import java.util.Vector;

public class CanvasView extends ImageView {

    private CanvasView(){
        super();
        GP.chosenPoints = new Vector<>();

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
                    }/*
                    else if (GP.chosenToggle == ToggleType.ELLIPSE){
                        String command = "drawEllipse " + GUIUtil.nextFreeId() + " " + event.getX() + " " + (Canvas.getInstance().getHeight() - event.getY())
                                + " " + GP.DEFAULT_ELLIPSE_AX + " " + GP.DEFAULT_ELLIPSE_BX;
                        CliInterpreter.commandProcess(command);
                    }*/
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
        

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GP.chosenToggle == ToggleType.ELLIPSE) {
                    GP.drawing = true;
                    GP.chosenPoints.clear();
                    GP.chosenPoints.add(new Point(event.getX(), Canvas.getInstance().getHeight() - event.getY()));
                    String command = "drawEllipse " + GUIUtil.nextFreeId() + " " + event.getX() + " " + (Canvas.getInstance().getHeight() - event.getY())
                            + " " + 1 + " " + 1;
                    CliInterpreter.commandProcess(command);
                    //System.out.println("center of ellipse: " + event.getX() + " " + (Canvas.getInstance().getHeight() - event.getY()));
                }
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GP.chosenToggle == ToggleType.ELLIPSE && GP.drawing) {
                    GP.drawing = false;
                    GP.chosenPoints.clear();
                    GP.drawingEntity = null;
                }
            }
        });

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GP.drawing && GP.chosenToggle == ToggleType.ELLIPSE) {
                    if (GP.drawingEntity != null && GP.drawingEntity.getType() == GraphEntityType.ELLIPSE) {
                        //System.out.println("id: " + GP.drawingEntity.getId());
                        GP.drawingEntity.clear();
                        GP.drawingEntity.clearPixel();
                        double ax = Math.abs(event.getX() - GP.chosenPoints.get(0).x);
                        double ay = Math.abs((Canvas.getInstance().getHeight() - event.getY()) - GP.chosenPoints.get(0).y);
                        //System.out.println("ax: " + ax + "\tay: " + ay);
                        double x = GP.chosenPoints.get(0).x;
                        double y = GP.chosenPoints.get(0).y;
                        /*
                        String command = "drawEllipse " + GP.drawingEntity.getId() + " " + GP.chosenPoints.get(0).x + " " + GP.chosenPoints.get(0).y
                                + " " + ax + " " + ay;
                        CliInterpreter.commandProcess(command);*/
                        CGAlgorithm.setEllipseAttr((Ellipse)GP.drawingEntity, x, y, ax, ay);
                        CGAlgorithm.midPointEllipse((Ellipse)GP.drawingEntity, x, y, ax, ay);
                        GP.drawingEntity.draw();
                        //System.out.println("fuck");
                        if (!GP.CLI)
                            ImageUtil.canvasUpdate();
                    }
                }
            }
        });
    }
    private static CanvasView canvasView = new CanvasView();
    public static CanvasView getInstance(){
        return canvasView;
    }
}
