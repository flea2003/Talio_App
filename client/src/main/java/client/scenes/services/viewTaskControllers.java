package client.scenes.services;

import client.scenes.TaskViewCtrl;
import commons.Card;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public  class viewTaskControllers {
    private static viewTaskControllers INSTANCE;

    private viewTaskControllers() {
    }

    public static client.scenes.services.viewTaskControllers getInstance() {
        if (INSTANCE == null)
            INSTANCE = new viewTaskControllers();
        return INSTANCE;
    }

    private List<TaskViewCtrl> taskViewControllers = new ArrayList<>();

    public void addTaskViewController(TaskViewCtrl taskViewCtrl) {
        taskViewControllers.add(taskViewCtrl);
    }

    public void removeTaskViewController(TaskViewCtrl taskViewCtrl) {
        taskViewControllers.remove(taskViewCtrl);
    }
//    public List<TaskViewCtrl> (){return taskViewControllers;}

    public boolean isOpened(Card q) {
        for (TaskViewCtrl viewCtrl : taskViewControllers) {
            if (viewCtrl.getCurrentCard().id == q.id) {
                viewCtrl.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }
}
