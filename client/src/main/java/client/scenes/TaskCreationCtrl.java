package client.scenes;

import client.Main;
import client.utils.ServerUtils;

import commons.Board;
import commons.Card;
import commons.List;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.Objects;

public class TaskCreationCtrl extends Application {

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


    @Inject
    public TaskCreationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

//    @Inject
//    public  TaskCreationCtrl(){
//
//    }

    public void sendData(Scene taskCreation, long boardId, List listCurr){
        this.taskCreation = taskCreation;
        this.boardId = boardId;
        this.listCurr = listCurr;
    }
    @Override
    public void start(javafx.stage.Stage primaryStage)  {
        // Create a new stage and scene for the new scene
        System.out.println("hielo");
        newStage = new Stage();
        newStage.setTitle("Task Creation");
        newStage.setScene(taskCreation);
        newStage.show();

        taskCreation.setOnKeyPressed(e -> this.keyPressed(e));
    }

    @FXML
    public void processClick(javafx.event.ActionEvent event){
        String valueName = "";
        String valueDes = "";
        setError("");

            valueName = extractValue(taskName);
            valueDes = extractValue(taskDescription);
            if (valueName.equals("")) {
                setError("Task Name cannot be empty. Please try again!");
                return;
            }
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
        newStage.close();
    }

    public void keyPressed(KeyEvent e) {
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

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }
}
