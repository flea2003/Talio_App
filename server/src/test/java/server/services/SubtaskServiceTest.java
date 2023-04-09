package server.services;

import commons.Subtask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.SubtaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskServiceTest {

    @Mock
    SubtaskRepository subtaskRepository;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    SubtaskService subtaskService;

    @Test
    public void constructorTest() {
        SubtaskService subtaskService = new SubtaskService(subtaskRepository, messagingTemplate);
        assertNotNull(subtaskService);
    }

    @Test
    public void getAllSubtasksTest() {
        List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask1 = new Subtask(1, 2);
        Subtask subtask2 = new Subtask(2, 1);
        subtasks.add(subtask1);
        subtasks.add((subtask2));

        when(subtaskRepository.findAll()).thenReturn(subtasks);

        List<Subtask> response = subtaskService.getAllSubtasks();

        verify(subtaskRepository, times(1)).findAll();

        assertEquals(subtasks, response);
    }

    @Test
    public void saveSubtaskTest() {
        Subtask subtask = new Subtask(1, 2);

        when(subtaskRepository.save(subtask)).thenReturn(subtask);

        Subtask response = subtaskService.saveSubtask(subtask);

        verify(subtaskRepository, times(1)).save(subtask);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);

        assertEquals(subtask, response);
    }

    @Test
    public void getSubtaskByIdTest() {
        Subtask subtask = new Subtask(1, 2);

        when(subtaskRepository.findById(1L)).thenReturn(Optional.of(subtask));

        Subtask response = subtaskService.getSubtaskById(1);

        verify(subtaskRepository, times(1)).findById(1L);

        assertEquals(subtask, response);
    }

    @Test
    public void deleteSubtaskTest() {
        Subtask subtask = new Subtask(1, 2);

        subtaskService.deleteSubtask(subtask);

        verify(subtaskRepository, times(1)).deleteById(1L);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);
    }

}
