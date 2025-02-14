package gui;

import io.CliInterpreter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.GP;

import java.util.stream.StreamSupport;

public class ToolConfig {

    static ToggleButton chooseButton = new ToggleButton("Choose");
    static ToggleButton lineButton = new ToggleButton("Line");
    static ToggleButton polygonButton = new ToggleButton("Polygon");
    static ToggleButton ellipseButton = new ToggleButton ("Ellipse");
    static ToggleButton curveButton = new ToggleButton("Curve");

    static ChoiceBox curveCb = new ChoiceBox();
    static CheckBox cropCheckBox = new CheckBox("Crop");

    public static void config(VBox vBox){
        HBox toolHBox = new HBox();
        toolHBox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text("Brush Color:");
        TextField commandText = new TextField();
        commandText.setPromptText("Enter your command");
        commandTextConfig(commandText);
        curveCb.getItems().addAll("Bezier", "B-spline");
        curveCb.setValue("Bezier");
        ColorPicker colorPicker = new ColorPicker();
        ColorPickerConfig.config(colorPicker);
        toggleConfig(toolHBox);
        toolHBox.setSpacing(10);
        toolHBox.getChildren().add(text);
        toolHBox.getChildren().add(colorPicker);
        toolHBox.getChildren().add(commandText);
        toolHBox.getChildren().add(cropCheckBox);
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
        chooseButton.setUserData("Choose");
        lineButton.setUserData("Line");
        polygonButton.setUserData("Polygon");
        ellipseButton.setUserData("Ellipse");
        curveButton.setUserData("Curve");
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov,
                 Toggle toggle, Toggle new_toggle) -> {
                    if (new_toggle != null) {
                        GP.chosenPoints.clear();
                        GP.drawing = false;
                        if (!new_toggle.getUserData().toString().equals("Choose") && GP.selectedEntity != null) {
                            GP.selectedEntity.selectedHide();
                            GP.selectedEntity = null;
                        }
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
        curveCb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                GP.isBezier = newValue.equals("Bezier");
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
        toolHBox.getChildren().add(curveCb);

        cropCheckBox.setText("Clip");
        cropCheckBox.setSelected(false);
        cropCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                GP.cropping = newValue;
            }
        });
    }
}
