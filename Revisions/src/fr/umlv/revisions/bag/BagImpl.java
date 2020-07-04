package fr.umlv.revisions.bag;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * bad practice to use public method into intern instance method (encapsulation !)
 * Pour pouvoir utiliser la boucle for-each-in il est nécessaire d’itérer sur la structure de
 * donnée. Par conséquent nous devons implémenter l’interface Iterable<E>. Car nous venons
 * d’implémenter Iterable.
 *
 * @param <T> T  element
 */
public class BagImpl<T> implements Bag<T> {
    private Map<T, Integer> map;
    private int size;

    public BagImpl() {
        this.map = new HashMap<>();
    }

    public BagImpl(HashMap<T, Integer> map, int size) {
        this.map = new HashMap<>();
        this.size = 0;
    }

    public BagImpl(Map<T, Integer> map) {
        this.map = map;
        this.size = 0;
    }


    @Override
    public int add(T element, int count) {
        Objects.requireNonNull(element);
        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        this.size += count;
        map.merge(element, count, Integer::sum);
        return count(element);
    }

    @Override
    public int count(Object element) {
        Objects.requireNonNull(element);
        if (map.get(element) == null) {
            return 0;
        }
        return map.get(element);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        /*
        map.forEach((key, value) -> {
            for(var i = 0; i < value; i++) {
                consumer.accept(key); // execute la méthode d'instance de consumer (interface fonctionnelle)
            }
        });
         */
        for (var entry : this.map.entrySet()) { //entrySet : liste des clés /valeurs
            var key = entry.getKey();
            for (int i = 0; i < entry.getValue(); i++) {
                consumer.accept(key);
            }
        }

    }

    @Override
    public Iterator<T> iterator() {

        var mapIterator = this.map.entrySet().iterator();
        return new Iterator<>() {

            private T current = null; // dernier element deja renvoye
            private int remaining = 0; // Nombre d'element restant Si remaining = 0 je suis a la fin

            @Override
            public boolean hasNext() {
                return (remaining != 0 && mapIterator.hasNext());
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (remaining != 0) {
                    remaining--;
                    return current;
                }
                var next = mapIterator.next();
                current = next.getKey();
                remaining = next.getValue() - 1;
                return current;
            }
        };
    }


    // La Collection renvoyée par la méthode .nCopies est non-mutable
    /*
    @Override
    public Iterator<T> iterator() {
        return map.entrySet()
                .stream()
                .flatMap(mapper -> Collections.nCopies(mapper.getValue(), mapper.getKey()).stream())
                .iterator();
    }

     */

    public static <T> void printAndTime(Supplier<T> supplier) {
        var start = System.currentTimeMillis();
        var result = supplier.get();
        var end = System.currentTimeMillis();
        System.out.println(" elapsed time" + (end - start) + " millisecondes");
    }

    public AbstractCollection<T> asCollection() {
        var bag = this;
        return new AbstractCollection<>() {
            @Override
            public Iterator<T> iterator() {
                return bag.iterator();
            }

            @Override
            public int size() {
                return bag.size;
            }

            @Override
            public boolean contains(Object object) {
                return bag.count(object) != 0;
            }
        };
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BagImpl<?>)) return false;
        BagImpl<?> bag1 = (BagImpl<?>) o;
        return size == bag1.size &&
                Objects.equals(map, bag1.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, size);
    }

    public static void main(String[] args) {
        var bag = new BagImpl<Long>();
        printAndTime(bag::iterator);
    }

}
