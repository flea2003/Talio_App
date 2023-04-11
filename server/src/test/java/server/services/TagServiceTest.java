package server.services;

import commons.Subtask;
import commons.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.SubtaskRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    TagRepository tagRepository;
    @Mock
    SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    TagService tagService;

    @Test
    void contructorTest() {
        TagService tagService = new TagService(tagRepository, messagingTemplate);
        assertNotNull(tagService);
    }
    @Test
    void getTagById() {
        Tag tag = new Tag(1, "name");

        when(tagRepository.getById(1L)).thenReturn(tag);

        Tag response = tagService.getTagById(1).getBody();

        verify(tagRepository, times(0)).findById(1L);

        assertEquals(tag, response);
    }

    @Test
    void getAll() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag(1, "tag1");
        Tag tag2 = new Tag(2, "tag2");
        tags.add(tag1);
        tags.add(tag2);

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> response = tagService.getAll();

        verify(tagRepository, times(1)).findAll();

        assertEquals(tags, response);
    }

    @Test
    void getSubtaskById() {
        Tag tag = new Tag(1, "name");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Tag response = tagService.getSubtaskById(1);

        verify(tagRepository, times(1)).findById(1L);

        assertEquals(tag, response);
    }

    @Test
    void saveTag() {
        Tag tag1 = new Tag(1, "tag1");

        when(tagRepository.save(tag1)).thenReturn(tag1);

        Tag response = tagService.saveTag(tag1);

        verify(tagRepository, times(1)).save(tag1);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);

        assertEquals(tag1, response);
    }

    @Test
    void deleteTag() {
        Tag tag = new Tag(1, "name");

        tagService.deleteTag(tag);

        verify(tagRepository, times(1)).deleteById(1L);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);
    }
}