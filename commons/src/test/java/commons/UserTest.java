package commons;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    public void checkConstructor() {
        var boards=new HashMap<Long,Board>();
        var p = new User("a",boards );
        assertEquals("a", p.username);
        assertEquals(new HashMap<Long,Board>(), p.boards);
    }

    @Test
    public void equalsHashCode() {
        var boards=new HashMap<Long,Board>();
        var a = new User("a",boards );
        var b = new User("a",boards );
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var boards=new HashMap<Long,Board>();
        var boards2=new HashMap<Long,Board>();
        boards2.put(1L,new Board(1,new HashMap<Long, List>(),"a"));
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
        var boards=new HashMap<Long,Board>();
        var actual = new User("a",boards ).toString();
        assertTrue(actual.contains(User.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("username"));
    }
}
