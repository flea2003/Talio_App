package client.utils;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.List;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServerUtilsTest{
    ServerUtils mockServerUtils = mock(ServerUtils.class);

    @Test
    public void testConstructor(){
        ServerUtils serverUtils = new ServerUtils("server");
        assertNotNull(serverUtils);
    }
    @Test
    public void testGetServer(){
        ServerUtils serverUtils = new ServerUtils("server");
        assertEquals(serverUtils.getServer(), "server");
    }

    @Test
    public void testSetServer(){
        ServerUtils serverUtils = new ServerUtils("test");
        serverUtils.setServer("server");
        assertEquals(serverUtils.getServer(), "server");
    }

    @Test
    public void testAddCard() {
        Card card = new Card("Description", "Card 1", new List(), 1);

        //make the mock object return the expected Card object when the addCard method is called
        when(mockServerUtils.addCard(card)).thenReturn(card);

        //call the method that sends the request to the server
        Card returnedCard = mockServerUtils.addCard(card);

        //verify that the mock object was called with the expected arguments
        verify(mockServerUtils).addCard(card);
        assertEquals(returnedCard, card);
    }

    @Test
    public void testGetCards(){
        java.util.List<Card> expectedCards = new ArrayList<>();
        expectedCards.add(new Card("Description", "Card 1", new List(), 1));
        expectedCards.add(new Card("Description", "Card 2", new List(), 2));
        when(mockServerUtils.getCards()).thenReturn(expectedCards);

        java.util.List<Card> actualCards = mockServerUtils.getCards();

        assertNotNull(actualCards);
        assertEquals(actualCards, expectedCards);
        verify(mockServerUtils).getCards();
    }

    @Test
    public void testUpdateCard(){
        Card card = new Card("Description", "Card 1", new List(), 1);

        when(mockServerUtils.updateCard(card)).thenReturn(card);

        Card returnedCard = mockServerUtils.updateCard(card);

        verify(mockServerUtils).updateCard(card);
        assertEquals(returnedCard, card);
    }

    @Test
    public void testDeleteCard(){
        Card card = new Card("Description", "Card 1", new List(), 1);

        when(mockServerUtils.deleteCard(card.getId())).thenReturn(card);

        Card returnedCard = mockServerUtils.deleteCard(card.getId());

        verify(mockServerUtils).deleteCard(card.getId());
        assertEquals(returnedCard, card);
    }

    @Test
    public void testGetLists(){
        java.util.List<List> expectedLists = new ArrayList<>();
        expectedLists.add(new List(new ArrayList<>(), "List 1", new Board(), 1));
        expectedLists.add(new List(new ArrayList<>(), "List 2", new Board(), 2));
        when(mockServerUtils.getLists()).thenReturn(expectedLists);

        java.util.List<List> actualLists = mockServerUtils.getLists();

        assertNotNull(actualLists);
        assertEquals(actualLists, expectedLists);
        verify(mockServerUtils).getLists();
    }

    @Test
    public void testGetList(){
        List list = new List(new ArrayList<>(), "List 1", new Board(), 1);

        when(mockServerUtils.getList(list.getID())).thenReturn(list);

        List returnedList = mockServerUtils.getList(list.getID());

        assertNotNull(returnedList);
        assertEquals(returnedList, list);
        verify(mockServerUtils).getList(list.getID());
    }

    @Test
    public void testAddList(){
        List list = new List(new ArrayList<>(), "List 1", new Board(), 1);

        when(mockServerUtils.addList(list)).thenReturn(list);

        List returnedList = mockServerUtils.addList(list);

        assertNotNull(returnedList);
        assertEquals(returnedList, list);
        verify(mockServerUtils).addList(list);
    }

    @Test
    public void testUpdateList(){
        List list = new List(new ArrayList<>(), "List 1", new Board(), 1);

        when(mockServerUtils.updateList(list)).thenReturn(list);

        List returnedList = mockServerUtils.updateList(list);

        assertNotNull(returnedList);
        assertEquals(returnedList, list);
        verify(mockServerUtils).updateList(list);
    }

    @Test
    public void testGetListById(){
        List list = new List(new ArrayList<>(), "List 1", new Board(), 1);

        when(mockServerUtils.getListById(list.getID())).thenReturn(list);

        List returnedList = mockServerUtils.getListById(list.getID());

        assertNotNull(returnedList);
        assertEquals(returnedList, list);
        verify(mockServerUtils).getListById(list.getID());
    }

    @Test
    public void testDeleteList(){
        List list = new List(new ArrayList<>(), "List 1", new Board(), 1);

        when(mockServerUtils.deleteList(list.getID())).thenReturn(list);

        List returnedList = mockServerUtils.deleteList(list.getID());

        assertNotNull(returnedList);
        assertEquals(returnedList, list);
        verify(mockServerUtils).deleteList(list.getID());
    }

    @Test
    public void testGetBoards(){
        java.util.List<Board> expectedBoard = new ArrayList<>();
        expectedBoard.add(new Board("Board 1"));
        expectedBoard.add(new Board("Board 2"));
        when(mockServerUtils.getBoards()).thenReturn(expectedBoard);

        java.util.List<Board> actualBoards = mockServerUtils.getBoards();

        assertNotNull(actualBoards);
        assertEquals(actualBoards, expectedBoard);
        verify(mockServerUtils).getBoards();
    }

    @Test
    public void testGetBoard(){
        Board board = new Board("Board 1");
        when(mockServerUtils.getBoard(board.getId())).thenReturn(board);

        Board actualBoard = mockServerUtils.getBoard(board.getId());

        assertNotNull(actualBoard);
        assertEquals(actualBoard, board);
        verify(mockServerUtils).getBoard(board.getId());
    }

    @Test
    public void testGetBoardByKey(){
        Board board = new Board("Board 1");
        when(mockServerUtils.getBoardByKey(board.getKey())).thenReturn(board);

        Board actualBoard = mockServerUtils.getBoardByKey(board.getKey());

        assertNotNull(actualBoard);
        assertEquals(actualBoard, board);
        verify(mockServerUtils).getBoardByKey(board.getKey());
    }

    @Test
    public void testUpdateBoard(){
        Board board = new Board("Board 1");
        when(mockServerUtils.updateBoard(board)).thenReturn(board);

        Board actualBoard = mockServerUtils.updateBoard(board);

        assertNotNull(actualBoard);
        assertEquals(actualBoard, board);
        verify(mockServerUtils).updateBoard(board);
    }

    @Test
    public void testDeleteBoard(){
        Board board = new Board("Board 1");
        when(mockServerUtils.deleteBoard(board.getId())).thenReturn(board);

        Board actualBoard = mockServerUtils.deleteBoard(board.getId());

        assertNotNull(actualBoard);
        assertEquals(actualBoard, board);
        verify(mockServerUtils).deleteBoard(board.getId());
    }

    @Test
    public void testAddBoard(){
        Board board = new Board("Board 1");
        when(mockServerUtils.addBoard(board)).thenReturn(board);

        Board actualBoard = mockServerUtils.addBoard(board);

        assertNotNull(actualBoard);
        assertEquals(actualBoard, board);
        verify(mockServerUtils).addBoard(board);
    }

}