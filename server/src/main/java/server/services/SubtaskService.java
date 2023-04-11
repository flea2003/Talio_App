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

    /**
     * constructor for a subtask service
     * @param subtaskRepository the repository of subtasks
     * @param messagingTemplate a template
     */
    @Autowired
    public SubtaskService(SubtaskRepository subtaskRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.subtaskRepository = subtaskRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets a subtask on the repository by its id
     * @param id its id
     * @return the subtask
     */
    public Subtask getSubtaskById(long id) {
        return subtaskRepository.findById(id).get();
    }

    /**
     * stores a subtask in the repository
     * @param subtask the subtask stored
     * @return the subtask that was saved
     */
    public Subtask saveSubtask(Subtask subtask) {
        Subtask savedSubtask = subtaskRepository.save(subtask);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedSubtask;
    }

    /**
     * deletes a subtask
     * @param subtask the subtask
     */
    public void deleteSubtask(Subtask subtask) {
        subtaskRepository.deleteById(subtask.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }

    /**
     * gets all subtasks in the repository
     * @return a list of subtasks
     */
    public java.util.List<Subtask> getAllSubtasks() {
        return subtaskRepository.findAll();
    }
}
