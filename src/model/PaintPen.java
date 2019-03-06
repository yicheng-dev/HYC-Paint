package model;

import gui.WarningText;

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
        if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255)
            color = new Color(r, g, b);
        else
            WarningText.getInstance().setWarningText("r, g, b should range from 0 to 255.");
    }

    public int getRGB(){
        return color.getRGB();
    }
}
