package me.croshaw.ess.util;

import java.io.Serializable;

public class Pair<T, F> implements Serializable {
    private T first;
    private F second;

    public Pair(T first, F second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public F getSecond() {
        return second;
    }

    public void setSecond(F second) {
        this.second = second;
    }
}
