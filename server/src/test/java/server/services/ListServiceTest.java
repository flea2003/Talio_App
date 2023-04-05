package server.services;

import commons.Board;
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
        commons.List list1 = new commons.List(1, new ArrayList<>(), "list 1", new Board());
        commons.List list2 = new commons.List(2, new ArrayList<>(), "list 2", new Board());
        List<commons.List> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);

        when(listRepository.findAll()).thenReturn(lists);

        List<commons.List> response = listService.getAllLists();

        verify(listRepository, times(1)).findAll();

        assertEquals(lists, response);
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