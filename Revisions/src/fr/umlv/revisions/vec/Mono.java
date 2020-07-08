package fr.umlv.revisions.vec;

import java.util.*;
import java.util.stream.Stream;

public class Mono<E> implements Vec<E> {
    private final Comparator<? super E> comparator;
    private final Optional<E> element;

    Mono(Comparator<? super E> orderToCompare, Optional<E> element) {
        this.comparator = Objects.requireNonNull(orderToCompare);
        this.element = Objects.requireNonNull(element);
    }

    @Override
    public int size() {
        return element.isEmpty() ? 0 : 1;
    }

    @Override
    public Comparator<?  super E> comparator() {
        return this.comparator;
    }

    @Override
    public Optional<E> min() {
        return element;
    }

    @Override
    public Optional<E> max() {
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            boolean hasNext = element.isPresent();

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public E next() {
                if(!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return element.get();
            }
        };
    }
}
