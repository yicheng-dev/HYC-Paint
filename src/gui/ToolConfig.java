package gui;

import io.CliInterpreter;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.GP;

public class ToolConfig {
    public static void config(VBox vBox){
        HBox toolHBox = new HBox();
        toolHBox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text("Brush Color:");
        TextField commandText = new TextField();
        commandText.setPromptText("Enter your command");
        commandTextConfig(commandText);
        ColorPicker colorPicker = new ColorPicker();
        ColorPickerConfig.config(colorPicker);
        toggleConfig(toolHBox);
        toolHBox.getChildren().add(text);
        toolHBox.getChildren().add(colorPicker);
        toolHBox.getChildren().add(commandText);
        vBox.getChildren().add(toolHBox);
    }

    private static void commandTextConfig(TextField commandText){
        commandText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    CliInterpreter.commandProcess(commandText.getText());
                }
            }
        });
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
                        GP.chosenPoints.clear();
                        GP.drawing = false;
                        switch (new_toggle.getUserData().toString()){
                            case "Choose": GP.chosenToggle = ToggleType.CHOOSE; break;
                            case "Line": GP.chosenToggle = ToggleType.LINE; break;
                            case "Polygon": GP.chosenToggle = ToggleType.POLYGON; break;
                            case "Ellipse": GP.chosenToggle = ToggleType.ELLIPSE; break;
                            case "Curve": GP.chosenToggle = ToggleType.CURVE; break;
                            default: GP.chosenToggle = ToggleType.CHOOSE; break;
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
