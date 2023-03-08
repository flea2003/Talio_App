package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    public void checkConstructor() {
        var boards=new ArrayList<Board>() ;
        var p = new User("a",boards );
        assertEquals("a", p.username);
        assertEquals(new ArrayList<Board>(), p.boards);
    }

    @Test
    public void equalsHashCode() {
        var boards=new ArrayList<Board>();
        var a = new User("a",boards );
        var b = new User("a",boards );
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var boards=new ArrayList<Board>();
        var boards2=new ArrayList<Board>();
        boards2.add(new Board(1,new ArrayList<Board>(),"a"));
        var a = new User("a",boards );
        var b = new User("b",boards );
        var c= new User("a",boards2);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    public void hasToString() {
        var boards=new ArrayList<Board>();
        var actual = new User("a",boards ).toString();
        assertTrue(actual.contains(User.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("username"));
    }
}
