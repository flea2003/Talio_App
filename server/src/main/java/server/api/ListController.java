package server.api;

import java.util.*;

import commons.Quote;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.AbstractDestinationResolvingMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    public ListController(Random random, ListRepository repo, SimpMessagingTemplate messagingTemplate) {
        this.random = random;
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
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
        if (list.name == null|| list.name.strip().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        commons.List saved = repo.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(list);
    }

    @MessageMapping("/addlist")
    @SendTo("/topic/updates")
    public commons.List addNewList(commons.List q){
        add(q);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return q;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        commons.List list=repo.getById(id);
        repo.delete(Objects.requireNonNull(getById(id).getBody()));
        messagingTemplate.convertAndSend("/topic/updates", true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/changeName/{id}")
//    public ResponseEntity<commons.List> changeName(@PathVariable("id") long id, @RequestBody String name){
//        if (id < 0 || !repo.existsById(id) || name == null || name.trim().isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//        commons.List list=repo.getById(id);
//        repo.getById(id).setName(name);
//        repo.save(list);
//        messagingTemplate.convertAndSend("/topic/updates", true);
//        return ResponseEntity.ok(list);
//    }

    @PostMapping("/update")
    public ResponseEntity<commons.List> updateList(@RequestBody commons.List list){
        System.out.println(list.getBoard());
        repo.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(list);
    }


    @GetMapping("rnd")
    public ResponseEntity<commons.List> getRandom() {
        var quotes = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(quotes.get(idx));
    } // huh ?

    @MessageMapping("/lists")
    @SendTo("/topic/lists")
    public commons.List addList(commons.List list){
        System.out.println("hello");
        add(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return list;
    }
}
