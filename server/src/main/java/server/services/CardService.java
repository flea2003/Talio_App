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

    /**
     * constructor for this class
     * @param cardRepository a storage for cards
     * @param messagingTemplate a template
     */
    @Autowired
    public CardService(CardRepository cardRepository, SimpMessagingTemplate messagingTemplate) {
        this.cardRepository = cardRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets a card by its id
     * @param id its id
     * @return the Card
     */
    public Card getCardById(long id) {
        return cardRepository.findById(id).get();
    }

    /**
     * finds cards by an id
     * @param listId the id
     * @return a list of cards
     */
    public List<Card> getCardsByListId(long listId) {
        return cardRepository.findAllByListId(listId).orElse(null);
    }

    /**
     * stores a card
     * @param card a Card
     * @return the card that was saved
     */
    public Card saveCard(Card card) {
        Card savedCard = cardRepository.save(card);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedCard;
    }

    /**
     * deletes a card
     * @param card a Card
     */
    public void deleteCard(Card card) {
        cardRepository.deleteById(card.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }

    /**
     * gets all cards in a list
     * @return a list of cards
     */
    public List<Card> getAllCards(){
        List<Card> res = cardRepository.findAll();
        Collections.sort(res, Comparator.comparingInt(Card::getNumberInTheList));
        for(Card card : res){
            Collections.sort(card.getSubtasks(),
                    Comparator.comparingInt(Subtask::getNumberInTheCard));
        }
        return res;
    }
}
