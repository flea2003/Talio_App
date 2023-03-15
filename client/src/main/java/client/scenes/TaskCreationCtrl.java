package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class TaskCreationCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField taskName;

    @FXML
    private TextField taskDescription;

    @FXML
    private javafx.scene.control.Button addTask;

    @FXML
    private AnchorPane pane;

    @FXML
    private Text error;

    private List listCurr;

    @Inject
    public TaskCreationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void processClick(javafx.event.ActionEvent event){
        System.out.println(event);
        System.out.println("hi");
        String valueName = "";
        String valueDes = "";
        setError("");
        System.out.println(addTask);
        System.out.println(event.getSource());
        if(event.getSource() == addTask) {
            valueName = extractValue(taskName);
            valueDes = extractValue(taskDescription);
            if (valueName.equals("")) {
                setError("Task Name cannot be empty. Please try again1!");
            } else {
                // here we send the value to the database
                mainCtrl.switchDashboard("LOL");
            }
        }
        Card card = new Card(valueDes, valueName, listCurr);
        listCurr.cards.add(card);
        server.addCard(card);
        server.updateList(listCurr);
        taskName.setText("");
        taskDescription.setText("");
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("LOL");
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ENTER) {
            String taskNameText = extractValue(taskName);
            if (taskNameText.length() >= 1) {
                addTask.fire();
            } else {
                setError("Task Name cannot be empty. Please try again2!");
            }
        }
    }

    private String extractValue(TextField curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }
}
