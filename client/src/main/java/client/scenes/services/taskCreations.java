package client.scenes.services;

import client.scenes.TaskCreationCtrl;
import commons.Card;

import java.util.ArrayList;
import java.util.List;

public class taskCreations {
    private static taskCreations INSTANCE;

    private taskCreations() {
    }

    /**
     * gets an instance
     * @return the instance
     */
    public synchronized static taskCreations getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new taskCreations();
        }
        return INSTANCE;
    }

    private final List<TaskCreationCtrl> controllers = new ArrayList<>();

    /**
     * adds a controller
     * @param controller the controller
     */
    public void add(TaskCreationCtrl controller) {
        controllers.add(controller);
    }

    /**
     * removes a controller
     * @param controller the controller
     */
    public void remove(TaskCreationCtrl controller) {
        controllers.remove(controller);
    }
//    public List<TaskCreationCtrl> (){return taskViewControllers;}

    /**
     * checks if a card is open
     * @param card the card
     * @return a boolean
     */
    public boolean isOpened(Card card) {
        for (TaskCreationCtrl controller : controllers) {
            if (controller.getCard().id == card.id) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    /**
     * closes all controllers
     */
    public void closeAll() {
        for (TaskCreationCtrl controller: controllers) {
            controller.getStage().close();
        }
    }

    /**
     * gets a controller
     * @return the controller
     */
    public List<TaskCreationCtrl> getArray(){
        return controllers;
    }
}
