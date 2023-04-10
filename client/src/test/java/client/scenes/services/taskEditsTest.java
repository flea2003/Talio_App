package client.scenes.services;

import client.scenes.TaskEditCtrl;
import commons.Card;
import commons.List;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class taskEditsTest {

    @Mock
    TaskEditCtrl mockController;

    @Mock
    Card mockCard;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        taskEdits.getInstance().closeAll();
    }

    @Test
    public void testGetInstance() {
        // Ensure that the singleton is created properly
        taskEdits instance1 = taskEdits.getInstance();
        taskEdits instance2 = taskEdits.getInstance();
        Assertions.assertSame(instance1, instance2, "The singleton instances should be the same");
    }

    @Test
    public void testAdd() {
        // Ensure that the controller is added to the list
        taskEdits edits = taskEdits.getInstance();
        edits.add(mockController);
        Assertions.assertEquals(1, edits.getControllers().size(), "The controller should be added to the list");
    }

    @Test
    public void testRemove() {
        // Ensure that the controller is removed from the list
        taskEdits edits = taskEdits.getInstance();
//        edits.closeAll();
//        Card mockCard = mock(Card.class);
//        when(mockCard.getId()).thenReturn(10L);
//        TaskEditCtrl mockController2 = mock(TaskEditCtrl.class);
//        when(mockController2.getCard()).thenReturn(mockCard);


        edits.add(mockController);
        edits.remove(mockController);
        Assertions.assertEquals(0, edits.getControllers().size(), "The controller should be removed from the list");
    }

    @Test
    public void testIsOpenedTrue() {
        // Ensure that isOpened returns true when the controller for the given card is in the list
        when(mockController.getCard()).thenReturn(mockCard);
        when(mockCard.getId()).thenReturn(1L);
        when(mockController.getStage()).thenReturn(mock(Stage.class));
        taskEdits edits = taskEdits.getInstance();
        edits.add(mockController);
        Assertions.assertTrue(edits.isOpened(mockCard), "isOpened should return true when the controller for the given card is in the list");
    }

    @Test
    public void testIsOpenedFalse() {
        // Ensure that isOpened returns false when the controller for the given card is not in the list
        when(mockController.getCard()).thenReturn(mockCard);
        when(mockCard.getId()).thenReturn(1L);
        taskEdits edits = taskEdits.getInstance();
        Assertions.assertFalse(edits.isOpened(mockCard), "isOpened should return false when the controller for the given card is not in the list");
    }

    @Test
    public void testCloseAll() {
        // Ensure that closeAll closes all stages and clears the list
        TaskEditCtrl mockController2 = mock(TaskEditCtrl.class);
        when(mockController.getStage()).thenReturn(mock(Stage.class));
        when(mockController2.getStage()).thenReturn(mock(Stage.class));
        taskEdits edits = taskEdits.getInstance();
        edits.add(mockController);
        edits.add(mockController2);
        edits.closeAll();
        verify(mockController, times(2)).getStage();
        verify(mockController2, times(2)).getStage();
        Assertions.assertTrue(edits.getControllers().isEmpty(), "closeAll should close all stages and clear the list");
    }

}