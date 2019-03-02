package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Canvas;
import model.CanvasView;

import java.awt.image.BufferedImage;

public class ImageUtil {
    public static Image bufToImage(BufferedImage bufferedImage){
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static void canvasUpdate(){
        CanvasView.getInstance().setImage(ImageUtil.bufToImage(Canvas.getInstance().getImage()));
    }
}
