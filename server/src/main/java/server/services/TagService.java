package server.services;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import server.database.TagRepository;

@Service
public class TagService {
    private final TagRepository tagRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TagService(TagRepository tagRepository, SimpMessagingTemplate messagingTemplate) {
        this.tagRepository = tagRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") long id) {
        Tag tag = tagRepository.getById(id);
        if (id < 0 || tag == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tag);
    }

    public java.util.List<Tag> getAll(){
        return tagRepository.findAll();
    }

    public Tag getSubtaskById(long id) {
        return tagRepository.findById(id).get();
    }

    public Tag saveTag(Tag tag){
        Tag savedTag = tagRepository.save(tag);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedTag;
    }

    public void deleteTag(Tag tag) {
        tagRepository.deleteById(tag.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }


}
