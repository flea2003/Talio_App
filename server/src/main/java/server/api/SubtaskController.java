package server.api;

import commons.Board;
import commons.List;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.SubtaskRepository;

@RestController
@RequestMapping("/api/subtask")
public class SubtaskController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    SubtaskRepository repo;

    @Autowired
    public SubtaskController(SimpMessagingTemplate messagingTemplate, SubtaskRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    @PostMapping(path = { "/delete", "/" })
    public void delete(@RequestBody commons.Subtask subtask) {
        repo.delete(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return;
    }

    public ResponseEntity<Subtask> update(@RequestBody commons.Subtask subtask){
//        System.out.println(subtask);
        repo.save(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(subtask);
    }
}
