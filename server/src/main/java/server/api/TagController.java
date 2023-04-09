package server.api;

import commons.Subtask;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.SubtaskService;
import server.services.TagService;

import java.util.Objects;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    TagService tagService;

    /**
     * gets all the tags in the database
     * @return a list of the tags
     */
    @GetMapping(path = { "", "/" })
    public java.util.List<Tag> getAll() {
        return tagService.getAll();
    }

    /**
     * gets all the tags for a given card in the database
     * @return a list of the tags for a given card
     */
    @PostMapping(path = { "/card" })
    public java.util.List<Tag> getAllTagsForCard(long id) {
        return tagService.getAllTagsForCard(id);
    }

    /**
     * gets all the tags for a given board in the database
     * @return a list of the tags for a given board
     */
    @PostMapping(path = { "/board" })
    public java.util.List<Tag> getAllTagsForBoard(long id) {
        return tagService.getAllTagsForBoard(id);
    }

    /**
     * adds a tag
     * @param tag the tag to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<commons.Tag> add(@RequestBody commons.Tag tag) {
        if (tag.getName() == null || tag.getName().strip().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        commons.Tag saved = tagService.saveTag(tag);
        return ResponseEntity.ok(tag);
    }

    /**
     * deletes a tag
     * @param id the id of the tag to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        commons.Subtask subtask = tagService.getTagById(id);
        if (id < 0 || subtask == null) {
            return ResponseEntity.badRequest().build();
        }
        tagService.deleteTag(Objects.requireNonNull(getById(id).getBody()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updates a tag
     * @param tag the tag to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<commons.Tag> updateTag(@RequestBody commons.Tag tag){
        tagService.saveTag(tag);
        return ResponseEntity.ok(tag);
    }
}
