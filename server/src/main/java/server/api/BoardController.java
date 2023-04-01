package server.api;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    BoardRepository repo;

    /**
     * constructor
     * @param messagingTemplate the messagingTemplate used to trigger the websocket
     * @param repo the board repository
     */
    public BoardController(SimpMessagingTemplate messagingTemplate, BoardRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    /**
     * gets all the boards
     * @return th list of boards
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * get a board by its id
     * @param id the id of the board
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * get the board by its key
     * @param key the key of the board
     * @return a response (bad request or ok)
     */
    @GetMapping("/key/{key}")
    public ResponseEntity<Board> getByKey(@PathVariable("key") String key) {
        Optional<Board> board = repo.findBoardByKey(key);
        if(board.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(board.get());

    }

    /**
     * adds a board
     * @param board the board to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (board.name == null) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(saved);
    }

    /**
     * deletes a board
     * @param id the id of the board to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {

        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board board = repo.getById(id);
        repo.delete(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * changes the name of a board
     * @param id the id of the board to be changed
     * @param name the new name of the board
     * @return a response (bad request or ok)
     */
    @PostMapping("/changeName/{id}")
    public ResponseEntity<Board> changeName(@PathVariable("id") long id,@RequestBody String name){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board board=repo.getById(id);
        repo.getById(id).setName(name);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(board);
    }

    /**
     * updates a board
     * @param board the board to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<Board> updateBoard(@RequestBody Board board){
        repo.save(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(board);
    }

}
