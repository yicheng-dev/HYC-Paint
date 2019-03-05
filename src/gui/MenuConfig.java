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
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.GP;

import java.io.File;
import java.util.Optional;

public class MenuConfig {

    public static void config(MenuBar menuBar, Stage stage){
        addFileMenu(menuBar, stage);
        addEditMenu(menuBar, stage);
    }

    private static void addFileMenu(MenuBar menuBar, Stage stage){
        Menu fileMenu = new Menu("File");
        addNewFileMenuItem(fileMenu);
        addSaveFileMenuItem(fileMenu, stage);
        addFromFileMenuItem(fileMenu, stage);
        menuBar.getMenus().add(fileMenu);
    }

    private static void addEditMenu(MenuBar menuBar, Stage stage){
        Menu editMenu = new Menu("Edit");
        // TODO
        menuBar.getMenus().add(editMenu);
    }

    private static void addFromFileMenuItem(Menu fileMenu, Stage stage){
        MenuItem fromFileFileMenuItem = new MenuItem("From file");
        fromFileFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                String path = file.getPath();
                GP.inputInstrFile = path;
                CliInterpreter.run();
            }
        });
        fileMenu.getItems().add(fromFileFileMenuItem);
    }

    private static void addSaveFileMenuItem(Menu fileMenu, Stage stage){
        MenuItem saveFileMenuItem = new MenuItem("Save");
        saveFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(stage);
                String path = file.getPath();
                String command = "saveCanvas " + path;
                CliInterpreter.commandProcess(command);
            }
        });
        fileMenu.getItems().add(saveFileMenuItem);
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
