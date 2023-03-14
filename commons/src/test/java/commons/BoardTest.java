package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void checkConstructor() {
        var list=new ArrayList<commons.List>();
        var p = new Board(1, list,"a" );
        assertEquals("a", p.name);
        assertEquals(new ArrayList<commons.List>(), p.lists);
    }

    @Test
    public void equalsHashCode() {
        var list=new ArrayList<commons.List>();
        var a = new Board(1, list,"a" );
        var b = new Board(1,list,"a" );
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var list=new ArrayList<commons.List>();
        var list2=new ArrayList<commons.List>();
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
}