package server.services;

import java.util.Objects;

public class Pair <A, B>{
    private A a;
    private B b;

    /**
     * constructs a pair
     * @param a A
     * @param b B
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * getter for a
     * @return a
     */
    public A getA() {
        return a;
    }

    /**
     * getter for b
     * @return b
     */
    public B getB() {
        return b;
    }

    /**
     * equals method
     * @param o compares to o
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getA(), pair.getA());
    }

    /**
     * creates a hash code
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getA());
    }
}
