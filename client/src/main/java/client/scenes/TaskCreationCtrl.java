package client.scenes;

import client.scenes.services.taskCreations;
import client.utils.ServerUtils;

import commons.Board;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.Objects;

public class TaskCreationCtrl  {

    private  ServerUtils server;
    private  MainCtrl mainCtrl;

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
    Scene taskCreation;
    Stage newStage;


    /**
     * constructor
     * @param server the current server
     * @param mainCtrl a reference to the MainCtrl
     */
    @Inject
    public TaskCreationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * initializes the data for the controller
     */
    public void sendData(Stage stage, Scene taskCreation, long boardId, List listCurr){
        newStage = stage;
        this.taskCreation = taskCreation;
        this.boardId = boardId;
        this.listCurr = listCurr;
    }

    /**
     * processes the click of the addTask button
     * @param event the click of the addTask button
     */
    @FXML
    public void processClick(javafx.event.ActionEvent event){
        String valueName = "";
        String valueDes = "";
        setError("");

            valueName = extractValue(taskName);
            valueDes = extractValue(taskDescription);
            if (valueName.strip().length() == 0) {
                setError("Task Name cannot be empty. Please try again!");
                return;
            }
        listCurr = server.getList(listCurr.id);
        Board boardCurr=server.getBoard(boardId);

                Card card = new Card(valueDes, valueName, listCurr, listCurr.cards.size() + 1);

                listCurr.cards.add(card);

                for(int i=0; i<boardCurr.lists.size(); i++){
                    if(Objects.equals(boardCurr.lists.get(i).getID(), listCurr.getID())){
                        boardCurr.lists.set(i,listCurr);
                    }
                }
                server.updateBoard(boardCurr);

        taskName.setText("");
        taskDescription.setText("");
        taskCreations.getInstance().remove(this);
        newStage.close();
    }

    /**
     * responsible for enabling 'enter' to act as a fire for the button
     * @param e an event from the keyboard
     */
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

    /**
     * extracts the value from the text field
     * @param curr the text field
     * @return the value of the text field
     */
    private String extractValue(TextField curr){
        return curr.getText();
    }

    /**
     * sets the error message
     * @param err the error message
     */
    private void setError(String err){
        error.setText(err);
    }

    /**
     * sets the current list
     * @param listCurr the current list
     */
    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }

    /**
     * sets the board id
     * @param boardId the id of the board
     */
    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }


    public Card getCard() {
        return null;
    }

    public Stage getStage() {
        return null;
    }
}
