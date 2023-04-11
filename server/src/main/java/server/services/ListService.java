package server.services;

import commons.Card;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.ListRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ListService {
    private final ListRepository listRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * a constructor for list service
     * @param listRepository the list repository
     * @param messagingTemplate a template
     */
    @Autowired
    public ListService(ListRepository listRepository, SimpMessagingTemplate messagingTemplate) {
        this.listRepository = listRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets a list by its id
     * @param id its id
     * @return the list
     */
    public commons.List getListById(long id) {
        return listRepository.findById(id).get();
    }

    /**
     * stores a list to the repository
     * @param list the list saved
     * @return the list
     */
    public commons.List saveList(commons.List list) {
        commons.List savedList = listRepository.save(list);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedList;
    }

    /**
     * deletes a list
     * @param list the list
     */
    public void deleteList(commons.List list) {
        listRepository.deleteById(list.getID());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }

    /**
     * gets all lists on the repository
     * @return a list of lists
     */
    public List<commons.List> getAllLists(){
        List<commons.List> res = listRepository.findAll();
        Collections.sort(res, Comparator.comparingInt(commons.List::getNumberInTheBoard));
        for(commons.List list : res){
            Collections.sort(list.getCards(), Comparator.comparingInt(Card::getNumberInTheList));
            for(commons.Card card : list.getCards()){
                Collections.sort(card.getSubtasks(),
                        Comparator.comparingInt(Subtask::getNumberInTheCard));
            }
        }
        return res;
    }
}

