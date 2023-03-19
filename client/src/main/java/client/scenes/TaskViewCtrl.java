package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Stage;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

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


    @Inject
    public TaskViewCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;

    }


    @FXML
    public void editCard() {
        editTask.setOnKeyPressed(e1 -> {
//            taskName.setEditable(true);
//            taskDescription.setEditable(true);
            done.setOnKeyPressed(e2 -> {
                server.updateCard(setCard(card));
            });
//            taskName.setEditable(false);
//            taskDescription.setEditable(false);
        });
    }

    public Card setCard(Card card) {
        card.setName(taskName.getText());
        card.setDescription(taskDescription.getText());
        return card;
    }

    public void renderInfo(Card card){
        currCard = card;
        System.out.println(card.name);
        System.out.println(card.description);
        taskName.setText(card.name);
        taskDescription.setText(card.description);
        taskNo.setText("Task No. " + card.getNumberInTheList());
        return;
    }

    public void goEdit(){
        mainCtrl.switchEdit(currCard);
    }
    public void goDelete(){
        currCard.getList().cards.remove(currCard);
        server.updateList(currCard.getList());
        server.deleteCard(currCard.id);
        mainCtrl.switchDelete(currCard);
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

}
