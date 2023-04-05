package server.services;

import commons.Card;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CardService(CardRepository cardRepository, SimpMessagingTemplate messagingTemplate) {
        this.cardRepository = cardRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Card getCardById(long id) {
        return cardRepository.findById(id).get();
    }

    public List<Card> getCardsByListId(long listId) {
        return cardRepository.findAllByListId(listId).orElse(null);
    }

    public Card saveCard(Card card) {
        Card savedCard = cardRepository.save(card);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedCard;
    }

    public void deleteCard(Card card) {
        cardRepository.deleteById(card.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }

    public List<Card> getAllCards(){
        List<Card> res = cardRepository.findAll();
        Collections.sort(res, Comparator.comparingInt(Card::getNumberInTheList));
        for(Card card : res){
            Collections.sort(card.getSubtasks() , Comparator.comparingInt(Subtask::getNumberInTheCard));
        }
        return res;
    }
}
