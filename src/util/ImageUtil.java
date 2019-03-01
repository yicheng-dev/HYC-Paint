package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Canvas;

import java.awt.image.BufferedImage;

public class ImageUtil {
    public static Image bufToImage(BufferedImage bufferedImage){
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static void imageUpdate(ImageView imageView, Canvas canvas){
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageView.setImage(ImageUtil.bufToImage(canvas.getImage()));
            }
        }).start();
    }
}
