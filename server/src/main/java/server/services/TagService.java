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

    /**
     * constructor of a tag service
     * @param tagRepository the storage of tags
     * @param messagingTemplate a template
     */
    @Autowired
    public TagService(TagRepository tagRepository, SimpMessagingTemplate messagingTemplate) {
        this.tagRepository = tagRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets a tag by its id
     * @param id its id
     * @return the response entity of a tag
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") long id) {
        Tag tag = tagRepository.getById(id);
        if (id < 0 || tag == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tag);
    }

    /**
     * gets everything in the tag repository
     * @return the list of cards
     */
    public java.util.List<Tag> getAll(){
        return tagRepository.findAll();
    }

    /**
     * gets all cards' tags
     * @param id the id used to get the tags
     * @return the list of tags
     */
    public java.util.List<Tag> getAllTagsForCard(long id){
        return tagRepository.findByCardsTagsId(id);
    }

    /**
     * gets all boards' tags
     * @param id the id used to get the tags
     * @return the list of tags
     */
    public java.util.List<Tag>getAllTagsForBoard(long id){
        return tagRepository.findAllByBoardId(id).orElse(null);
    }

    /**
     * gets a tag by its id
     * @param id its id
     * @return the Tag
     */
    public Tag getSubtaskById(long id) {
        return tagRepository.findById(id).get();
    }

    /**
     * stores a tag on the repository
     * @param tag the tag
     * @return the saved tag
     */
    public Tag saveTag(Tag tag){
        Tag savedTag = tagRepository.save(tag);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedTag;
    }

    /**
     * deletes a tag
     * @param tag the tag
     */
    public void deleteTag(Tag tag) {
        tagRepository.deleteById(tag.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }


}
