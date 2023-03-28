package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Stage;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.Optional;

public class TaskViewCtrl {

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


    @Inject
    public TaskViewCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;

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
            mainCtrl.switchDelete(currCard);
        }

    }

    @FXML
    public void setDone(){
        mainCtrl.switchDashboard("LOL");
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

}
