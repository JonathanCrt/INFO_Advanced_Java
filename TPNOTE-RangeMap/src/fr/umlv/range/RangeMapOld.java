package fr.umlv.range;

import java.util.*;
import java.util.function.BiConsumer;

// without Comparable Q6
public class RangeMapOld<V> {
    // end of range + value : pair
    private static class EndValue<W> {
        final int end;
        final W value;

        EndValue(int end, W value) {
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
    private final TreeMap<Integer, EndValue<V>> treeMap = new TreeMap<>();


    // to make private
    private RangeMapOld() {
        //default constructor
    }

    /**
     * factory method --> always static
     *
     * @return rangeMap Object
     */
    public static <T> RangeMapOld<T> createUsingInts() {
        return new RangeMapOld<>();
    }

    public void add(int start, int end, V value) {
        Objects.requireNonNull(value);
        if (start >= end) {
            throw new IllegalArgumentException("start >= end");
        }
        treeMap.put(start, new EndValue<>(end, value));
    }


    public Optional<V> get(int index) {
        // Warning confusion : we can't see type with var
        Map.Entry<Integer, EndValue<V>> range = treeMap.floorEntry(index);
        if(range == null) {
            return Optional.empty();
        }
        //
        // the index must be greater than or equal to the beginning of the range;
        // BUT ALSO that the index is strictly smaller than the end in the code,
        // floorEntry() can check the first condition, and if (index > = end)
        // allows to see the second case (optional returning empty if the case)
        EndValue<V> endValue = range.getValue();
        if(index >= endValue.end) {
            return Optional.empty();
        }
        return Optional.of(endValue.value);

    }

    // [0, 5] = foo, [10, 20] = bar --> // virgule after foo
    public String toString() {
        var joiner  = new StringJoiner(", ");  // if (", ", "[", "]") --> [[0, 5] = foo, [10, 20] = bar]
        this.treeMap.forEach((start, endValue) -> joiner.add("[" + start + ", " + endValue.end + "] = " + endValue.value));
        return joiner.toString();
    }

    /**
     * choose best functionnal interface : consumer, 2 parameter -> BiConsumer
     * testForEachOneElement --> need to do with Objects ! PECS -> Consume = super,
     * so ? super Integer
     * @param consumer function with two parameters
     */
    public void forEach(BiConsumer<? super Integer, ? super Integer> consumer) {
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
        var rangeMap = RangeMapOld.<String>createUsingInts();
        rangeMap.add(0, 5, "foo");
        rangeMap.add(10, 20, "bar");
        System.out.println(rangeMap.get(2));
        rangeMap.forEach((start, end) -> System.out.println("start: " + start + " end: " + end));
    }


}
