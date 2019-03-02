package model;

import main.GraphEntityType;

import java.util.Vector;

public class Line extends GraphEntity{

    private Pixel beginPoint;
    private Pixel endPoint;

    public Line(int id, GraphEntityType type){
        super(id, type);
    }

    public Line(int id, GraphEntityType type, Vector<Pixel> pixels){
        super(id, type, pixels);
    }

    public Pixel getBeginPoint(){
        return beginPoint;
    }

    public Pixel getEndPoint(){
        return endPoint;
    }

    public void setBeginPoint(int x, int y, int rgb){
        beginPoint = new Pixel(x, y, rgb);
    }

    public void setEndPoint(int x, int y, int rgb){
        endPoint = new Pixel(x, y, rgb);
    }

}
