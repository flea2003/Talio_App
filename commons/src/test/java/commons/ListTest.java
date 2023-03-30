package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

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
        List list1 = new List("name");
        List emptyList = new List();
        assertNotNull(emptyList);
        assertNotNull(list1);
    }

    @Test
    void getCards() {
        assertEquals(list.getCards(), cards);
    }

    @Test
    void testEquals() {
        List list2 = new List(1, cards, "list", board);
        List list3 = new List(2, cards, "list", board);
        assertTrue(list.equals(list)); // same object
        assertTrue(list.equals(list2)); // different object, same values
        assertFalse(list.equals(list3)); // different object, different values
    }

    @Test
    void testHashCode() {
        List list2 = new List(1, cards, "list", board);
        List list3 = new List(2, cards, "list", board);
        assertEquals(list.hashCode(), list2.hashCode()); // same values
        assertNotEquals(list.hashCode(), list3.hashCode()); // different values
    }

    @Test
    void testToString() {
        List list2 = new List(1, cards, "list", board);
        assertTrue(list.toString().equals(list2.toString()));
    }

    @Test
    void setCards() {
        ArrayList<Card> cards2 = new ArrayList<>();
        list.setCards(cards2);
        assertEquals(list.getCards(), cards2);
    }

    @Test
    void setName() {
        list.setName("new");
        assertTrue(list.getName().equals("new"));
    }

    @Test
    void getID() {
        assertEquals(list.getID(), (long)1);
    }
}