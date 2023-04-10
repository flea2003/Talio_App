package server.api;

import commons.Board;
import commons.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.BoardService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    BoardService boardService;

    @InjectMocks
    BoardController boardController;

    @Before
    public void initialize(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addBoardTest(){
        Board board1 = new Board(1, new ArrayList<List>(), "Board 1");
        Board board2 = new Board(2, new ArrayList<List>(), null);

        when(boardService.saveBoard(board1)).thenReturn(board1);

        ResponseEntity<Board> responseBad = boardController.add(board2);
        ResponseEntity<Board> response = boardController.add(board1);

        verify(boardService, times(1)).saveBoard(board1);
        assertEquals(board1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad.getStatusCode());
    }

    @Test
    public void getAllTest(){
        Board board1 = new Board(1, new ArrayList<List>(), "Board 1");
        Board board2 = new Board(2, new ArrayList<List>(), "Board 2");
        java.util.List<Board> boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);

        when(boardService.getAllBoards()).thenReturn(boards);

        java.util.List<Board> response = boardController.getAll();

        verify(boardService, times(1)).getAllBoards();
        assertEquals(boards, response);
    }

    @Test
    public void getByIdTest(){
        Board board1 = new Board(1, new ArrayList<List>(), "Board 1");
        Board board2 = new Board(2, new ArrayList<List>(), "Board 2");

        when(boardService.getBoardById(1)).thenReturn(board1);
        when(boardService.getBoardById(2)).thenReturn(board2);

        Board response1 = boardController.getById(1).getBody();
        Board response2 = boardController.getById(2).getBody();
        ResponseEntity<Board> responseBad1 = boardController.getById(3);
        ResponseEntity<Board> responseBad2 = boardController.getById(-1);

        verify(boardService, times(1)).getBoardById(1);
        verify(boardService, times(1)).getBoardById(2);

        assertEquals(board1, response1);
        assertEquals(board2, response2);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void getByKeyTest(){
        Board board1 = new Board(1, new ArrayList<List>(), "Board 1");
        Board board2 = new Board(2, new ArrayList<List>(), "Board 2");

        board1.setKey("key1");
        board2.setKey("key2");

        when(boardService.getBoardByKey("key1")).thenReturn(board1);
        when(boardService.getBoardByKey("key2")).thenReturn(board2);

        Board response1 = boardController.getByKey("key1").getBody();
        Board response2 = boardController.getByKey("key2").getBody();
        ResponseEntity<Board> responseBad = boardController.getByKey("key3");

        verify(boardService, times(1)).getBoardByKey("key1");
        verify(boardService, times(1)).getBoardByKey("key2");

        assertEquals(board1, response1);
        assertEquals(board2, response2);
        assertEquals(null, responseBad.getBody());
    }

    @Test
    public void deleteTest(){
        Board board1 = new Board(1, new ArrayList<List>(), "Board 1");
        Board board2 = new Board(2, new ArrayList<List>(), null);

        when(boardService.getBoardById(1)).thenReturn(board1);

        ResponseEntity<Void> response = boardController.delete(1);
        ResponseEntity<Void> responseBad1 = boardController.delete(2);
        ResponseEntity<Void> responseBad2 = boardController.delete(-1);

        verify(boardService, times(1)).deleteBoard(board1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void changeNameTest(){
        Board board = new Board(1, new ArrayList<List>(), "Board 1");

        when(boardService.getBoardById(1)).thenReturn(board);

        ResponseEntity<Board> response = boardController.changeName(1, "New Name");
        ResponseEntity<Board> responseBad1 = boardController.changeName(2, "name");
        ResponseEntity<Board> responseBad2 = boardController.changeName(-1, "name");

        verify(boardService, times(2)).getBoardById(1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void updateTest(){
        Board board = new Board(1, new ArrayList<List>(), "Board 1");

        ResponseEntity<Board> response = boardController.updateBoard(board);

        verify(boardService, times(1)).saveBoard(board);

        assertEquals(board, response.getBody());
    }
}