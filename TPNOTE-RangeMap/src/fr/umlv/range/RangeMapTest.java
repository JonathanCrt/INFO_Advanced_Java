package fr.umlv.range;

import static java.util.Spliterator.SORTED;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public class RangeMapTest {
    @Test @Tag("Q1")
    public void testCreateFromInts() {
        RangeMap.createUsingInts();
    }
    @Test @Tag("Q1")
    public void testCreateFromIntsExplicit() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "hello");
    }
    @Test @Tag("Q1")
    public void testAddNoOverlap() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        rangeMap.add(-10, 0, 555);
        rangeMap.add(0, 10, 666);
        rangeMap.add(10, 20, 777);
    }
    @Test @Tag("Q1")
    public void testAddProblems() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> RangeMap.<String>createUsingInts().add(10, 0, "oops")),
                () -> assertThrows(IllegalArgumentException.class, () -> RangeMap.<String>createUsingInts().add(13, 13, "oops")),
                () -> assertThrows(NullPointerException.class, () -> RangeMap.createUsingInts().add(20, 20, null))
        );
    }


    @Test @Tag("Q2")
    public void testGetSimple() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 5, "foo");
        rangeMap.add(10, 20, "bar");
        assertEquals("foo", rangeMap.get(2).orElseThrow());
    }
    @Test @Tag("Q2")
    public void testGetWithOneRange() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 3, "foo");
        assertAll(
                () -> assertEquals("foo", rangeMap.get(0).orElseThrow()),
                () -> assertEquals("foo", rangeMap.get(1).orElseThrow()),
                () -> assertEquals("foo", rangeMap.get(2).orElseThrow()),
                () -> assertFalse(rangeMap.get(-1).isPresent()),
                () -> assertFalse(rangeMap.get(3).isPresent())
        );
    }
    @Test @Tag("Q2")
    public void testGetWithSeveralRanges() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 1, "foo");
        rangeMap.add(2, 3, "bar");
        rangeMap.add(4, 5, "baz");
        rangeMap.add(10, 15, "baz");
        assertAll(
                () -> assertEquals("foo", rangeMap.get(0).orElseThrow()),
                () -> assertEquals("bar", rangeMap.get(2).orElseThrow()),
                () -> assertEquals("baz", rangeMap.get(4).orElseThrow()),
                () -> assertEquals("baz", rangeMap.get(14).orElseThrow()),
                () -> assertFalse(rangeMap.get(-1).isPresent()),
                () -> assertFalse(rangeMap.get(1).isPresent()),
                () -> assertFalse(rangeMap.get(3).isPresent()),
                () -> assertFalse(rangeMap.get(5).isPresent()),
                () -> assertFalse(rangeMap.get(7).isPresent()),
                () -> assertFalse(rangeMap.get(17).isPresent())
        );
    }
    @Test @Tag("Q2")
    public void testGetWithNoRange() {
        var rangeMap = RangeMap.createUsingInts();
        new Random(0).ints(100_000).forEach(index -> assertFalse(rangeMap.get(index).isPresent()));
    }
    @Test @Tag("Q2")
    public void testGetWithALotOfRanges() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        new Random(0).ints(100_000).distinct().forEach(index -> rangeMap.add(index, index + 1, index));
        new Random(0).ints(100_000).forEach(index -> assertEquals((int)rangeMap.get(index).orElseThrow(), index));
    }


    @Test @Tag("Q3")
    public void testOverlapLeft() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "foo");
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(0, 10, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(0, 5, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(-2, 5, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(-2, 10, "bar"))
        );
    }
    @Test @Tag("Q3")
    public void testOverlapRight() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "foo");
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(0, 10, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(0, 15, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(5, 15, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(5, 10, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(9, 15, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(9, 10, "bar"))
        );
    }
    @Test @Tag("Q3")
    public void testOverlapMiddle() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "foo");
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(-5, 15, "bar")),
                () -> assertThrows(IllegalArgumentException.class, () -> rangeMap.add(2, 8, "bar"))
        );
    }



    @Test @Tag("Q4")
    public void testToStringSimple() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 5, "foo");
        rangeMap.add(10, 20, "bar");
        assertEquals("[0, 5] = foo, [10, 20] = bar", rangeMap.toString());
    }
    @Test @Tag("Q4")
    public void testToStringEmpty() {
        var rangeMap = RangeMap.<String>createUsingInts();
        assertEquals("", rangeMap.toString());
    }
    @Test @Tag("Q4")
    public void testToStringOneRange() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "foo");
        assertEquals("[0, 10] = foo", rangeMap.toString());
    }
    @Test @Tag("Q4")
    public void testToStringTwoRanges() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(0, 10, "foo");
        rangeMap.add(-7, -5, "bar");
        assertEquals("[-7, -5] = bar, [0, 10] = foo", rangeMap.toString());
    }
    @Test @Tag("Q4")
    public void testToStringThreeRanges() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(11, 15, "baz");
        rangeMap.add(0, 10, "foo");
        rangeMap.add(-7, -5, "bar");
        assertEquals("[-7, -5] = bar, [0, 10] = foo, [11, 15] = baz", rangeMap.toString());
    }



    @Test @Tag("Q5")
    public void testForEach() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(10, 15, "baz");
        rangeMap.add(0, 5, "foo");
        rangeMap.add(15, 20, "bar");

        var list = new ArrayList<Integer>();
        rangeMap.forEach((start, end) -> {
            assertEquals(start + 5, (int)end);
            list.add(start);
        });
        assertEquals(List.of(0, 10, 15), list);
    }
    @Test @Tag("Q5")
    public void testForEachEmpty() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.forEach((start, end) -> fail("oops"));
    }
    @Test @Tag("Q5")
    public void testForEachOneElement() {
        var rangeMap = RangeMap.createUsingInts();
        rangeMap.add(0, 10, "wizz");
        rangeMap.forEach((Object start, Object end) -> {
            assertEquals(0, (int)start);
            assertEquals(10, (int)end);
        });
    }
    @Test @Tag("Q5")
    public void testForEachNull() {
        assertThrows(NullPointerException.class, () -> RangeMap.createUsingInts().forEach(null));
    }
    @Test @Tag("Q5")
    public void testForEachALot() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 100_000).forEach(index -> rangeMap.add(index, index + 1, index));

        var counter = new Object() {
            int value;
        };
        rangeMap.forEach((start, end) -> {
            assertEquals(counter.value++, (int)start);
            assertEquals(start + 1, (int)end);
        });
    }


    @Test @Tag("Q6")
    public void testRangeMapStrings() {
        var rangeMap = new RangeMap<String, String>();
        rangeMap.add("b", "j", "foo");
        rangeMap.add("k", "w", "bar");
        assertAll(
                () -> assertEquals("foo", rangeMap.get("c").orElseThrow()),
                () -> assertEquals("bar", rangeMap.get("o").orElseThrow()),
                () -> assertFalse(rangeMap.get("a").isPresent()),
                () -> assertFalse(rangeMap.get("j").isPresent()),
                () -> assertFalse(rangeMap.get("z").isPresent())
        );
    }
    @Test @Tag("Q6")
    public void testRangeMapIntegers() {
        var rangeMap = new RangeMap<Integer, String>();
        rangeMap.add(0, 10, "foo");
        rangeMap.add(15, 20, "bar");
        assertAll(
                () -> assertEquals("foo", rangeMap.get(5).orElseThrow()),
                () -> assertEquals("bar", rangeMap.get(17).orElseThrow()),
                () -> assertFalse(rangeMap.get(-5).isPresent()),
                () -> assertFalse(rangeMap.get(12).isPresent()),
                () -> assertFalse(rangeMap.get(22).isPresent())
        );
    }
    @Test @Tag("Q6")
    public void testRangeMapLocalDates() {
        var rangeMap = new RangeMap<LocalDate, String>();
        rangeMap.add(LocalDate.MIN, LocalDate.MAX, "occupied");
        assertEquals("occupied", rangeMap.get(LocalDate.now()).orElseThrow());
    }
    @Test @Tag("Q6")
    public void testRangeMapWithNulls() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new RangeMap<Integer, String>().add(null, 1, "foo")),
                () -> assertThrows(NullPointerException.class, () -> new RangeMap<Integer, String>().add(1, null, "foo")),
                () -> assertThrows(NullPointerException.class, () -> new RangeMap<>().get(null))
        );
    }

    /*
    @Test @Tag("Q7")
    public void testIteratorSimple() {
        var rangeMap = RangeMap.createUsingInts();
        rangeMap.add(15, 20, "foo");
        rangeMap.add(0, 10, "bar");
        var it = rangeMap.iterator();
        assertTrue(it.hasNext());
        assertEquals(Map.entry(0, 10), it.next());
        assertTrue(it.hasNext());
        assertEquals(Map.entry(15, 20), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
    @Test @Tag("Q7")
    public void testIteratorEmpty() {
        var rangeMap = RangeMap.createUsingInts();
        var it = rangeMap.iterator();
        assertAll(
                () -> assertFalse(it.hasNext()),
                () -> assertThrows(NoSuchElementException.class, it::next)
        );
    }
    @Test @Tag("Q7")
    public void testIteratorHasNextHasNoSideEffect() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 100).forEach(index -> rangeMap.add(index, index + 1, index));
        var it = rangeMap.iterator();
        for(var count = 0; it.hasNext(); count++) {
            for(var i = 0; i < 5; i++) {
                it.hasNext();
            }
            assertEquals(Map.entry(count, count + 1), it.next());
        }
    }
    @Test @Tag("Q7")
    public void testIteratorALot() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 100_000).forEach(index -> rangeMap.add(index, index + 1, index));
        var it = rangeMap.iterator();
        for(var count = 0; it.hasNext(); count++) {
            assertEquals(Map.entry(count, count + 1), it.next());
        }
    }
    @Test @Tag("Q7")
    public void testIteratorOrder() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        new Random(0).ints(100_000).distinct().forEach(index -> rangeMap.add(index, index + 1, index));
        var list = new ArrayList<Integer>();
        rangeMap.iterator().forEachRemaining(entry -> list.add(entry.getKey()));
        assertEquals(new Random(0).ints(100_000).distinct().sorted().boxed().collect(toList()), list);
    }
    @Test @Tag("Q7")
    public void testTwoIterators() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 10).forEach(index -> rangeMap.add(index, index + 1, index));
        var it1 = rangeMap.iterator();
        var it2 = rangeMap.iterator();
        for(var count = 0; it1.hasNext() || it2.hasNext(); count++) {
            assertEquals(Map.entry(count, count + 1), it1.next());
            assertEquals(Map.entry(count, count + 1), it2.next());
        }
    }
    @Test @Tag("Q7")
    public void testIteratorNoRemove() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        assertThrows(UnsupportedOperationException.class, () -> rangeMap.iterator().remove());
    }


    @Test @Tag("Q8")
    public void testStreamSimple() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(5, 10, "foo");
        rangeMap.add(0, 5, "bar");
        rangeMap.add(10, 15, "baz");
        assertEquals(Map.of(0, 5, 5, 10, 10, 15), rangeMap.stream().collect(toMap(Entry::getKey, Entry::getValue)));
    }
    @Test @Tag("Q8")
    public void testStreamEmpty() {
        var rangeMap = RangeMap.createUsingInts();
        assertEquals(0, rangeMap.stream().count());
    }
    @Test @Tag("Q8")
    public void testStreamOrder() {
        var rangeMap = RangeMap.<String>createUsingInts();
        rangeMap.add(5, 10, "foo");
        rangeMap.add(0, 5, "bar");
        rangeMap.add(10, 15, "baz");
        assertEquals(List.of(Map.entry(0, 5), Map.entry(5, 10), Map.entry(10, 15)), rangeMap.stream().collect(toList()));
    }
    @Test @Tag("Q8")
    public void testStreamSortedCharacteristics() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        assertTrue(rangeMap.stream().spliterator().hasCharacteristics(SORTED));
    }
    @Test @Tag("Q8")
    public void testStreamCanBeSplit() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 100_000).forEach(i -> rangeMap.add(i, i + 1, i));
        assertNotNull(rangeMap.stream().spliterator().trySplit());
    }
    @Test @Tag("Q8")
    public void testStreamSplitSize() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        IntStream.range(0, 100_000).forEach(i -> rangeMap.add(i, i + 1, i));
        var spliterator = rangeMap.stream().spliterator();
        assertEquals(100_000, spliterator.estimateSize());
        var spliterator2 = spliterator.trySplit();
        assertNotNull(spliterator2);
        assertTrue(spliterator.estimateSize() < 100_000);
        assertTrue(spliterator2.estimateSize() < 100_000);
    }
    @Test @Tag("Q8")
    public void testStreamSorted() {
        var rangeMap = RangeMap.<Integer>createUsingInts();
        var list = new ArrayList<Integer>();
        new Random(0).ints(1_000_000).distinct().forEach(value -> {
            rangeMap.add(value, value + 1, value);
            list.add(value);
        });
        list.sort(null);
        assertEquals(list, rangeMap.stream().map(Entry::getKey).collect(toList()));
    }

     */
}