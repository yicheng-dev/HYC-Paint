package model;

import main.GraphEntityType;

import java.util.Vector;

public class Curve extends GraphEntity{
    Vector<Pixel> points;

    public Curve(int id, GraphEntityType type){
        super(id, type);
        points = new Vector<>();
    }

    public Curve(int id, GraphEntityType type, Vector<Pixel> pixels){
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
