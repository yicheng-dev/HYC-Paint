package model;

import java.awt.*;

public class PaintPen{

    private Color color;

    private PaintPen(int r, int g, int b){
        color = new Color(r, g, b);
    }

    private static PaintPen paintPen = new PaintPen(0, 0, 0);
    public static PaintPen getInstance() {
        return paintPen;
    }

    public void setColor(int r, int g, int b){
        color = new Color(r, g, b);
    }

    public int getRGB(){
        return color.getRGB();
    }
}
