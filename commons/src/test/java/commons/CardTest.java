package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private ArrayList<Card> cards;
    private ArrayList<List> lists;
    private Board board;
    private List list;
    private Card card;

    /**
     * initializes all attributes for convenience and to avoid repetition
     */
    @BeforeEach
    void initialize() {
        cards = new ArrayList<>();
        lists = new ArrayList<>();
        board = new Board(1, lists, "board");
        list = new List(1, cards, "list", board);
        card = new Card("description", "name", list);
        cards.add(card);
        lists.add(list);
    }

    @Test
    void testConstructor() {
        Card emptyCard = new Card();
        assertNotNull(emptyCard);
        assertNotNull(card);
    }

    @Test
    void testEquals() {
        Card card2 = new Card("description", "name", list);
        Card card3 = new Card("name", "description", list);
        assertTrue(card.equals(card)); // same object
        assertTrue(card.equals(card2)); // different object, same values
        assertFalse(card.equals(card3)); // different values
    }

    @Test
    void getDescription() {
        assertTrue(card.getDescription().equals("description"));
    }

    @Test
    void setDescription() {
        card.setDescription("new");
        assertTrue(card.getDescription().equals("new"));
    }

    @Test
    void setName() {
        card.setName("new");
        assertTrue(card.getName().equals("new"));
    }
}