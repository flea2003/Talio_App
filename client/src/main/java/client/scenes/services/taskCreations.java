package client.scenes.services;

import client.scenes.TaskCreationCtrl;
import client.scenes.TaskCreationCtrl;
import client.scenes.TaskEditCtrl;
import commons.Card;

import java.util.ArrayList;
import java.util.List;

public class taskCreations {
    private static taskCreations INSTANCE;

    private taskCreations() {
    }

    public synchronized static taskCreations getInstance() {
        if (INSTANCE == null)
            INSTANCE = new taskCreations();
        return INSTANCE;
    }

    private final List<TaskCreationCtrl> controllers = new ArrayList<>();

    public void add(TaskCreationCtrl controller) {
        controllers.add(controller);
    }

    public void remove(TaskCreationCtrl controller) {
        controllers.remove(controller);
    }
//    public List<TaskCreationCtrl> (){return taskViewControllers;}


    public boolean isOpened(Card card) {
        for (TaskCreationCtrl controller : controllers) {
            if (controller.getCard().id == card.id) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    public void closeAll() {
        for(TaskCreationCtrl controller: controllers)
            controller.getStage().close();

    }
    
    public List<TaskCreationCtrl> getArray(){
        return controllers;
    }
}
