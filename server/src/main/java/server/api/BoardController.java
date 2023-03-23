package server.api;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    BoardRepository repo;

    public BoardController(SimpMessagingTemplate messagingTemplate, BoardRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (board.name == null) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(saved);
    }

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

    @PostMapping("/update")
    public ResponseEntity<Board> updateBoard(@RequestBody Board board){
        repo.save(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(board);
    }

}
