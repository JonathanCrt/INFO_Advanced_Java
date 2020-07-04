package fr.umlv.revisions.cactusList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CactusList<E> implements Iterable<E> {
    private ArrayList<Object> list;
    private boolean isFrozen;
    private int size;

    public CactusList() {
        this.list = new ArrayList<>();
    }

    public void checkIfListIsFrozen() {
        if(this.isFrozen) {
            throw new IllegalStateException();
        }
    }


    public void add(E element) {
        Objects.requireNonNull(element);

        if(element instanceof CactusList) {
            throw new IllegalArgumentException();
        }
        this.checkIfListIsFrozen();
        list.add(element);
        this.size++;
    }

    public void addCactus(CactusList<? extends E> cactusList) {
        Objects.requireNonNull(cactusList);

        cactusList.isFrozen = true;
        this.checkIfListIsFrozen();
        this.list.add(cactusList);
        this.size += cactusList.size;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) this.list.iterator();
    }

    public boolean frozen() {
        return isFrozen;
    }

    public void forEach(Consumer<? super E> consumer) {
        //this.list.forEach(consumer);
        for(var element: list) {
            consumer.accept((((E)element)));
        }
    }

    @Override
    public String toString() {
        return this.list
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "<", ">"));
    }

    public static <S> CactusList<S> from(List<S> list) {
        var cactusList = new CactusList<S>();
        for(var elt: list) {
            cactusList.add(elt);
        }
        cactusList.isFrozen = true;
        return cactusList;
    }


    public static void main(String[] args) {
        var cactus = new CactusList<String>();
        cactus.add("foo");

        var cactus2 = new CactusList<String>();
        cactus2.add("bar");
        cactus2.add("baz");

        cactus.addCactus(cactus2);
        cactus.add("booz");

        //System.out.println(s.size());  // prints 4

        for(var s : cactus) {
            System.out.println(s);  // prints foo then bar then baz then booz
        }
    }





}
