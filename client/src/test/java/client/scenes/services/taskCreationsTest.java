package client.scenes.services;

import client.scenes.TaskCreationCtrl;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class taskCreationsTest {
    private TaskCreationCtrl mockController;
    private Card mockCard;

    @BeforeEach
    void setUp() {
        mockController = Mockito.mock(TaskCreationCtrl.class);
        when(mockController.getStage()).thenReturn(Mockito.mock(javafx.stage.Stage.class));

        mockCard = Mockito.mock(Card.class);
        when(mockController.getCard()).thenReturn(mockCard);
        when(mockCard.getId()).thenReturn(1L);
    }

    @Test
    void testIsOpened() {
        taskCreations instance = taskCreations.getInstance();
        instance.add(mockController);
        assertTrue(instance.isOpened(mockCard));
    }

    @Test
    void testAdd() {
        taskCreations instance = taskCreations.getInstance();
        instance.add(mockController);
        assertTrue(instance.getArray().contains(mockController));
    }

    @Test
    void testRemove() {
        taskCreations instance = taskCreations.getInstance();
        instance.add(mockController);
        instance.remove(mockController);
        assertFalse(instance.getArray().contains(mockController));
    }


    @Test
    void testCloseAll() {
        taskCreations instance = taskCreations.getInstance();
        instance.add(mockController);
        instance.closeAll();
        verify(mockController.getStage()).close();
    }

    @Test
    void testGetArray() {
        taskCreations instance = taskCreations.getInstance();
        instance.add(mockController);
        List<TaskCreationCtrl> controllers = instance.getArray();
        assertTrue(controllers.contains(mockController));
    }
}