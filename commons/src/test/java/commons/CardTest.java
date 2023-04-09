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
        card = new Card("description", "name", list, 1);
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
        Card card2 = new Card("description", "name", list, 1);
        Card card3 = new Card("name", "description", list, 1);
        assertTrue(card.equals(card)); // same object
        assertTrue(card.equals(card2)); // different object, same values
        assertFalse(card.equals(card3)); // different values
    }

    @Test
    void getDescription() {
        assertTrue(card.getDescription().equals("description"));
    }

    @Test
    void getList() {
        assertEquals(list, card.getList());
    }

    @Test
    void getSubtasks() {
        assertEquals(card.subtasks, card.getSubtasks());
    }

    @Test
    void getId() {
        assertEquals(card.id, card.getId());
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

    @Test
    void setList() {
        List list2 = new List();
        card.setList(list2);
        assertEquals(list2, card.getList());
    }

    @Test
    void setSubtasks() {
        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        card.setSubtasks(subtasks2);
        assertEquals(subtasks2, card.getSubtasks());
    }

    @Test
    void setNumberInTheList() { // also tests getNumberInTheList
        card.setNumberInTheList(5);
        assertEquals(5, card.getNumberInTheList());
    }

}