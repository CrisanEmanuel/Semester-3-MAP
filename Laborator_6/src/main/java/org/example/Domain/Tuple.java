package org.example.Domain;

import java.util.Objects;

/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {

    private E1 e1;

    private E2 e2;

    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 getLeft() {
        return e1;
    }

    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    public E2 getRight() {
        return e2;
    }

    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return " " + e1 + ',' + e2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple<?, ?> tuple)) return false;
        return Objects.equals(e1, tuple.e1) &&
                Objects.equals(e2, tuple.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
