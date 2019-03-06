package gui;

import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import model.Canvas;
import model.GraphEntity;

public class GraphView extends ListCell<Integer> {
    @Override
    protected void updateItem(Integer id, boolean empty){
        super.updateItem(id, empty);
        if (id != null) {
            for (GraphEntity graph : Canvas.getInstance().getGraphs()) {
                if (graph.getId() == id) {
                    Text text = new Text();
                    text.setText(graph.getType().toString() + " @" + id);
                    setGraphic(text);
                    break;
                }
            }
        }
    }
}
