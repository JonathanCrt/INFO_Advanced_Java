package fr.umlv.range;

import org.junit.jupiter.params.provider.NullSource;

import java.util.*;
import java.util.function.BiConsumer;

//RangeMap should have two parameter now --> B and V
public class RangeMap<B extends Comparable<? super B>, V> {
    // end of range + value : pair
    // decalre que B is comparable
    private static class EndValue<I, W> { // int --> C
        final I end;
        final W value;

        EndValue(I end, W value) {
            this.end = end;
            this.value = value;
        }

        @Override
        public String toString() {
            return "EndValue{" +
                    "end=" + end +
                    ", value=" + value +
                    '}';
        }
    }

    // Other possibility : use Entry<Integer, V>
    private final TreeMap<B, EndValue<B, V>> treeMap = new TreeMap<>();


    // to make private
    RangeMap() { // delete private to be use by tests
        //default constructor

    }

    /**
     * factory method --> always static
     *
     * @return rangeMap Object
     */
    public static <T> RangeMap<Integer, T> createUsingInts() {
        return new RangeMap<>();
    }

    /**
     *
     * @param start beginning of range (a)
     * @param end end of range (b)
     * @param value value into range
     */
    public void add(B start, B end, V value) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(value);
        if (start.compareTo(end) >= 0) { // start >= end
            throw new IllegalArgumentException("start >= end");
        }
        //lowerEntry : entry with start lower than index
        //floorEntry : entry with start lower or equal than index
        var greatestRange = this.treeMap.lowerEntry(end); // range the greatest possible which begin after end (rightmost)
        if(greatestRange != null) {
            var endRange = greatestRange.getValue().end;
            if(endRange.compareTo(start) > 0) { // greater than a
                throw new IllegalArgumentException("Overlap");
            }
        }
        this.treeMap.put(start, new EndValue<>(end, value));
    }


    public Optional<V> get(B index) {
        Objects.requireNonNull(index);
        // Warning confusion : we can't see type with var
        Map.Entry<B, EndValue<B, V>> range = treeMap.floorEntry(index);
        if (range == null) {
            return Optional.empty();
        }
        //
        // the index must be greater than or equal to the beginning of the range;
        // BUT ALSO that the index is strictly smaller than the end in the code,
        // floorEntry() can check the first condition, and if (index > = end)
        // allows to see the second case (optional returning empty if the case)
        EndValue<B, V> endValue = range.getValue();
        if (index.compareTo(endValue.end) >= 0) { //index >= endValue.end
            return Optional.empty();
        }
        return Optional.of(endValue.value);

    }

    // [0, 5] = foo, [10, 20] = bar --> // virgule after foo
    public String toString() {
        var joiner = new StringJoiner(", ");  // if (", ", "[", "]") --> [[0, 5] = foo, [10, 20] = bar]
        this.treeMap.forEach((start, endValue) -> joiner.add("[" + start + ", " + endValue.end + "] = " + endValue.value));
        return joiner.toString();
    }

    /**
     * choose best functionnal interface : consumer, 2 parameter -> BiConsumer
     * testForEachOneElement --> need to do with Objects ! PECS -> Consume = super,
     * so ? super Integer
     *
     * @param consumer function with two parameters
     */
    public void forEach(BiConsumer<? super B, ? super B> consumer) {
        Objects.requireNonNull(consumer);
        // no need stream because Map have forEach
        this.treeMap.forEach((start, endValue) -> consumer.accept(start, endValue.end));
        /*
        this.treeMap.entrySet()
                .stream()
                .forEach((mapper) -> consumer.accept(mapper.getKey(), mapper.getValue().end));

         */
    }

    public static void main(String[] args) {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 5, "foo");
        rangeMap.add(10, 20, "bar");
        System.out.println(rangeMap.get(2));
        rangeMap.forEach((start, end) -> System.out.println("start: " + start + " end: " + end));
    }


}
