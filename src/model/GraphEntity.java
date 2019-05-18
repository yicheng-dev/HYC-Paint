package model;

import gui.WarningText;
import main.GP;

import java.util.Vector;

public class GraphEntity {
    private int id;
    private GraphEntityType type;
    private Vector<Pixel> pixels;
    private boolean hasPainted = false;
    private SelectedFrame selectedFrame;
    private int rgb;

    public GraphEntity() {
        this.pixels = new Vector<>();
    }

    public GraphEntity(int id, Vector<Pixel> pixels) {
        this.id = id;
        this.pixels = pixels;
    }

    public GraphEntity(int id, GraphEntityType type){
        this.id = id;
        this.type = type;
        this.pixels = new Vector<>();
    }

    public GraphEntity(int id, GraphEntityType type, Vector<Pixel> pixels){
        this.id = id;
        this.type = type;
        this.pixels = pixels;
    }

    public void addPixel(int x, int y){
        if (!hasPainted) {
            hasPainted = true;
            rgb = PaintPen.getInstance().getRGB();
        }
        pixels.add(new Pixel(x, y, rgb));
    }

    public Vector<Pixel> getPixels(){
        return pixels;
    }

    public void clearPixel(){
        pixels.clear();
    }

    public int getId(){
        return id;
    }

    public GraphEntityType getType(){
        return type;
    }

    public void draw(){
        for (Pixel pixel : pixels){
            Canvas.getInstance().paintPixel(pixel.getX(), Canvas.getInstance().getHeight() - pixel.getY(), id);
        }
        GP.drawingEntity = this;
    }

    public void clear(){
        if (pixels.size() == 0){
            WarningText.getInstance().setWarningText("GraphEntity " + id + "'s pixels haven't been assigned.");
            return;
        }
        for (Pixel pixel : pixels) {
            Canvas.getInstance().clearPixel(pixel.getX(), Canvas.getInstance().getHeight() - pixel.getY(), id);
        }
    }

    public int getRgb() {
        if (!hasPainted)
            return PaintPen.getInstance().getRGB();
        return rgb;
    }

    public void selectedDraw() {
        if (selectedFrame != null) {
            selectedFrame.show();
        }
        else {
            selectedFrame = new SelectedFrame(this);
        }
    }

    public void selectedHide() {
        selectedFrame.hide();
    }

    public void processPressed(double eventX, double eventY) {
        selectedFrame.processPressed(eventX, eventY);
    }

    public void processReleased() {
        selectedFrame.processReleased();
    }

    public void processDragged(double eventX, double eventY) {
        selectedFrame.processDragged(eventX, eventY);
    }
}
