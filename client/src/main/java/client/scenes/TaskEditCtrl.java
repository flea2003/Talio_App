package client.scenes;

import client.scenes.services.CardControllerState;
import client.scenes.services.taskEdits;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.application.Application;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.inject.Inject;


public class TaskEditCtrl extends Application implements CardControllerState {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;
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

    private Board boardCurr;

    Scene scene;

    Stage stage;

    /**
     * constructor
     * @param server the current server
     * @param mainCtrl a reference to the MainCtrl
     * @param card the card to be edited
     */
    @Inject
    public TaskEditCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.currCard = card;
    }


    /**
     * sets the card info to the ones of the given card
     * @param q the card of which the info will be used
     */
    public void renderInfo(Card q){
        name.setText(q.name);
        description.setText(q.description);
        currCard = q;
    }

    /**
     * sets the changes to the card and exists the scene
     */
    @FXML
    public void setDone(){
        String valueName = "";
        String valueDes = "";
        valueName = extractValue(name);
        valueDes = extractValue(description);
        if (valueName.strip().length() == 0) {
            setError("Task Name cannot be empty. Please try again!");
        } else {
            setError("");
            currCard.name = valueName;
            currCard.description = valueDes;

            listCurr=currCard.getList();

            for(int i=0; i<boardCurr.lists.size(); i++){
                if(boardCurr.lists.get(i).getID()==listCurr.getID()){
                    boardCurr.lists.set(i,listCurr);
                }
            }

            server.updateBoard(boardCurr);
            taskEdits.getInstance().remove(this);
            mainCtrl.reallySwitchTaskView(currCard, boardCurr, this.stage);
        }
        name.setText("");
        description.setText("");

    }

    /**
     * sets the card to the given one
     * @param card the new card to be used
     * @return the new card
     */
    public Card setCard(Card card) {
        card.setName(name.getText());
        card.setDescription(description.getText());
        return card;
    }

    /**
     * extract value written in the TextArea
     * @param curr textArea
     * @return text
     */
    private String extractValue(TextArea curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

    /**
     * sets the current list to a new one
     * @param listCurr the new list
     */
    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }

    /**
     * sets the current board to a new one
     * @param boardCurr the new board
     */
    public void setBoardCurr(Board boardCurr) {
        this.boardCurr = boardCurr;
    }

    @Override
    public Card getCard() {
        return currCard;
    }

    @Override
    public Stage getStage() {
        return null;
    }

    public void sendData(Scene scene, Card q, Board boardCurr) {
        this.boardCurr = boardCurr;
        this.currCard = q;
        this.scene = scene;
        renderInfo(q);
    }


    @Override
    public void start(Stage primaryStage)  {
        this.stage = primaryStage;
        primaryStage.setTitle("Edit Task");
        primaryStage.setScene(scene);

    }


}
