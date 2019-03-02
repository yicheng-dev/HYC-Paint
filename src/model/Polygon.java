package model;

import main.GraphEntityType;

import java.util.Vector;

public class Polygon extends GraphEntity{

    Vector<Pixel> points;

    public Polygon(int id, GraphEntityType type){
        super(id, type);
        points = new Vector<>();
    }

    public Polygon(int id, GraphEntityType type, Vector<Pixel> pixels){
        super(id, type, pixels);
        points = new Vector<>();
    }

    public void addPoint(int x, int y, int rgb){
        points.add(new Pixel(x, y, rgb));
    }

    public Vector<Pixel> getPoints(){
        return points;
    }
}
