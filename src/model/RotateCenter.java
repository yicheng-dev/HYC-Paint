package model;

import java.util.Vector;

public class RotateCenter extends GraphEntity{
    private double centerX;
    private double centerY;
    public RotateCenter(int id, Vector<Pixel> pixels) {
        super(id, pixels);
    }
    public void changeTo() {

    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
}
