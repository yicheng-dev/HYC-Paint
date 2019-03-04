package model;

import java.util.Vector;

public class Ellipse extends GraphEntity{

    private Point center;
    private int rx;
    private int ry;

    public Ellipse(int id, GraphEntityType type){
        super(id, type);
    }

    public Ellipse(int id, GraphEntityType type, Vector<Pixel> pixels){
        super(id, type, pixels);
    }

    public void setCenter(int x, int y) {
        this.center = new Point(x, y);
    }

    public void setRadius(int rx, int ry){
        this.rx = rx;
        this.ry = ry;
    }

    public Point getCenter(){
        return center;
    }

    public int getRx(){
        return rx;
    }

    public int getRy(){
        return ry;
    }
}
