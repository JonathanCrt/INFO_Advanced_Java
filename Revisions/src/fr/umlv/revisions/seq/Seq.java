package fr.umlv.revisions.seq;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq<E>  implements Iterable<E> {
    private List<Object> seqList;
    private Function<Object,  E> myFunction; // Object -> anything, function return E


    private Seq(List<Object> seq, Function <Object,  E> func) {
        this.seqList = seq;
        this.myFunction = func;
    }

    @SuppressWarnings("unchecked")
    private Seq(List<Object> seq) {
        this(seq, elt -> (E)elt);
    }

    @SuppressWarnings("unchecked")
    public static <S> Seq<S> from(List<? extends S> givenList) {
        Objects.requireNonNull(givenList);
        return new Seq<>(List.copyOf(givenList), param -> (S)param);
    }

    public E get(int index) {
        var element  = this.seqList.get(index);
        return this.myFunction.apply(element);
    }

    public int size() {
        return this.seqList.size();
    }

    /**
     * use StringJoiner
     * use forEach method of List and apply my function in all elements of seq
     * @return string asked
     */
    @Override
    public String toString() {
        var stringJoiner = new StringJoiner(", ", "<", ">");
        seqList.forEach(elt -> stringJoiner.add(myFunction.apply(elt).toString()));
        return stringJoiner.toString();
    }

    /**
     * Create a Seq with given values
     * @param args some arguments
     * @param <S> of S
     * @return new Seq
     */
    @SafeVarargs
    public static <S> Seq<S> of(S...args){ // we have garanty of only vargs of S
        Objects.requireNonNull(args);
        return new Seq<>(List.of(args));
    }



    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        for(var element: seqList) {
            consumer.accept(myFunction.apply(element));
        }
    }


    /**
     * Function<T,R>
     * prend les lements la structure de donnee et les transforme
     * Moment ou l'application de la fonction a ete faite -> get(...), toString(...), foreach(...)
     * Les méthodes donnent acces aux elements qui sont dans la sequence
     * Sequenc de E
     *
     */
    public <W> Seq<W> map(Function<? super E, ? extends W> mapper) {
        Objects.requireNonNull(mapper);
        return new Seq<>(seqList, this.myFunction.andThen(mapper));
    }


    public Optional<E> findFirst() {
        if(seqList.isEmpty()) {
            return Optional.empty();
        }
        E mappedElement  =  myFunction.apply(seqList.get(0));
        return Optional.of(mappedElement);
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return this.cursor != seqList.size();
            }

            @Override
            public E next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                var nextElement = myFunction.apply(seqList.get(cursor));
                this.cursor++;
                return nextElement;
            }
        };
    }

    /**
     * use Spliterators from java.util
     * NONNULL - IMMUTABLE - ORDERED
     * @return Stream of Seq elements
     */
    public Stream<E> stream() {
        return StreamSupport.stream(Spliterators.spliterator(seqList.toArray(),
                Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED), false);
    }

    public static void main(String[] args) {
       // var seq = Seq.from(List.of(78, 56, 34, 23));
        //System.out.println(seq.size());  // 4
        //System.out.println(seq.get(2));  // 34
    }

}
