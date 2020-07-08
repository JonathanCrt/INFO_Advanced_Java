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
        if (this.isFrozen) {
            throw new IllegalStateException();
        }
    }


    public void add(E element) {
        Objects.requireNonNull(element);

        if (element instanceof CactusList) {
            throw new IllegalArgumentException();
        }
        this.checkIfListIsFrozen();
        list.add(element);
        this.size++;
    }

    /**
     * A vérifier : ne contient pas l'élement lui même
     *
     * @param cactusList
     */
    public void addCactus(CactusList<? extends E> cactusList) {
        Objects.requireNonNull(cactusList);

        // Ne pas
        if(cactusList == this){
            throw new IllegalStateException();
        }

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

    /**
     * Le cast ne marche pas , que si on sait que l'element que on manipule est exact
     * Ne marche pas sur els element contenant des List<?>
     * Traiter séparement les E et CactusList ("Structure à tiroir")
     * => instance of
     *
     * @param consumer
     */
    @SuppressWarnings("unchecked")
    public void forEach(Consumer<? super E> consumer) {
        //this.list.forEach(consumer);
        Objects.requireNonNull(consumer);
        for (var element : list) {
            if (element instanceof CactusList) { // forcément un CactusList<E>
                ((CactusList<E>) element).forEach(consumer);
            } else {
                consumer.accept((E) element);
            }

        }
    }

    /**

     * @return
     */
    @Override
    public String toString() {
        // foreach et StringJoiner
        return this.list
                .stream() // stream de E et de CactusList<E>
                // flatmap --> Comment transformer une Cactus list en stream
                .map(Object::toString)
                .collect(Collectors.joining(", ", "<", ">"));
    }

    public static <S> CactusList<S> from(List<S> list) {
        var cactusList = new CactusList<S>();
        for (var elt : list) {
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

        for (var s : cactus) {
            System.out.println(s);  // prints foo then bar then baz then booz
        }
    }


}
