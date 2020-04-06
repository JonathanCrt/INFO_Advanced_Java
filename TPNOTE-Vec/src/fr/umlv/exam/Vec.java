package fr.umlv.exam;

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

    /**
     * on doit mettre un type paramétré (doc)
     * méthode d'instance
     */
    Comparator<? super E> comparator();

    Optional<E> min();
    Optional<E> max();



    /**
     * static : pas le même type (indépendant), pas lié au E
     * extends ou super ?
     * PECS --> Consomme =  super
     */
    static <T> Mono<T> mono(Comparator<? super T> comparator) {
        return new Mono<>(comparator, Optional.empty());
    }

    /**
     * La vérification que le comaparator est pas null est fait dans la factory
     * de l'interface
     * @param comparator
     * @param element
     * @param <V>
     * @return
     */
    static <V> Mono<V> mono(Comparator<? super V> comparator, V element) {
        Objects.requireNonNull(element);
        return new Mono<>(comparator, Optional.of(element));
    }


    /**
     * @SafeVarargs Toujours des objets du type E --> garantie du type
     * à chaque fois qu'on utilise un varargs de type paramétré
     * @param comparator
     * @param values Tableau d'objets
     * @param <W>
     * @return
     */
    @SafeVarargs
    static <W> Fixed<W> fixed(Comparator<? super W> comparator, W... values) {
        Objects.requireNonNull(values);
        return new Fixed<>(comparator, values);
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
