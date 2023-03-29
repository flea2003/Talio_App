package client.scenes.services;

import commons.Card;

import java.util.ArrayList;
import java.util.List;

public  class ControllerArray<T extends CardControllerState>{
    private static ControllerArray INSTANCE;

    private ControllerArray() {
    }

    public synchronized static ControllerArray getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ControllerArray();
        return INSTANCE;
    }

    private List<T> controllers = new ArrayList<>();

    public void add(T controller) {
        controllers.add(controller);
    }

    public void remove(T controller) {
        controllers.remove(controller);
    }
//    public List<TaskViewCtrl> (){return taskViewControllers;}

    public boolean isOpened(Card card) {
        for (T controller : controllers) {
            if (controller.getCard().id == card.id) {
                controller.getStage().requestFocus();
                return true;
            }
        }
        return false;
    }

    public void closeAll() {
        for(T controller: controllers)
            controller.getStage().close();
        
    }
}
