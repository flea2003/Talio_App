package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Board board;
    private Board board2;
    private ArrayList<Card> cardsTest;
    private Tag tag1;
    private Tag tag2;
    private Tag tag3;
    private Tag tag4;
    private Tag emptyTag;

    /**
     * initializes all attributes for convenience and to avoid repetition
     */
    @BeforeEach
    void initialize() {
        emptyTag = new Tag();
        board = new Board();
        board2 = new Board();
        cardsTest = new ArrayList<>();
        tag1 = new Tag( "name", 255, 0, 0, board, new ArrayList<>());
        tag2 = new Tag( "name", 255, 0, 0, board, new ArrayList<>());
        tag3 = new Tag( "name", 169, 118, 231, board, new ArrayList<>());
        tag4 = new Tag(1, "name", board);
    }

    @Test
    void constructorTest() {
        assertNotNull(emptyTag);
        assertNotNull(tag1);
    }

    @Test
    void getId() {
        assertEquals(1, tag4.getId());
    }

    @Test
    void setId() {
        tag1.setId(2);
        assertEquals(2, tag1.getId());
    }

    @Test
    void getName() {
        assertTrue("name".equals(tag1.getName()));
    }

    @Test
    void setName() {
        tag1.setName("tag");
        assertTrue("tag".equals(tag1.getName()));
    }


    @Test
    void getBoard() {
        assertEquals(board, tag1.getBoard());
    }

    @Test
    void setBoard() {
        tag1.setBoard(board2);
        assertEquals(board2, tag1.getBoard());
    }

    @Test
    void getCards() { // also tests setCards
        tag1.setCards(cardsTest);
        assertEquals(cardsTest, tag1.getCards());
    }

    @Test
    void testEquals1() { // same object
        assertTrue(tag1.equals(tag1));
    }

    @Test
    void testEquals2() { // same values, different object
        assertTrue(tag1.equals(tag2));
    }

    @Test
    void testEquals3() { // different values
        assertFalse(tag1.equals(tag3));
    }

    @Test
    void testHashCode() {
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testToString() {
        assertTrue(tag1.toString().equals(tag2.toString()));
    }
}