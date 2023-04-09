package server.api;

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
import server.services.SubtaskService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskControllerTest {

    @Mock
    SubtaskService subtaskService;

    @InjectMocks
    SubtaskController subtaskController;

    @Before
    public void initialize(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addSubtaskTest(){
        Subtask subtask1 = new Subtask(1,1);
        Subtask subtask2 = new Subtask(2,2);
        subtask1.setName("name");
        subtask2.setName(" ");

        when(subtaskService.saveSubtask(subtask1)).thenReturn(subtask1);

        ResponseEntity<Subtask> responseBad = subtaskController.add(subtask2);
        ResponseEntity<Subtask> response = subtaskController.add(subtask1);

        verify(subtaskService, times(1)).saveSubtask(subtask1);
        assertEquals(subtask1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad.getStatusCode());
    }

    @Test
    public void getAllTest(){
        Subtask subtask1 = new Subtask(1,1);
        Subtask subtask2 = new Subtask(2,2);
        java.util.List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        when(subtaskService.getAllSubtasks()).thenReturn(subtasks);

        java.util.List<Subtask> response = subtaskController.getAll();

        verify(subtaskService, times(1)).getAllSubtasks();
        assertEquals(subtasks, response);
    }

    @Test
    public void getByIdTest(){
        Subtask subtask1 = new Subtask(1,1);
        Subtask subtask2 = new Subtask(2,2);

        when(subtaskService.getSubtaskById(1)).thenReturn(subtask1);
        when(subtaskService.getSubtaskById(2)).thenReturn(subtask2);

        Subtask response1 = subtaskController.getById(1).getBody();
        Subtask response2 = subtaskController.getById(2).getBody();
        ResponseEntity<Subtask> responseBad1 = subtaskController.getById(3);
        ResponseEntity<Subtask> responseBad2 = subtaskController.getById(-1);

        verify(subtaskService, times(2)).getSubtaskById(1);
        verify(subtaskService, times(2)).getSubtaskById(2);

        assertEquals(subtask1, response1);
        assertEquals(subtask2, response2);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }


    @Test
    public void deleteTest(){
        Subtask subtask = new Subtask(1,1);

        when(subtaskService.getSubtaskById(1)).thenReturn(subtask);

        ResponseEntity<Void> response = subtaskController.delete(1);
        ResponseEntity<Void> responseBad1 = subtaskController.delete(2);
        ResponseEntity<Void> responseBad2 = subtaskController.delete(-1);

        verify(subtaskService, times(1)).deleteSubtask(subtask);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }


    @Test
    public void updateTest(){
        Subtask subtask = new Subtask(1,1);

        ResponseEntity<Subtask> response = subtaskController.updateSubtask(subtask);

        verify(subtaskService, times(1)).saveSubtask(subtask);

        assertEquals(subtask, response.getBody());
    }
}
