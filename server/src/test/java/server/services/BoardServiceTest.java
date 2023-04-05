package server.services;

import commons.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    BoardService boardService;

    @Test
    public void constructorTest(){
        BoardService boardService = new BoardService(boardRepository,messagingTemplate);
        assertNotNull(boardService);
    }

    @Test
    public void getAllBoardsTest(){
        Board board1 = new Board(1, new ArrayList<>(), "board 1");
        Board board2 = new Board(2, new ArrayList<>(), "board 2");
        List<Board> boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);

        when(boardRepository.findAll()).thenReturn(boards);

        List<Board> response = boardService.getAllBoards();

        verify(boardRepository, times(1)).findAll();

        assertEquals(boards, response);
    }

    @Test
    public void saveBoardTest(){
        Board board = new Board(1, new ArrayList<>(), "board 1");

        when(boardRepository.save(board)).thenReturn(board);

        Board response = boardService.saveBoard(board);

        verify(boardRepository, times(1)).save(board);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);

        assertEquals(board, response);
    }

    @Test
    public void getBoardByIdTest(){
        Board board = new Board(1, new ArrayList<>(), "board 1");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        Board response = boardService.getBoardById(1);

        verify(boardRepository, times(1)).findById(1L);

        assertEquals(board, response);
    }

    @Test
    public void deleteBoardTest(){
        Board board = new Board(1, new ArrayList<>(), "board 1");

        boardService.deleteBoard(board);

        verify(boardRepository, times(1)).deleteById(1L);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);
    }

    @Test
    public void getBoardByKeyTest(){
        Board board = new Board(1, new ArrayList<>(), "board 1");
        board.setKey("key");

        when(boardRepository.findBoardByKey("key")).thenReturn(Optional.of(board));

        Board response = boardService.getBoardByKey("key");

        verify(boardRepository, times(1)).findBoardByKey("key");

        assertEquals(board, response);
    }
}
