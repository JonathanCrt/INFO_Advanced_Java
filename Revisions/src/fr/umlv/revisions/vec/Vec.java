package fr.umlv.revisions.vec;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Notes :
 * Par défaut dans une interface tous est publique
 * 2 méthodes mono
 * Ne pas mixer les types
 * Q5 -> doit implémenter Iterable (forEach)
 * Faire jusqu'a la 6)
 * 6) ->  méthode par défaut
 * 7) Parcourir un stream d'élements
 * Est ce qu'on peut faire un Mono avec le stream que l'on donne ?
 * Vérifier ce que contient un Mono
 * itérateur
 */
public interface Vec<E> extends Iterable<E> {
    int size();

    Comparator<? super E> comparator();

    Optional<E> min();

    Optional<E> max();

    /**
     * Factory method to create a Mono with zero elements
     *
     * @param orderToCompare comparator
     * @param <T>            parameter type
     * @return new Mono instance
     */
    static <T> Mono<T> mono(Comparator<? super T> orderToCompare) {
        return new Mono<>(orderToCompare, Optional.empty());
    }

    /**
     * Factory method to create a Mono with one element
     *
     * @param orderToCompare comparator
     * @param element        given element
     * @param <M>            parameter type
     * @return new Mono instance
     */
    static <M> Mono<M> mono(Comparator<? super M> orderToCompare, M element) {
        Objects.requireNonNull(element);
        return new Mono<>(orderToCompare, Optional.of(element));
    }

    @SafeVarargs
    static <F> Fixed<F> fixed(Comparator<? super F> orderToCompare, F... args) {
        Objects.requireNonNull(args);
        return new Fixed<>(orderToCompare, args);
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static <W> Mono<W> toMono(Comparator<? super W> orderToCompare, Stream<W> streamOfElements) {
        if(streamOfElements.count() > 1){
            throw new IllegalArgumentException();
        }
        return new Mono<W>(orderToCompare, streamOfElements.findFirst());
    }

}
