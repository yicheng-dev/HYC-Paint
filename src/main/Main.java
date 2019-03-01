package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.PaintPen;
import util.ImageUtil;

import java.awt.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        BorderPane root = new BorderPane();
        model.Canvas ca = model.Canvas.getInstance();
        ImageView imageView = new ImageView();
        imageView.setImage(ImageUtil.bufToImage(ca.getImage()));
        root.setCenter(imageView);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        PaintPen p = PaintPen.getInstance();
        p.setColor(255, 0, 255);
        ca.resetCanvas(700, 700);
        for (int i = 0; i < 700; i++) {
            ca.paintPixel(i, i, p.getRGB());
        }
        ca.saveCanvas("output.bmp");
        ImageUtil.imageUpdate(imageView, ca);
    }
}
