package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void checkConstructor() {
        var list = new ArrayList<commons.List>();
        var p = new Board(1, list,"a" );
        Board emptyBoard = new Board();
        assertEquals("a", p.name);
        assertEquals(new ArrayList<commons.List>(), p.lists);
        assertNotNull(emptyBoard);
    }

    @Test
    public void checkConstructor2() {
        Board board = new Board("name");
        assertNotNull(board);
    }

    @Test
    public void equalsHashCode() {
        var list = new ArrayList<commons.List>();
        var a = new Board(1, list,"a" );
        var b = new Board(1,list,"a" );
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var list = new ArrayList<commons.List>();
        var list2 = new ArrayList<commons.List>();
        list2.add(new commons.List());
        var a = new Board(1, list,"a" );
        var b = new Board(1,list,"b" );
        var c = new Board(1, list2,"a" );
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    public void hasToString() {
        var list=new ArrayList<commons.List>();
        var actual = new Board(1,list,"a" ).toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("name"));
    }

    @Test
    public void testGetId() {
        Board board = new Board(1, new ArrayList<List>(), "name");
        assertEquals(1, board.getId());
    }

    @Test
    public void testGetKey() { // also tests onCreate
        Board board = new Board(1, new ArrayList<List>(), "name");
        board.onCreate();
        assertTrue(board.key.equals(board.getKey()));
    }

    @Test
    public void testSetLists() {
        Board board = new Board(1, new ArrayList<List>(), "name");
        ArrayList<List> lists = new ArrayList<>();
        board.setLists(lists);
        assertEquals(board.getLists(), lists);
    }

    @Test
    public void testSetName() {
        Board board = new Board(1, new ArrayList<List>(), "name");
        board.setName("new");
        assertTrue(board.getName().equals("new"));
    }

    @Test
    public void testGetTags() { // also tests set tags
        Board board = new Board(1, new ArrayList<List>(), "name");
        ArrayList<Tag> tags2 = new ArrayList<>();
        board.setTags(tags2);
        assertEquals(tags2, board.getTags());
    }

    @Test
    public void testSetKey() {
        Board board = new Board(1, new ArrayList<List>(), "name");
        board.setKey("key2");
        assertTrue("key2".equals(board.getKey()));
    }
}
