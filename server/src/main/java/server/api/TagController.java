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
        return tagService.getAllTags();
    }

    String endpoint = String.format("api/tags/card/%d", id);

    /**
     * gets all the tags for a given card in the database
     * @return a list of the tags for a given card
     */
    @PostMapping(path = { "/card" })
    public java.util.List<Tag> getAll(long id) {
        return tagService.getAllTags();
    }

    /**
     * gets all the tags in the database
     * @return a list of the tags ('_')
     */
    @GetMapping(path = { "", "/" })
    public java.util.List<Tag> getAll() {
        return tagService.getAllTags();
    }




    /**
     * gets a subtask by its id
     * @param id the subtask id
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getById(@PathVariable("id") long id) {
        Subtask subtask = subtaskService.getSubtaskById(id);
        if (id < 0 || subtask == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(subtaskService.getSubtaskById(id));
    }

    /**
     * adds a subtask
     * @param subtask the subtask to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<commons.Subtask> add(@RequestBody commons.Subtask subtask) {
        if (subtask.getName() == null|| subtask.getName().strip().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        commons.Subtask saved = subtaskService.saveSubtask(subtask);
        return ResponseEntity.ok(subtask);
    }

    /**
     * deletes a subtask
     * @param id the id of the subtask to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        commons.Subtask subtask = subtaskService.getSubtaskById(id);
        if (id < 0 || subtask == null) {
            return ResponseEntity.badRequest().build();
        }
        subtaskService.deleteSubtask(Objects.requireNonNull(getById(id).getBody()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updates a subtask
     * @param subtask the subtask to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<commons.Subtask> updateSubtask(@RequestBody commons.Subtask subtask){
        subtaskService.saveSubtask(subtask);
        return ResponseEntity.ok(subtask);
    }
}
