package server.services;

import commons.Board;
import commons.Card;
import commons.Subtask;
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
        List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask1 = new Subtask(1, 2);
        Subtask subtask2 = new Subtask(2, 1);
        subtasks.add(subtask1);
        subtasks.add((subtask2));

        List<Card> cards = new ArrayList<>();

        Card card1 = new Card();
        card1.setNumberInTheList(2);
        card1.setSubtasks(subtasks);

        Card card2 = new Card();
        card2.setNumberInTheList(1);
        card2.setSubtasks(new ArrayList<>());

        cards.add(card1);
        cards.add(card2);

        commons.List list1 = new commons.List(1, (ArrayList<Card>) cards, "list 1", new Board());
        commons.List list2 = new commons.List(2, new ArrayList<>(), "list 2", new Board());
        list1.setNumberInTheBoard(2);
        list2.setNumberInTheBoard(1);
        List<commons.List> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);

        Board board1 = new Board(1, (ArrayList<commons.List>) lists, "board 1");
        Board board2 = new Board(2, new ArrayList<>(), "board 2");
        List<Board> boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);

        when(boardRepository.findAll()).thenReturn(boards);

        List<Board> response = boardService.getAllBoards();

        verify(boardRepository, times(1)).findAll();

        assertEquals(boards, response);
        
        //check that the lists are sorted by their number in the board
        assertEquals(list2, lists.get(0));
        assertEquals(list1, lists.get(1));

        //check that the cards are sorted by their number in the list
        assertEquals(card2, list1.getCards().get(0));
        assertEquals(card1, list1.getCards().get(1));

        //check that the subtasks are sorted by their number in the card
        assertEquals(subtask2, card1.getSubtasks().get(0));
        assertEquals(subtask1, card1.getSubtasks().get(1));
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
