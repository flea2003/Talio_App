package client.scenes.services;

import commons.Card;
import javafx.stage.Stage;

public interface CardControllerState {

    /**
     * gets a card
     * @return card
     */
    public Card getCard();

    /**
     * gets a stage
     * @return stage
     */
    public Stage getStage();
}
