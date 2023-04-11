package client.scenes.services;

import client.scenes.TaskEditCtrl;
import client.scenes.TaskEditCtrl;
import commons.Card;

import java.util.ArrayList;
import java.util.List;

public  class taskEdits {
    private static taskEdits INSTANCE;

    private taskEdits() {
    }

    /**
     * gets an instance
     * @return the instance
     */
    public synchronized static taskEdits getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new taskEdits();
        }
        return INSTANCE;
    }

    private List<TaskEditCtrl> controllers = new ArrayList<>();

    /**
     * adds a controller
     * @param controller the controller
     */
    public void add(TaskEditCtrl controller) {
        controllers.add(controller);
    }

    /**
     * removes a controller
     * @param controller
     */
    public void remove(TaskEditCtrl controller) {
        controllers.remove(controller);
    }
//    public List<TaskEditCtrl> (){return taskViewControllers;}

    /**
     * checks if a card is open
     * @param card the card
     * @return a boolean
     */
    public boolean isOpened(Card card) {
        for (TaskEditCtrl controller : controllers) {
            if (controller.getCard().getId() == card.getId()) {
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
        for (TaskEditCtrl controller: controllers) {
            if (controller.getStage() != null) {
                controller.getStage().close();
            }
        }
        controllers = new ArrayList<>();
    }

    /**
     * checks if a controller is closed
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

    /**
     * getter for controllers
     * @return controllers
     */
    public List<TaskEditCtrl> getControllers() {
        return controllers;
    }
}
