package client.scenes.services;

import client.scenes.TaskCreationCtrl;
import commons.Card;
import javafx.concurrent.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class taskCreationsTest {

    private TaskCreationCtrl taskCreationCtrl;
    private TaskCreationCtrl taskCreationCtrl1;

    @BeforeEach
    void setUp() {
         taskCreationCtrl = new TaskCreationCtrl(null, null);
         taskCreationCtrl.sendData(null, 10, null);
         taskCreationCtrl1 = new TaskCreationCtrl(null, null);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        Assertions.assertEquals(taskCreations.getInstance(), taskCreations.getInstance());

    }

    @Test
    void add() {
        taskCreations.getInstance().add(taskCreationCtrl);
        taskCreations.getInstance().add(taskCreationCtrl1);
        Assertions.assertEquals(taskCreations.getInstance().getArray().size(), 3);

    }

    @Test
    void remove() {
        taskCreations.getInstance().add(taskCreationCtrl);
        taskCreations.getInstance().add(taskCreationCtrl1);
        taskCreations.getInstance().remove(taskCreationCtrl);
        Assertions.assertEquals(taskCreations.getInstance().getArray().size(), 1);
    }

    @Test
    void isOpened() {
        Card card = new Card( "test", "t", null, 3);
        card.id = 1;
//        taskCreations.getInstance().isOpened(card);

    }

    @Test
    void closeAll() {
//        taskCreations.getInstance().closeAll();
    }
}