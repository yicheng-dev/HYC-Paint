package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas{
    private int height;
    private int width;
    private int imageType;
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;
    private static int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;

    private BufferedImage bufferedImage;

    private Canvas(int width, int height, int imageType) {
        bufferedImage = new BufferedImage(width, height, imageType);
        this.width = width;
        this.height = height;
        this.imageType = imageType;
        setWhileBackground();
    }
    private static Canvas canvas = new Canvas(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_IMAGE_TYPE);
    public static Canvas getInstance() {
        return canvas;
    }

    private void setWidth(int width){
        this.width = width;
    }

    private void setHeight(int height){
        this.height = height;
    }

    private void setWhileBackground(){
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                this.paintPixel(x, y, new Color(255, 255, 255).getRGB());
            }
        }
    }

    public void resetCanvas(int width, int height){
        if (!(assertWidthHeight(width) && assertWidthHeight(height)))
            return;
        bufferedImage = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
        this.width = width;
        this.height = height;
        setWhileBackground();
    }

    public void saveCanvas(String name){
        File outputFile = new File(name);
        try {
            ImageIO.write(bufferedImage, "bmp", outputFile);
        }catch (IOException e){
            System.out.println("Canvas saving failed.");
        }
    }

    public void paintPixel(int x, int y, int rgb){
        if (!assertXY(x, y))
            return;
        bufferedImage.setRGB(x, y, rgb);
    }

    public BufferedImage getImage(){
        return bufferedImage;
    }

    private boolean assertXY(int x, int y){
        if (x >= 0 && x <= width && y >= 0 && y <= height)
            return true;
        System.out.println("X or Y should be in range of width or height.");
        return false;
    }

    private boolean assertWidthHeight(int value){
        if (value >= 100 && value <= 1000)
            return true;
        System.out.println("Height or width of the canvas should range from 100 to 1000.");
        return false;
    }
}
