package server.api;

import commons.Board;
import commons.Subtask;
import commons.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.TagService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    TagService tagService;

    @InjectMocks
    TagController tagController;

    @Before
    public void initialize(){
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    public void getAllTest(){
        Tag tag1 = new Tag(1, "tag1");
        Tag tag2 = new Tag(2, "tag2");

        java.util.List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        when(tagService.getAll()).thenReturn(tags);

        java.util.List<Tag> response = tagController.getAll();
        verify(tagService, times(1)).getAll();

        assertEquals(tags, response);
    }

    @org.junit.jupiter.api.Test
    void add() {
        Tag tag1 = new Tag(1, "tag1");
        Tag tag2 = new Tag(2, "  ");

        when(tagService.saveTag(tag1)).thenReturn(tag1);

        ResponseEntity<Tag> response = tagController.add(tag1);
        ResponseEntity<Tag> responseBad = tagController.add(tag2);
        Tag res = response.getBody();
        verify(tagService, times(1)).saveTag(tag1);

        assertEquals(tag1, res);
        assertEquals(responseBad.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @org.junit.jupiter.api.Test
    void delete() {
        Tag tag1 = new Tag(1, "tag1");
        Tag tag2 = new Tag(2, "  ");

        when(tagService.getTagById(1)).thenReturn(ResponseEntity.ok(tag1));

        ResponseEntity<Void> response = tagController.delete(1);
        ResponseEntity<Void> responseBad2 = tagController.delete(-1);

        verify(tagService, times(1)).deleteTag(tag1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    void updateTag() {
        Tag tag1 = new Tag(1, "tag1");

        ResponseEntity<Tag> response = tagController.updateTag(tag1);

        verify(tagService, times(1)).saveTag(tag1);

        assertEquals(tag1, response.getBody());
    }
}