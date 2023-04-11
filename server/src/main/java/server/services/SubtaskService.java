package server.services;

import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.SubtaskRepository;

@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SubtaskService(SubtaskRepository subtaskRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.subtaskRepository = subtaskRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Subtask getSubtaskById(long id) {
        return subtaskRepository.findById(id).get();
    }

    public Subtask saveSubtask(Subtask subtask) {
        Subtask savedSubtask = subtaskRepository.save(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedSubtask;
    }

    public void deleteSubtask(Subtask subtask) {
        subtaskRepository.deleteById(subtask.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }


    public java.util.List<Subtask> getAllSubtasks() {
        return subtaskRepository.findAll();
    }
}
