package gui;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class WarningText extends Text {
    private WarningText(){
        super();
        this.setFill(Color.RED);
    }
    private static WarningText warningText = new WarningText();
    public static WarningText getInstance(){
        return warningText;
    }

    public void setWarningText(String text){
        this.setText(text);
        System.out.println(text);
    }
}
