package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import main.GP;

public class LeftConfig {

    public static void config(BorderPane root){
        ListView<Integer> listView = new ListView<>();
        listView.setEditable(false);
        listView.setCellFactory((ListView<Integer> l) -> new GraphView());
        listView.setItems(GP.graphList);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WarningText.getInstance().setWarningText(String.valueOf(listView.getSelectionModel().getSelectedItem()));
            }
        });
        root.setLeft(listView);
    }
}
