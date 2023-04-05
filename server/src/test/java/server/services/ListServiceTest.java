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
import server.database.ListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {

    @Mock
    ListRepository listRepository;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    ListService listService;

    @Test
    public void constructorTest(){
        ListService listService = new ListService(listRepository,messagingTemplate);
        assertNotNull(listService);
    }

    @Test
    public void getAllListsTest(){
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

        when(listRepository.findAll()).thenReturn(lists);

        List<commons.List> response = listService.getAllLists();

        verify(listRepository, times(1)).findAll();

        assertEquals(lists, response);

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
    public void saveListTest(){
        commons.List list = new commons.List(1, new ArrayList<>(), "list 1", new Board());

        when(listRepository.save(list)).thenReturn(list);

        commons.List response = listService.saveList(list);

        verify(listRepository, times(1)).save(list);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);

        assertEquals(list, response);
    }

    @Test
    public void getListByIdTest(){
        commons.List list = new commons.List(1, new ArrayList<>(), "list 1", new Board());

        when(listRepository.findById(1L)).thenReturn(Optional.of(list));

        commons.List response = listService.getListById(1);

        verify(listRepository, times(1)).findById(1L);

        assertEquals(list, response);
    }

    @Test
    public void deleteListTest(){
        commons.List list = new commons.List(1, new ArrayList<>(), "list 1", new Board());

        listService.deleteList(list);

        verify(listRepository, times(1)).deleteById(1L);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);
    }

}