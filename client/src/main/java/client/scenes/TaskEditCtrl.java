package client.scenes;

import client.utils.ServerUtils;
import com.google.errorprone.annotations.FormatMethod;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.text.Text;

import javax.inject.Inject;


public class TaskEditCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Card card;
    @FXML
    private TextArea name;
    @FXML
    private TextArea description;
    @FXML
    private Text error;

    @FXML
    private Button done;

    private Card currCard;
    @FXML
    private List listCurr;

    @Inject
    public TaskEditCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;
    }

    public void renderInfo(Card q){
        name.setText(q.name);
        description.setText(q.description);
        currCard = q;
    }

    @FXML
    public void setDone(){
        System.out.println("Done");
        String valueName = "";
        String valueDes = "";
        valueName = extractValue(name);
        valueDes = extractValue(description);
        if (valueName.equals("")) {
            System.out.println("EMPTYY");
            setError("Task Name cannot be empty. Please try again!");
        } else {
//            server.updateCard(setCard(card));
            setError("");
            mainCtrl.switchDashboard("LOL");
        }
//        server.addCard(card);
        server.updateList(listCurr);
        name.setText("");
        description.setText("");
    }
    public Card setCard(Card card) {
        card.setName(name.getText());
        card.setDescription(description.getText());
        return card;
    }

    private String extractValue(TextArea curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }
}
