package server.api;

import commons.Board;
import commons.Card;
import commons.List;
import commons.Subtask;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.ListService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListControllerTest {

    @Mock
    ListService listService;

    @Mock
    CardController cardController;

    @InjectMocks
    ListController listController;

    @Before
    public void initialize(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTest(){
        List list1 = new List(1, new ArrayList<Card>(), "List 1", new Board());
        List list2 = new List(2, new ArrayList<Card>(), null, new Board());
        List list3 = new List(3, new ArrayList<Card>(), "  ", new Board());

        when(listService.saveList(list1)).thenReturn(list1);

        ResponseEntity<List> responseBad1 = listController.add(list2);
        ResponseEntity<List> responseBad2 = listController.add(list3);
        ResponseEntity<List> response = listController.add(list1);

        verify(listService, times(1)).saveList(list1);
        assertEquals(list1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void getAllTest(){
        List list1 = new List(1, new ArrayList<Card>(), "List 1", new Board());
        List list2 = new List(2, new ArrayList<Card>(), "List 2", new Board());
        java.util.List<List> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);

        when(listService.getAllLists()).thenReturn(lists);

        java.util.List<List> response = listController.getAll();

        verify(listService, times(1)).getAllLists();
        assertEquals(lists, response);
    }

    @Test
    public void getByIdTest(){
        List list1 = new List(1, new ArrayList<Card>(), "List 1", new Board());
        List list2 = new List(2, new ArrayList<Card>(), "List 2", new Board());

        when(listService.getListById(1)).thenReturn(list1);
        when(listService.getListById(2)).thenReturn(list2);

        List response1 = listController.getById(1).getBody();
        List response2 = listController.getById(2).getBody();
        ResponseEntity<List> responseBad1 = listController.getById(3);
        ResponseEntity<List> responseBad2 = listController.getById(-1);

        verify(listService, times(1)).getListById(1);
        verify(listService, times(1)).getListById(2);

        assertEquals(list1, response1);
        assertEquals(list2, response2);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void deleteTest(){
        java.util.List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask1 = new Subtask(1, 2);
        Subtask subtask2 = new Subtask(2, 1);
        subtasks.add(subtask1);
        subtasks.add((subtask2));

        java.util.List<Card> cards = new ArrayList<>();

        Card card1 = new Card();
        card1.setNumberInTheList(2);
        card1.setSubtasks(subtasks);

        Card card2 = new Card();
        card2.setNumberInTheList(1);
        card2.setSubtasks(new ArrayList<>());

        cards.add(card1);
        cards.add(card2);

        List list1 = new List(1, (ArrayList<Card>) cards, "List 1", new Board());

        when(listService.getListById(1)).thenReturn(list1);

        ResponseEntity<Void> response = listController.delete(1);
        ResponseEntity<Void> responseBad1 = listController.delete(2);
        ResponseEntity<Void> responseBad2 = listController.delete(-1);

        verify(listService, times(1)).deleteList(list1);
        verify(cardController, times(1)).activateListeners(card1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void updateListTest(){
        java.util.List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask1 = new Subtask(1, 2);
        Subtask subtask2 = new Subtask(2, 1);
        subtasks.add(subtask1);
        subtasks.add((subtask2));

        java.util.List<Card> cards = new ArrayList<>();

        Card card1 = new Card();
        card1.setNumberInTheList(2);
        card1.setSubtasks(subtasks);

        Card card2 = new Card();
        card2.setNumberInTheList(1);
        card2.setSubtasks(new ArrayList<>());

        cards.add(card1);
        cards.add(card2);

        List list = new List(1, (ArrayList<Card>) cards, "List 1", new Board());

        ResponseEntity<List> response = listController.updateList(list);

        verify(listService, times(1)).saveList(list);
        verify(cardController, times(1)).activateListeners(card1);

        assertEquals(list, response.getBody());
    }
}
