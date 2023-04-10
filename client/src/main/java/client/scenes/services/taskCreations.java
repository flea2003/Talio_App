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
     * @return the instance of the singleton
     */
    public synchronized static taskCreations getInstance() {
        if (INSTANCE == null)
            INSTANCE = new taskCreations();
        return INSTANCE;
    }


    private final List<TaskCreationCtrl> controllers = new ArrayList<>();

    /**
     * @return the list of controllers
     */
    public void add(TaskCreationCtrl controller) {
        controllers.add(controller);
    }

    /**
     * @return the list of controllers
     */
    public void remove(TaskCreationCtrl controller) {
        controllers.remove(controller);
    }

    /**
     * @param card the card to check
     * @return true if the card is already opened
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
     * Close all the opened windows
     */
    public void closeAll() {
        for(TaskCreationCtrl controller: controllers)
            controller.getStage().close();

    }

    /**
     * @return the list of controllers
     */
    public List<TaskCreationCtrl> getArray(){
        return controllers;
    }
}
