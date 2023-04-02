package server.api;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.database.ListRepository;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    private final SimpMessagingTemplate messagingTemplate;
    private final Random random;
    @Autowired
    ListRepository repo;

    /**
     * constructor
     * @param random a random used for testing
     * @param repo the list repository
     * @param messagingTemplate the messagingTemplate used to trigger the websocket
     */
    public ListController(Random random, ListRepository repo,
                          SimpMessagingTemplate messagingTemplate) {
        this.random = random;
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets all the lists in the database
     * @return a list of the lists
     */
    @GetMapping(path = { "", "/" })
    public List<commons.List> getAll() {
        return repo.findAll();
    }

    /**
     * gets a list by its id
     * @param id the list id
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<commons.List> getById(@PathVariable("id") Long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * adds a list
     * @param list the list to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<commons.List> add(@RequestBody commons.List list) {
        if (list.name == null|| list.name.strip().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        commons.List saved = repo.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(list);
    }

    /**
     * deletes a list
     * @param id the id of the list to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        commons.List list=repo.getById(id);
        repo.delete(Objects.requireNonNull(getById(id).getBody()));
        messagingTemplate.convertAndSend("/topic/updates", true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updates a list
     * @param list the list to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<commons.List> updateList(@RequestBody commons.List list){
        System.out.println(list.getBoard());
        repo.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(list);
    }

    /**
     * gets a random for testing
     * @return a response (bad request or ok)
     */
    @GetMapping("rnd")
    public ResponseEntity<commons.List> getRandom() {
        var lists = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(lists.get(idx));
    }

}
