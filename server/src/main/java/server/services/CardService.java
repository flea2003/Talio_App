package server.services;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

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

    public List<Card> getAllCards() {
        return cardRepository.findAll();
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
}
