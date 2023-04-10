package server.api;

import commons.Card;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    CardService cardService;

    @InjectMocks
    CardController cardController;

    @Before
    public void initialize(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, "");

        when(cardService.saveCard(card1)).thenReturn(card1);

        ResponseEntity<Card> responseBad = cardController.add(card2);
        ResponseEntity<Card> response = cardController.add(card1);

        verify(cardService, times(1)).saveCard(card1);
        assertEquals(card1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad.getStatusCode());
    }

    @Test
    public void getAllTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, "Card 2");
        java.util.List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        when(cardService.getAllCards()).thenReturn(cards);

        java.util.List<Card> response = cardController.getAll();

        verify(cardService, times(1)).getAllCards();
        assertEquals(cards, response);
    }

    @Test
    public void getByIdTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, "Card 2");

        when(cardService.getCardById(1)).thenReturn(card1);
        when(cardService.getCardById(2)).thenReturn(card2);

        Card response1 = cardController.getById(1).getBody();
        Card response2 = cardController.getById(2).getBody();
        ResponseEntity<Card> responseBad1 = cardController.getById(3);
        ResponseEntity<Card> responseBad2 = cardController.getById(-1);

        verify(cardService, times(1)).getCardById(1);
        verify(cardService, times(1)).getCardById(2);

        assertEquals(card1, response1);
        assertEquals(card2, response2);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void deleteTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, null);

        when(cardService.getCardById(1)).thenReturn(card1);

        ResponseEntity<Card> response = cardController.delete(1);
        ResponseEntity<Card> responseBad1 = cardController.delete(2);
        ResponseEntity<Card> responseBad2 = cardController.delete(-1);

        verify(cardService, times(1)).deleteCard(card1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void updateTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, "");

        ResponseEntity<Card> response = cardController.update(card1);
        ResponseEntity<Card> responseBad1 = cardController.update(null);
        ResponseEntity<Card> responseBad2 = cardController.update(card2);

        verify(cardService, times(1)).saveCard(card1);

        assertEquals(card1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseBad2.getStatusCode());
    }

    @Test
    public void isNullOrEmptyTest(){
        assertFalse(cardController.isNullOrEmpty("card1"));
        assertTrue(cardController.isNullOrEmpty(""));
        assertTrue(cardController.isNullOrEmpty(null));
    }

    @Test
    public void getAllFromListTest(){
        Card card1 = new Card(1, "Card 1");
        Card card2 = new Card(2, "");
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        when(cardService.getCardsByListId(1)).thenReturn(cards);

        List<Card> response = cardController.getAllFromList(1);

        verify(cardService, times(1)).getCardsByListId(1);

        assertEquals(cards, response);
    }
}
