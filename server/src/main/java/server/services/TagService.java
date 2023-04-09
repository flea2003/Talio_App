package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.TagRepository;

public class TagService {
    private final TagRepository tagRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public TagService(TagRepository tagRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.tagRepository = tagRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


}
