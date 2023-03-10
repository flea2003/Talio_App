package client.scenes;

import com.google.errorprone.annotations.FormatMethod;
import commons.Card;
import javafx.fxml.FXML;

import javafx.scene.control.*;

public class TaskEditCtrl {
    @FXML
    private TextArea name;
    @FXML
    private TextArea description;

    public void renderInfo(Card q){
        name.setText(q.name);
        description.setText(q.description);
    }
}
