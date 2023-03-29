package client.scenes;
import client.scenes.services.*;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.inject.Inject;
import java.util.Optional;

public class TaskViewCtrl extends Application implements CardControllerState{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Card card;

    @FXML
    private Scene scene;

    @FXML
    private Text taskName;

    @FXML
    private Text taskDescription;

    private Card currCard;
    @FXML
    private Button editTask;

    @FXML
    private Button done;
    @FXML
    private Text taskNo;

    @FXML
    private List listCurr;

    @FXML
    private Text error;

    @FXML
    private Button deleteButton;

    private Board boardCurr;

    private Scene taskView;

    private Stage newStage;

    private taskViews viewTasks;


    @Inject
    public TaskViewCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.currCard = card;
        this.viewTasks = viewTasks;

    }

    public void sendData(Scene scene, Card card, Board board){
        this.taskView = scene;
        this.currCard = card;
        this.boardCurr = board;
    }

    @Override
    public void start(javafx.stage.Stage primaryStage)  {
        if(primaryStage != null)
            newStage = primaryStage;
        else newStage = new Stage();
        newStage.setTitle("Task View");
        newStage.setScene(taskView);
        renderInfo(currCard);
        newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                taskViews.getInstance().remove(TaskViewCtrl.this);
            }
        });
        newStage.show();

//        taskView.setOnKeyPressed(e -> this.keyPressed(e));
    }

    public void renderInfo(Card card){
        currCard = card;
        taskName.setText(card.name);
        taskDescription.setText(card.description);
        if(taskNo==null){
            taskNo=new Text();
        }
        taskNo.setText("Task No. " + card.getNumberInTheList());
        return;
    }

    public void goEdit(){
        mainCtrl.switchEdit(currCard, boardCurr);
    }
    public void goDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete Task '" + currCard.getName() + "'?");
        alert.setContentText("Are you sure you want to delete task '" + currCard.getName() +
                "'?\nThis will permanently delete the task from the server.");

        ButtonType delete = new ButtonType("Delete");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(delete, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == delete) {
            List listCurr=currCard.getList();
            listCurr.cards.remove(currCard);

            for(int i=0; i<boardCurr.lists.size(); i++){
                if(boardCurr.lists.get(i).getID()==listCurr.getID()){
                    boardCurr.lists.set(i,listCurr);
                }
            }

            server.updateBoard(boardCurr);
            server.deleteCard(currCard.id);
            newStage.close();
            taskViews.getInstance().remove(this);
//            mainCtrl.switchDelete(currCard);
        }

    }


    @FXML
    public void setDone(){
//        mainCtrl.switchDashboard("LOL");
        newStage.close();
    }


    private String extractValue(Text curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }

    public void setBoardCurr(Board boardCurr) {
        this.boardCurr = boardCurr;
    }

    @Override
    public Card getCard() {
        return currCard;
    }

    public Stage getStage(){return newStage;}
}

