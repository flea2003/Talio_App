package server.api;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.ListRepository;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final Random random;
    private final ListRepository repo;

    public ListController(Random random, ListRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<commons.List> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<commons.List> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<commons.List> add(@RequestBody commons.List list) {

        if (list.name == null) {
            return ResponseEntity.badRequest().build();
        }
        commons.List saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {

        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        commons.List list=repo.getById(id);
        repo.delete(Objects.requireNonNull(getById(id).getBody()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changeName/{id}")
    public ResponseEntity<commons.List> changeName(@PathVariable("id") long id, @RequestBody String name){

        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        commons.List list=repo.getById(id);
        repo.getById(id).setName(name);
        return ResponseEntity.ok(list);
    }

    @GetMapping("rnd")
    public ResponseEntity<commons.List> getRandom() {
        var quotes = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(quotes.get(idx));
    }
}