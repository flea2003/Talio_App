package server.services;

import commons.Board;
import commons.Card;
import commons.Subtask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    CardService cardService;

    @Test
    public void constructorTest(){
        CardService cardService = new CardService(cardRepository,messagingTemplate);
        assertNotNull(cardService);
    }

    @Test
    public void getAllCardsTest(){
        List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask1 = new Subtask(1, 2);
        Subtask subtask2 = new Subtask(2, 1);
        subtasks.add(subtask1);
        subtasks.add((subtask2));

        Card card1 = new Card(1, "card 1");
        Card card2 = new Card(2, "card 2");

        card1.setSubtasks(subtasks);
        card2.setSubtasks(new ArrayList<>());

        card1.setNumberInTheList(2);
        card2.setNumberInTheList(1);
        
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        when(cardRepository.findAll()).thenReturn(cards);

        List<Card> response = cardService.getAllCards();

        verify(cardRepository, times(1)).findAll();

        assertEquals(cards, response);

        //check that the cards are sorted by their number in the list
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));

        //check that the subtasks are sorted by their number in the card
        assertEquals(subtask2, card1.getSubtasks().get(0));
        assertEquals(subtask1, card1.getSubtasks().get(1));
    }

    @Test
    public void saveCardTest(){
        Card card = new Card(1, "card 1");

        when(cardRepository.save(card)).thenReturn(card);

        Card response = cardService.saveCard(card);

        verify(cardRepository, times(1)).save(card);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);

        assertEquals(card, response);
    }

    @Test
    public void getCardByIdTest(){
        Card card = new Card(1, "card 1");

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card response = cardService.getCardById(1);

        verify(cardRepository, times(1)).findById(1L);

        assertEquals(card, response);
    }

    @Test
    public void deleteCardTest(){
        Card card = new Card(1, "card 1");

        cardService.deleteCard(card);

        verify(cardRepository, times(1)).deleteById(1L);
        verify(messagingTemplate).convertAndSend("/topic/updates", true);
    }

    @Test
    public void getCardByListId(){
        Card card1 = new Card(1, "card 1");
        Card card2 = new Card(2, "card 2");
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        commons.List list =new commons.List(2L, (ArrayList<Card>) cards, "list 1", new Board());

        card1.setList(list);
        card2.setList(list);

        when(cardRepository.findAllByListId(2L)).thenReturn(Optional.of(cards));

        List<Card> response = cardService.getCardsByListId(2);

        verify(cardRepository, times(1)).findAllByListId(2L);

        assertEquals(cards, response);
    }
}
