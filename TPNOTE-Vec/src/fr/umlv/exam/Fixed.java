package fr.umlv.exam;

import java.util.*;

public class Fixed<E> implements Vec<E> {

    private final Comparator<? super E> comparator; // toujours mettre nom complet
    private final E[] elements;
    private boolean isSorted;

    /**
     * @param comparator
     * @param elements
     * Copie défensive : copie du tablrau venant de l'extérieur
     */
    Fixed(Comparator<? super E> comparator, E[] elements) {
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(elements);
        for (E e : elements) {
            Objects.requireNonNull(e);
        }
        this.comparator = comparator;
        this.elements = Arrays.copyOf(elements, elements.length);
    }


    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    private void sortIfNotSorted() {
        if(! isSorted){
            Arrays.sort(elements, comparator);
            isSorted = true;
        }
    }

    private Optional<E> getAtRank(int rank) {
        if (elements.length == 0) {
            return Optional.empty();
        }
        if (!isSorted) {
            Arrays.sort(elements, comparator);
            isSorted = true;
        }
        return Optional.of(elements[rank]);
    }

    @Override
    public Optional<E> min() {
        return this.getAtRank(0);
    }

    @Override
    public Optional<E> max() {
        return this.getAtRank(elements.length - 1);
    }

    @Override
    public Iterator<E> iterator() {
        this.sortIfNotSorted();
        return Arrays.stream(elements).iterator(); // iterator du stream
    }
}
