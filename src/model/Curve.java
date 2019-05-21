package model;

import java.util.Vector;

public class Curve extends GraphEntity{
    private Vector<Pixel> points;
    private Vector<Pixel> backupPoints;
    private String algorithm;

    public Curve(int id, GraphEntityType type){
        super(id, type);
        points = new Vector<>();
        backupPoints = new Vector<>();
    }

    public Curve(int id, GraphEntityType type, Vector<Pixel> pixels){
        super(id, type, pixels);
        points = new Vector<>();
        backupPoints = new Vector<>();
    }

    public void clearPoints(){
        points.clear();
    }

    public void addPoint(int x, int y, int rgb){
        points.add(new Pixel(x, y, rgb));
    }

    public Vector<Pixel> getPoints(){
        return points;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void savePixel() {
        backupPoints.clear();
        backupPoints.addAll(points);
    }

    @Override
    public void loadPixel() {
        points.clear();
        points.addAll(backupPoints);
    }
}
