package fr.umlv.revisions.bag;

import java.util.*;
import java.util.function.Consumer;

public interface Bag<E>  extends Iterable<E> {

    /**
     * add count times one element and returns the number of times an item is present in the bag;
     * count must be strictly positive element and not null.
     * @param element given element
     * @param count count of times to add element
     * @return number of times an item is present
     */
    int add(E element, int count);

    /**
     * returns count the number of times that element is present in the bag
     *  Q2 -> this method take an Object in argument because we want most generic type
     * @param element given element
     * @return count of number of times or 0 if given element is not present
     */
    int count(Object element);

    static <T> Bag<T> createSimpleBag() {
        return new BagImpl<>();
    }

    static <T> Bag<T> createOrderedByInsertionBag() {
        return new BagImpl<T>(new LinkedHashMap<>());
    }

    static <T> Bag<T> createOrderedByElementBag(Comparator<? super T> comparator) {
        return new BagImpl<>(new TreeMap<>(comparator));
    }


    void forEach(Consumer<? super E> consumer);
    Iterator<E> iterator();

    AbstractCollection<E> asCollection();


}
