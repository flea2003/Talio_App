package client.scenes.services;

import client.scenes.TaskViewCtrl;
import commons.Card;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public  class taskViews {
    private static taskViews INSTANCE;

    private taskViews() {
    }

    public synchronized static taskViews getInstance() {
        if (INSTANCE == null)
            INSTANCE = new taskViews();
        return INSTANCE;
    }

    private List<TaskViewCtrl> controllers = new ArrayList<>();

    public void add(TaskViewCtrl controller) {
        controllers.add(controller);
    }

    public void remove(TaskViewCtrl controller) {
        controllers.remove(controller);
    }
//    public List<TaskViewCtrl> (){return taskViewControllers;}

    public boolean isOpened(Card card) {
        for (TaskViewCtrl controller : controllers) {
            if (controller.getCard().getId() == card.getId()) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    public void closeAll() {
        for(TaskViewCtrl controller: controllers)
            controller.getStage().close();

    }

    public Stage getStage(Card card) {
        for(TaskViewCtrl controller: controllers)
            if(controller.getCard().getId() == card.getId())
                return controller.getStage();
        return null;
    }

    public TaskViewCtrl getCotroller(Card card){
        for(TaskViewCtrl controller: controllers)
            if(controller.getCard().getId() == card.getId())
                return controller;
        return null;
    }

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
