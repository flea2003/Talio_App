package client.scenes.services;

import client.scenes.TaskViewCtrl;
import commons.Card;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public  class taskViews {
    private static taskViews INSTANCE;

    private taskViews() {
    }

    /**
     * gets an instance
     * @return the istance
     */
    public synchronized static taskViews getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new taskViews();
        }
        return INSTANCE;
    }

    private List<TaskViewCtrl> controllers = new ArrayList<>();

    /**
     * adds a controller
     * @param controller the controller
     */
    public void add(TaskViewCtrl controller) {
        controllers.add(controller);
    }

    /**
     * removes a controller
     * @param controller the controller
     */
    public void remove(TaskViewCtrl controller) {
        controllers.remove(controller);
    }

    /**
     * checks if a Card is open
     * @param card the card
     * @return a boolean
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
     * closes all stages
     */
    public void closeAll() {
        for (TaskViewCtrl controller: controllers) {
            controller.getStage().close();
        }
    }

    /**
     * gets a Stage
     * @param card a Card
     * @return a stage
     */
    public Stage getStage(Card card) {
        for (TaskViewCtrl controller: controllers) {
            if (controller.getCard().getId() == card.getId()) {
                return controller.getStage();
            }
        }
        return null;
    }

    /**
     * gets the controller
     * @param card a Card
     * @return the controller
     */
    public TaskViewCtrl getCotroller(Card card){
        for(TaskViewCtrl controller: controllers) {
            if (controller.getCard().getId() == card.getId()) {
                return controller;
            }
        }
        return null;
    }

    /**
     * closes all windows
     * @param lists a list of lists
     */
    public void checkClosed(List<commons.List> lists) {
        for(var controller: controllers) {
            boolean wasDeleted = true;
            for (var list : lists) {
                for (var card : list.getCards()) {
                    if (controller.getCard().getId() == card.getId()) {
                        wasDeleted = false;
                    }
                }
            }
            if (wasDeleted) {
                controller.getStage().close();
            }
        }

    }
}
