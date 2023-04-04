package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private Card card;
    private Card card2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;

    /**
     * initializes all attributes for convenience and to avoid repetition
     */
    @BeforeEach
    void initialize() {
        subtask1 = new Subtask(1, "name", "description", 1, card);
        subtask2 = new Subtask(1, "name", "description", 1, card);
        subtask3 = new Subtask(1, "subtask", "description", 1, card);
    }

    @Test
    void testConstructor() {
        assertNotNull(subtask1);
    }

    @Test
    void getId() {
        assertEquals(1, subtask1.getId());
    }

    @Test
    void getName() {
        assertTrue("name".equals(subtask1.getName()));
    }

    @Test
    void getDescription() {
        assertTrue("description".equals(subtask1.getDescription()));
    }

    @Test
    void getNumberInTheCard() {
        assertEquals(1, subtask1.getNumberInTheCard());
    }

    @Test
    void getCard() {
        assertEquals(card, subtask1.getCard());
    }

    @Test
    void setId() {
        subtask1.setId(2);
        assertEquals(2, subtask1.getId());
    }

    @Test
    void setName() {
        subtask1.setName("subtask");
        assertTrue("subtask".equals(subtask1.getName()));
    }

    @Test
    void setDescription() {
        subtask1.setDescription("subtask");
        assertTrue("subtask".equals(subtask1.getDescription()));
    }

    @Test
    void setNumberInTheCard() {
        subtask1.setNumberInTheCard(5);
        assertEquals(5, subtask1.getNumberInTheCard());
    }

    @Test
    void setCard() {
        subtask1.setCard(card2);
        assertEquals(card2, subtask1.getCard());
    }

    @Test
    void testEquals1() { // same object
        assertEquals(subtask1, subtask1);
    }

    @Test
    void testEquals2() { // same values, different object
        assertEquals(subtask1, subtask2);
    }

    @Test
    void testEquals3() { // different values
        assertNotEquals(subtask1, subtask3);
    }

    @Test
    void testHashCode() {
        assertEquals(subtask1.hashCode(), subtask2.hashCode());
    }

}