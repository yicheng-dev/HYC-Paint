package gui;

import io.CliInterpreter;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorPickerConfig {
    public static void config(ColorPicker colorPicker){
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction((ActionEvent t) -> {
            int hashCode = colorPicker.getValue().hashCode();
            int r = hashCode >> 24;
            if (r < 0)
                r = 256 + r;
            int g = hashCode << 8 >> 24;
            if (g < 0)
                g = 256 + g;
            int b = hashCode << 16 >> 24;
            if (b < 0)
                b = 256 + b;
            System.out.println(r + " " + g + " " + b);
            String command = "setColor " + String.valueOf(r) + " " + String.valueOf(g) + " " + String.valueOf(b);
            CliInterpreter.commandProcess(command);
        });
    }
}
