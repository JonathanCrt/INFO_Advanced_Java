package fr.umlv.revisions.vec;

import java.util.*;

public class Fixed<E> implements Vec<E> {

    private final Comparator<? super E> comparator;
    private final E[] elements;
    private boolean isSorted;

    /**
     *
     * @param orderToCompare comparator
     * @param elements elements
     * array = Defensive copy
     */
    Fixed(Comparator<? super E> orderToCompare, E[] elements) {
        Objects.requireNonNull(orderToCompare);
        Objects.requireNonNull(elements);
        for(E e: elements) {
            Objects.requireNonNull(e);
        }
        this.comparator = orderToCompare;
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
        if(!isSorted) {
            Arrays.sort(this.elements, this.comparator);
            isSorted = true;
        }
    }


    private Optional<E> getElementAtRank(int rank) {
        if(elements.length == 0){
            return Optional.empty();
        }
        this.sortIfNotSorted();
        return Optional.of(elements[rank]);
    }


    @Override
    public Optional<E> min() {
        return this.getElementAtRank(0);
    }

    @Override
    public Optional<E> max() {
        return this.getElementAtRank(elements.length - 1);
    }

    @Override
    public Iterator<E> iterator() {
        this.sortIfNotSorted();
        return Arrays.stream(elements).iterator();
    }
}
