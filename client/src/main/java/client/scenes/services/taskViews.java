package client.scenes.services;

import client.scenes.TaskViewCtrl;
import commons.Card;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public  class taskViews {
    private static taskViews INSTANCE;


    /**
     * Private constructor to prevent instantiation
     */
    private taskViews() {
    }

    /**
     * @return the instance of the singleton
     */
    public synchronized static taskViews getInstance() {
        if (INSTANCE == null)
            INSTANCE = new taskViews();
        return INSTANCE;
    }

    /**
     * @return the list of controllers
     */
    private List<TaskViewCtrl> controllers = new ArrayList<>();

    /**
     * @return the list of controllers
     */
    public void add(TaskViewCtrl controller) {
        controllers.add(controller);
    }

    /**
     * @return the list of controllers
     */
    public void remove(TaskViewCtrl controller) {
        controllers.remove(controller);
    }

    /**
     * @param card the card to check
     * @return true if the card is already opened
     */
    public boolean isOpened(Card card) {
        for (TaskViewCtrl controller : controllers) {
            if (controller.getCard().getId() == card.getId()) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    /**
     * Close all the opened windows
     */
    public void closeAll() {
        for(TaskViewCtrl controller: controllers)
            controller.getStage().close();

    }

    /**
     * @param card the card to check
     * @return the stage of the card
     */
    public Stage getStage(Card card) {
        for(TaskViewCtrl controller: controllers)
            if(controller.getCard().getId() == card.getId())
                return controller.getStage();
        return null;
    }

    /**
     * @param card the card to check
     * @return the controller of the card
     */
    public TaskViewCtrl getCotroller(Card card){
        for(TaskViewCtrl controller: controllers)
            if(controller.getCard().getId() == card.getId())
                return controller;
        return null;
    }

    /**
     * @param lists the lists to check
     * @return true if the card is already opened
     */
    public void checkClosed(List<commons.List> lists) {
        for(var controller: controllers) {
            boolean wasDeleted = true;
            for (var list : lists)
                for (var card : list.getCards())
                    if(controller.getCard().getId() == card.getId())
                        wasDeleted = false;
            if(wasDeleted)
                controller.getStage().close();
        }
    }
}
