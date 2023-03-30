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
    private ArrayList<Subtask> subtasks;

    /**
     * initializes all attributes for convenience and to avoid repetition
     */
    @BeforeEach
    void initialize() {
        cards = new ArrayList<>();
        lists = new ArrayList<>();
        subtasks = new ArrayList<>();

        board = new Board(1, lists, "board");
        list = new List(1, cards, "list", board);
        card = new Card(subtasks, "description", "name", list, 1);
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
        Card card2 = new Card(subtasks, "description", "name", list, 1);
        Card card3 = new Card(subtasks, "name", "description", list, 1);
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