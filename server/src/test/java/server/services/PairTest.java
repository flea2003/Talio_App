package server.services;


import commons.Board;
import commons.List;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    @Test
    public void constructorTest(){
        Object a = new Object();
        Object b = new Object();
        Pair pair = new Pair(a, b);
        assertNotNull(pair);
    }

    @Test
    public void gettersTest(){
        Object a = new Object();
        Object b = new Object();
        Pair pair = new Pair(a, b);
        assertEquals(a, pair.getA());
        assertEquals(b, pair.getB());
    }

    @Test
    public void equalsTest(){
        Object a = new List("a");
        Object b = new List("a");
        Pair pair1 = new Pair(a, b);
        Pair pair2 = new Pair(a, b);
        Pair pair3 = new Pair(a, new Board("b"));

        assertEquals(pair1, pair2);
        assertFalse(pair1.equals(pair3));
        assertNotEquals(pair1, pair3);
        assertNotEquals(pair2, pair3);

        assertFalse(pair1.equals(new ArrayList<>()));
        assertTrue(pair1.equals(pair1));
    }

    @Test
    public void hashCodeTest(){
        Object a = new List("a");
        Object b = new List("a");
        Pair pair1 = new Pair(a, b);
        Pair pair2 = new Pair(a, b);
        Pair pair3 = new Pair(a, new Board("b"));

        assertEquals(pair1.hashCode(), pair2.hashCode());
        assertNotEquals(pair1.hashCode(), pair3.hashCode());
    }
}
