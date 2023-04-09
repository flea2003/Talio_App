package server.api;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    @Autowired
    BoardService boardService;

    /**
     * gets all the boards
     * @return th list of boards
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardService.getAllBoards();
    }

    /**
     * get a board by its id
     * @param id the id of the board
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        Board board = boardService.getBoardById(id);
        if (id < 0 || board == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(board);
    }

    /**
     * get the board by its key
     * @param key the key of the board
     * @return a response (bad request or ok)
     */
    @GetMapping("/key/{key}")
    public ResponseEntity<Board> getByKey(@PathVariable("key") String key) {
        Board board = boardService.getBoardByKey(key);
        if(board == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(board);

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
        Board saved = boardService.saveBoard(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * deletes a board
     * @param id the id of the board to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Board board = boardService.getBoardById(id);

        if (id < 0 || board == null) {
            return ResponseEntity.badRequest().build();
        }
        boardService.deleteBoard(board);
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
        Board board = boardService.getBoardById(id);

        if (id < 0 || board == null) {
            return ResponseEntity.badRequest().build();
        }
        boardService.getBoardById(id).setName(name);
        return ResponseEntity.ok(board);
    }

    /**
     * updates a board
     * @param board the board to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<Board> updateBoard(@RequestBody Board board){
        boardService.saveBoard(board);
        return ResponseEntity.ok(board);
    }

}
