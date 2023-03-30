package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
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

    private long boardId;

    @Inject
    public TaskCreationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void processClick(javafx.event.ActionEvent event){
        String valueName = "";
        String valueDes = "";
        setError("");
        if(event.getSource() == addTask) {
            valueName = extractValue(taskName);
            valueDes = extractValue(taskDescription);
            if (valueName.equals("")) {
                setError("Task Name cannot be empty. Please try again!");
            } else {
                listCurr = server.getList(listCurr.id);
                Board boardCurr=server.getBoard(boardId);

                Card card = new Card(valueDes, valueName, listCurr, listCurr.cards.size() + 1);
                listCurr.cards.add(card);

                for(int i=0; i<boardCurr.lists.size(); i++){
                    if(boardCurr.lists.get(i).getID()==listCurr.getID()){
                        boardCurr.lists.set(i,listCurr);
                    }
                }

                server.updateBoard(boardCurr);

                taskName.setText("");
                taskDescription.setText("");
                mainCtrl.switchDashboard("LOL");
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ENTER) {
            String taskNameText = extractValue(taskName);
            if (taskNameText.length() >= 1) {
                addTask.fire();
            } else {
                setError("Task Name cannot be empty. Please try again!");
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

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }
}
