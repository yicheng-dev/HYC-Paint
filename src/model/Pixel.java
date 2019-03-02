package model;

public class Pixel {
    private int x;
    private int y;
    private int rgb;
    public Pixel(int x, int y, int rgb){
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getRgb(){
        return rgb;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setRgb(int rgb){
        this.rgb = rgb;
    }
}
