package server.api;

import java.util.*;

import commons.List;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.database.ListRepository;
import server.database.SubtaskRepository;

@RestController
@RequestMapping("/api/subtask")
public class SubtaskController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    SubtaskRepository repo;

    /**
     * constructor
     * @param repo the subtask repository
     * @param messagingTemplate the messagingTemplate used to trigger the websocket
     */
    public SubtaskController(SubtaskRepository repo,
                          SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets all the subtasks in the database
     * @return a subtasks of the lists
     */
    @GetMapping(path = { "", "/" })
    public java.util.List<Subtask> getAll() {
        return repo.findAll();
    }

    /**
     * gets a subtask by its id
     * @param id the subtask id
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<commons.Subtask> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
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
        commons.Subtask saved = repo.save(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(subtask);
    }

    /**
     * deletes a subtask
     * @param id the id of the subtask to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        commons.Subtask subtask=repo.getById(id);
        repo.delete(Objects.requireNonNull(getById(id).getBody()));
        messagingTemplate.convertAndSend("/topic/updates", true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updates a subtask
     * @param subtask the subtask to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<commons.Subtask> updateSubtask(@RequestBody commons.Subtask subtask){
        repo.save(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(subtask);
    }

}





