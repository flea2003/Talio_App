package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.ListRepository;

import java.util.List;

@Service
public class ListService {
    private final ListRepository listRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ListService(ListRepository listRepository, SimpMessagingTemplate messagingTemplate) {
        this.listRepository = listRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<commons.List> getAllLists() {
        return listRepository.findAll();
    }

    public commons.List getListById(long id) {
        return listRepository.findById(id).get();
    }

    public commons.List saveList(commons.List list) {
        commons.List savedList = listRepository.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedList;
    }

    public void deleteList(commons.List list) {
        listRepository.deleteById(list.getID());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }
}

