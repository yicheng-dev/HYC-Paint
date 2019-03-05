package gui;

import io.CliInterpreter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class MenuConfig {

    public static void config(MenuBar menuBar){
        addFileMenu(menuBar);
    }

    private static void addFileMenu(MenuBar menuBar){
        Menu fileMenu = new Menu("File");
        addNewFileMenuItem(fileMenu);

        menuBar.getMenus().add(fileMenu);
    }

    private static void addNewFileMenuItem(Menu fileMenu){
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("New Canvas");
                ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField widthText = new TextField();
                widthText.setPromptText("Width");
                TextField heightText = new TextField();
                heightText.setPromptText("Height");

                grid.add(new Label("Width:"), 0, 0);
                grid.add(widthText, 1, 0);
                grid.add(new Label("Height:"), 0, 1);
                grid.add(heightText, 1, 1);
                Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.setDisable(true);
                /*widthText.textProperty().addListener((observable, oldValue, newValue) -> {
                    heightText.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                        confirmButton.setDisable(newValue.trim().isEmpty() || newValue1.trim().isEmpty());
                    });
                });*/
                confirmButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                                widthText.getText().trim().isEmpty() || heightText.getText().trim().isEmpty(),
                        widthText.textProperty(), heightText.textProperty()));
                dialog.getDialogPane().setContent(grid);
                Platform.runLater(() -> widthText.requestFocus());
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == confirmButtonType) {
                        return new Pair<>(widthText.getText(), heightText.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(resultContent -> {
                    String command = "resetCanvas " + resultContent.getKey() + " " + resultContent.getValue();
                    CliInterpreter.commandProcess(command);
                });
            }
        });
        fileMenu.getItems().add(newMenuItem);

    }
}
