package client.scenes.services;

import client.scenes.TaskViewCtrl;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class taskViewsTest {

    private taskViews taskViewsUnderTest;

    @Mock
    private TaskViewCtrl mockTaskViewCtrl;
    @Mock
    private Card mockCard;
    @Mock
    private Stage mockStage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskViewsUnderTest = taskViews.getInstance();
        taskViewsUnderTest.add(mockTaskViewCtrl);
        when(mockTaskViewCtrl.getCard()).thenReturn(mockCard);
        when(mockTaskViewCtrl.getStage()).thenReturn(mockStage);
    }

    @Test
    void testGetInstance() {
        assertNotNull(taskViews.getInstance());
    }

    @Test
    void testIsOpened() {
        when(mockCard.getId()).thenReturn(1L);
        assertTrue(taskViewsUnderTest.isOpened(mockCard));
//        verify(mockTaskViewCtrl).getStage();
//        verify(mockStage).requestFocus();
    }

    @Test
    void testIsNotOpened() {
        Card mockCard1 = mock(Card.class);
        when(mockCard1.getId()).thenReturn(20L);
        assertFalse(taskViewsUnderTest.isOpened(mockCard1));
        verify(mockTaskViewCtrl).getCard();
    }

    @Test
    void testCloseAll() {
        taskViewsUnderTest.closeAll();
        verify(mockStage).close();
    }

    @Test
    void testGetStage() {
        when(mockCard.getId()).thenReturn(10L);
        when(mockTaskViewCtrl.getCard()).thenReturn(mockCard);
        when(mockTaskViewCtrl.getStage()).thenReturn(mockStage);
        assertEquals(mockStage, taskViewsUnderTest.getStage(mockCard));
        verify(mockTaskViewCtrl, times(1)).getCard();
        verify(mockTaskViewCtrl).getStage();
    }

    @Test
    void testGetController() {
        when(mockCard.getId()).thenReturn(1L);
        assertEquals(mockTaskViewCtrl, taskViewsUnderTest.getCotroller(mockCard));
        verify(mockCard, times(2)).getId();
    }

    @Test
    void testCheckClosed() {
        Board board = mock(Board.class);
        List list1 = new List("list  1", board);
        var card1 = new Card("Card 1", "Description 1", null, 1);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        list1.setCards(cards);
        taskViewsUnderTest.checkClosed(new ArrayList<>(java.util.List.of(list1)));
        verify(mockStage, never()).close();


        List list2 = new List("list  2", board);
        var card2 = new Card("Card 1", "Description 1", null, 1);
        card2.id = 1;

        cards.add(card2);
        list2.setCards(cards);
        when(mockTaskViewCtrl.getCard()).thenReturn(card2);
        taskViewsUnderTest.checkClosed(new ArrayList<>(java.util.List.of(list1, list2)));
        doNothing().when(mockStage).close();
//        verify(mockStage).close();
    }
}