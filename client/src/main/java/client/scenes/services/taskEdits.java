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

    public synchronized static taskEdits getInstance() {
        if (INSTANCE == null)
            INSTANCE = new taskEdits();
        return INSTANCE;
    }

    private List<TaskEditCtrl> controllers = new ArrayList<>();

    public void add(TaskEditCtrl controller) {
        controllers.add(controller);
    }

    public void remove(TaskEditCtrl controller) {
        controllers.remove(controller);
    }
//    public List<TaskEditCtrl> (){return taskViewControllers;}

    public boolean isOpened(Card card) {
        for (TaskEditCtrl controller : controllers) {
            if (controller.getCard().id == card.id) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    public void closeAll() {
        for(TaskEditCtrl controller: controllers)
            controller.getStage().close();
        
    }
}
