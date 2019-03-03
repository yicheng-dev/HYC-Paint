package model;

import main.GraphEntityType;
import util.ImageUtil;

import java.util.Vector;

public class GraphEntity {
    private int id;
    private GraphEntityType type;
    private Vector<Pixel> pixels;

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
        pixels.add(new Pixel(x, y, PaintPen.getInstance().getRGB()));
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
            Canvas.getInstance().paintPixel(pixel.getX(), Canvas.getInstance().getHeight() - pixel.getY(), pixel.getRgb());
        }
    }
}
