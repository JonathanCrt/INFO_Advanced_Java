package fr.umlv.exam;

import java.util.*;

/**
 * S'assurer que les entêtes des méthodes sont ok avant body
 * La structure n'est pas itérable naturellement (tableau)
 * @param <E>
 */
public class Mono<E> implements Vec<E> {
    private final Comparator<?  super E> comparator; // toujours mettre nom complet
    private  final Optional<E> element;

    /**
     * Les vérifications sont faites dans les factory
     * Visibilité package -> ne rien mettre
     * @param comparator
     * @param element
     */
    Mono(Comparator<? super E> comparator, Optional<E> element) {
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
        this.element = element;
    }

    @Override
    public int size() {
        return element.isEmpty()? 0 : 1;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public Optional<E> min() {
        return element;
    }

    @Override
    public Optional<E> max() {
        return element;
    }

    /**
     *
     * @return
     */
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
                if(!hasNext){
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return element.get();
            }
        };
    }
}
