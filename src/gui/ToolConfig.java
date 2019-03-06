package gui;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.GP;

public class ToolConfig {
    public static void config(VBox vBox){
        HBox toolHBox = new HBox();
        toolHBox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text("Brush Color:");
        ColorPicker colorPicker = new ColorPicker();
        ColorPickerConfig.config(colorPicker);
        toggleConfig(toolHBox);
        toolHBox.getChildren().add(text);
        toolHBox.getChildren().add(colorPicker);
        vBox.getChildren().add(toolHBox);
    }

    private static void toggleConfig(HBox toolHBox){
        ToggleButton chooseButton = new ToggleButton("Choose");
        chooseButton.setUserData("Choose");
        ToggleButton lineButton = new ToggleButton("Line");
        lineButton.setUserData("Line");
        ToggleButton polygonButton = new ToggleButton("Polygon");
        polygonButton.setUserData("Polygon");
        ToggleButton ellipseButton = new ToggleButton ("Ellipse");
        ellipseButton.setUserData("Ellipse");
        ToggleButton curveButton = new ToggleButton("Curve");
        curveButton.setUserData("Curve");
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov,
                 Toggle toggle, Toggle new_toggle) -> {
                    if (new_toggle != null) {
                        switch (new_toggle.getUserData().toString()){
                            case "Choose": GP.choosenToggle = ToggleType.CHOOSE; break;
                            case "Line": GP.choosenToggle = ToggleType.LINE; break;
                            case "Polygon": GP.choosenToggle = ToggleType.POLYGON; break;
                            case "Ellipse": GP.choosenToggle = ToggleType.ELLIPSE; break;
                            case "Curve": GP.choosenToggle = ToggleType.CURVE; break;
                            default: GP.choosenToggle = ToggleType.CHOOSE; break;
                        }
                    }
                });


        chooseButton.setToggleGroup(group);
        chooseButton.setSelected(true);
        lineButton.setToggleGroup(group);
        polygonButton.setToggleGroup(group);
        ellipseButton.setToggleGroup(group);
        curveButton.setToggleGroup(group);
        toolHBox.getChildren().add(chooseButton);
        toolHBox.getChildren().add(lineButton);
        toolHBox.getChildren().add(polygonButton);
        toolHBox.getChildren().add(ellipseButton);
        toolHBox.getChildren().add(curveButton);
    }
}
