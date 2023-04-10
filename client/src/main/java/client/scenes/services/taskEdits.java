package client.scenes.services;

import client.scenes.TaskEditCtrl;
import commons.Card;

import java.util.ArrayList;
import java.util.List;

public  class taskEdits {
    private static taskEdits INSTANCE;
    private List<TaskEditCtrl> controllers = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation
     */
    private taskEdits() {
    }

    /**
     * @return the instance of the singleton
     */
    public synchronized static taskEdits getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new taskEdits();
        }
        return INSTANCE;
    }

    /**
     * @return the list of controllers
     * @param controller the controller to add
     */
    public void add(TaskEditCtrl controller) {
        controllers.add(controller);
    }

    /**
     * @return the list of controllers
     * @param controller the controller to remove
     */
    public void remove(TaskEditCtrl controller) {
        controllers.remove(controller);
    }

    /**
     * @param card the card to check
     * @return true if the card is already opened
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
     * Close all the opened windows
     */
    public void closeAll() {
        for(TaskEditCtrl controller: controllers) {
            if (controller.getStage() != null) {
                controller.getStage().close();
            }
        }
        controllers = new ArrayList<>();

    }

    /**
     * Close all the opened windows
     * @param lists the lists to check
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
            if(wasDeleted) {
                controller.getStage().close();
            }
        }

    }

    /**
     * @return returns all of the controllers
     */
    public List<TaskEditCtrl> getControllers() {
        return controllers;
    }
}
