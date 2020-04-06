package fr.umlv.exam;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/*
@SuppressWarnings("static-method")
public class VecTest {
    @Test
    @Tag("Q1")
    public void testMonoEmpty() {
        Mono<String> mono = Vec.mono(String::compareTo);
        Vec<String> vec = Vec.mono(String::compareTo);
    }

    @Test
    @Tag("Q1")
    public void testMonoOne() {
        Mono<String> mono = Vec.mono(String::compareTo, "hello mono");
        Vec<String> vec = Vec.mono(String::compareTo, "hello vec");
    }

    @Test
    @Tag("Q1")
    public void testMonoOneSize() {
        var mono = Vec.mono(String::compareTo, "hello mono");
        assertEquals(1, mono.size());
    }

    @Test
    @Tag("Q1")
    public void testMonoEmptySize() {
        var mono = Vec.mono(String::compareTo);
        assertEquals(0, mono.size());
    }

    @Test
    @Tag("Q1")
    public void testMonoComparator() {
        class Cmp implements Comparator<Integer> {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        }
        var cmp = new Cmp();
        var mono0 = Vec.mono(cmp);
        var mono1 = Vec.mono(cmp, 42);
        assertSame(cmp, mono0.comparator());
        assertSame(cmp, mono1.comparator());
    }

    @Test
    @Tag("Q1")
    public void testMonoEmptyComparatorSignature() {
        class Cmp implements Comparator<CharSequence> {
            @Override
            public int compare(CharSequence s1, CharSequence s2) {
                return s1.toString().compareTo(s2.toString());
            }
        }
        Cmp cmp = new Cmp();
        Mono<String> mono = Vec.mono(cmp);
    }

    @Test
    @Tag("Q1")
    public void testMonoOneComparatorSignature() {
        class Cmp implements Comparator<CharSequence> {
            @Override
            public int compare(CharSequence s1, CharSequence s2) {
                return s1.toString().compareTo(s2.toString());
            }
        }
        Cmp cmp = new Cmp();
        Mono<String> mono = Vec.mono(cmp, "foobar");
    }

    @Test
    @Tag("Q1")
    public void testMonoNoPublicConstructor() {
        assertEquals(0, Mono.class.getConstructors().length);
    }

    @Test
    @Tag("Q1")
    public void testMono() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> Vec.mono(null, "foo")),
                () -> assertThrows(NullPointerException.class, () -> Vec.mono(null)),
                () -> assertThrows(NullPointerException.class, () -> Vec.mono(String::compareTo, null)));
    }

    @Test
    @Tag("Q2")
    public void testFixedSimple() {
        Fixed<String> fixed = Vec.fixed(String::compareTo, "foo", "bar", "baz");
        Vec<String> vec = Vec.fixed(String::compareTo, "foo", "bar", "baz");
    }

    @Test
    @Tag("Q2")
    public void testFixedSize() {
        assertAll(() -> assertEquals(3, Vec.fixed(String::compareTo, "foo", "bar", "baz").size()),
                () -> assertEquals(2, Vec.fixed(Integer::compareTo, 2, 3).size()),
                () -> assertEquals(1, Vec.fixed(String::compareTo, "foo").size()),
                () -> assertEquals(0, Vec.fixed(Integer::compareTo).size()));
    }

    @Test
    @Tag("Q2")
    public void testFixedSizeSameElement() {
        var fixed = Vec.fixed(Integer::compareTo, 1, 1, 1, 1);
        assertEquals(4, fixed.size());
    }

    @Test
    @Tag("Q2")
    public void testFixedComparator() {
        class Cmp implements Comparator<Integer> {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        }
        var cmp = new Cmp();
        var fixed = Vec.fixed(cmp, 1, 10, 100, 1000);
        assertSame(cmp, fixed.comparator());
    }

    @Test
    @Tag("Q2")
    public void testFixedComparatorSignature() {
        class Cmp implements Comparator<CharSequence> {
            @Override
            public int compare(CharSequence s1, CharSequence s2) {
                return s1.toString().compareTo(s2.toString());
            }
        }
        Cmp cmp = new Cmp();
        Fixed<String> fixed = Vec.fixed(cmp, "foo", "bar");
    }

    @Test
    @Tag("Q2")
    public void testFixedNoPublicConstructor() {
        assertEquals(0, Fixed.class.getConstructors().length);
    }

    @Test
    @Tag("Q2")
    public void testFixed() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> Vec.fixed(null, "foo")),
                () -> assertThrows(NullPointerException.class, () -> Vec.fixed(String::compareTo, (String[]) null)),
                () -> assertThrows(NullPointerException.class, () -> Vec.fixed(String::compareTo, "foo", null, "bar")));
    }

    @Test
    @Tag("Q3")
    public void testMonoOneMin() {
        var mono = Vec.mono(Integer::compareTo, 42);
        assertEquals(42, mono.min().orElseThrow());
        mono.min().ifPresent((Integer value) -> assertEquals(42, value));
    }

    @Test
    @Tag("Q3")
    public void testMonoEmptyMinMax() {
        var mono = Vec.mono(Integer::compareTo);
        assertTrue(mono.min().isEmpty());
        assertTrue(mono.max().isEmpty());
    }

    @Test
    @Tag("Q3")
    public void testMonoOneMax() {
        var mono = Vec.mono(String::compareToIgnoreCase, "hello");
        assertEquals("hello", mono.max().orElseThrow());
        mono.max().ifPresent((String value) -> assertEquals("hello", value));
    }

    @Test
    @Tag("Q4")
    public void testFixedMin() {
        var fixed = Vec.fixed(Integer::compareTo, 1, 23, 12);
        assertEquals(1, fixed.min().orElseThrow());
        fixed.min().ifPresent((Integer value) -> assertEquals(1, value));
    }

    @Test
    @Tag("Q4")
    public void testFixedMax() {
        var fixed = Vec.fixed(String::compareToIgnoreCase, "cc", "a", "bbb");
        assertEquals("cc", fixed.max().orElseThrow());
        fixed.max().ifPresent((String value) -> assertEquals("cc", value));
    }

    @Test
    @Tag("Q4")
    public void testFixedEmptyMinMax() {
        var fixed = Vec.fixed(Integer::compareTo);
        assertTrue(fixed.min().isEmpty());
        assertTrue(fixed.max().isEmpty());
    }

    @Test
    @Tag("Q4")
    public void testFixedMinChangeAfterCall() {
        var array = List.of(1, 7, 4, 9, 3).toArray(Integer[]::new);
        var fixed = Vec.fixed(Integer::compareTo, array);
        array[0] = 0;
        assertEquals(1, fixed.min().orElseThrow());
    }

    @Test
    @Tag("Q4")
    public void testFixedSizeSideEffectFree() {
        var fixed = Vec.fixed((s1, s2) -> fail(), "zorg", "alien");
        assertEquals(2, fixed.size());
    }

    @Test
    @Tag("Q4")
    public void testFixedComparatorNotCalledTooManyTimes() {
        var cmp = new Comparator<String>() {
            boolean canBeCalled = true;

            @Override
            public int compare(String s1, String s2) {
                if (!canBeCalled) {
                    throw (RuntimeException) fail();
                }
                return s1.compareTo(s2);
            }
        };
        var fixed = Vec.fixed(cmp, "jay", "alice", "bob");
        assertEquals("alice", fixed.min().orElseThrow());
        cmp.canBeCalled = false;
        assertEquals("jay", fixed.max().orElseThrow());
    }

    @Test
    @Tag("Q5")
    public void testMonoOneEnhancedLoop() {
        var mono = Vec.mono(String::compareTo, "foo");
        for (var value : mono) {
            assertEquals("foo", value);
        }
    }

    @Test
    @Tag("Q5")
    public void testMonoEmptyEnhancedLoop() {
        var mono = Vec.mono(String::compareTo);
        for (var value : mono) {
            fail();
        }
    }

    @Test
    @Tag("Q5")
    public void testFixedEnhancedLoop() {
        var fixed = Vec.fixed(String::compareTo, "foo", "bar", "whizz", "baz");
        var list = new ArrayList<String>();
        for (var value : fixed) {
            list.add(value);
        }
        assertEquals(List.of("bar", "baz", "foo", "whizz"), list);
    }

    @Test
    @Tag("Q5")
    public void testVecEnhancedLoop() {
        Vec<String> vec = Vec.fixed(String::compareTo, "foo", "bar", "whizz", "baz");
        ArrayList<String> list = new ArrayList<>();
        for (String value : vec) {
            list.add(value);
        }
        assertEquals(List.of("bar", "baz", "foo", "whizz"), list);
    }

    @Test
    @Tag("Q5")
    public void testMonoOneForEach() {
        var mono = Vec.mono(Integer::compareTo, 158);
        mono.forEach(value -> {
            assertEquals(158, value);
        });
    }

    @Test
    @Tag("Q5")
    public void testMonoEmptyForEach() {
        var mono = Vec.mono(Integer::compareTo);
        mono.forEach(value -> fail());
    }

    @Test
    @Tag("Q5")
    public void testFixedForEach() {
        var fixed = Vec.fixed(Integer::compareTo, 5, 2, 8, 3);
        var list = new ArrayList<Integer>();
        fixed.forEach(list::add);
        assertEquals(List.of(2, 3, 5, 8), list);
    }

    @Test
    @Tag("Q5")
    public void testVecForEach() {
        Vec<Integer> vec = Vec.fixed(Integer::compareTo, 5, 2, 8, 3);
        ArrayList<Integer> list = new ArrayList<>();
        vec.forEach((Integer value) -> list.add(value));
        assertEquals(List.of(2, 3, 5, 8), list);
    }

    @Test
    @Tag("Q5")
    public void testForEachNull() {
        Mono<Integer> mono = Vec.mono(Integer::compareTo, 158);
        Fixed<String> fixed = Vec.fixed(String::compareTo, "foo", "bar", "whizz", "baz");
        Vec<Integer> vec = Vec.fixed(Integer::compareTo, 5, 2, 8, 3);
        assertAll(() -> assertThrows(NullPointerException.class, () -> mono.forEach(null)),
                () -> assertThrows(NullPointerException.class, () -> fixed.forEach(null)),
                () -> assertThrows(NullPointerException.class, () -> vec.forEach(null)));
    }

    @Test
    @Tag("Q5")
    public void testForEachSameElement() {
        var fixed = Vec.fixed(Integer::compareTo, 1, 1, 1, 1);
        var list = new ArrayList<Integer>();
        for(var value: fixed) {
            list.add(value);
        }
        assertEquals(List.of(1, 1, 1, 1), list);
    }

    @Test
    @Tag("Q5")
    public void testForEachALot() {
        assertTimeoutPreemptively(Duration.ofMillis(1_000), () -> {
            var elements = range(0, 1_000_000).boxed().toArray(Integer[]::new);
            var fixed = Vec.fixed(Integer::compareTo, elements);
            var sum = 0;
            for(var value: fixed) {
                sum += value;
            }
            assertEquals(1783293664, sum);
        });
    }

    @Test
    @Tag("Q5")
    public void testForEachIteratorRemove() {
        var elements = range(0, 100).boxed().toArray(Integer[]::new);
        var fixed = Vec.fixed(Integer::compareTo, elements);
        var it = fixed.iterator();
        it.next();
        assertThrows(UnsupportedOperationException.class, () -> it.remove());
    }

    @Test
    @Tag("Q5")
    public void testForEachIteratorNoNext() {
        var elements = range(0, 100).boxed().toArray(Integer[]::new);
        Collections.reverse(Arrays.asList(elements));

        var fixed = Vec.fixed(Integer::compareTo, elements);
        var min = -1;
        var it = fixed.iterator();
        for(var i = 0; i < 100; i++) {
            var value = it.next();
            if (value <= min) {
                throw new AssertionError("not sorted");
            }
            min = value;
        }
    }

    @Test
    @Tag("Q6")
    public void testMonoOneStream() {
        var mono = Vec.mono(String::compareTo, "foo");
        mono.stream().forEach(value -> {
            assertEquals("foo", value);
        });
    }

    @Test
    @Tag("Q6")
    public void testMonoEmptyStream() {
        var mono = Vec.mono(String::compareTo);
        mono.stream().forEach(value -> fail());
    }

    @Test
    @Tag("Q6")
    public void testMonoEmptyStreamFindFirst() {
        var mono = Vec.mono(String::compareTo);
        assertTrue(mono.stream().findFirst().isEmpty());
    }

    @Test
    @Tag("Q6")
    public void testMonoOneStreamFindFirst() {
        var mono = Vec.mono(String::compareTo, "foo");
        assertEquals("foo", mono.stream().findFirst().orElseThrow());
    }

    @Test
    @Tag("Q6")
    public void testFixedStreamToList() {
        var fixed = Vec.fixed(String::compareTo, "foo", "bar", "whizz", "baz");
        assertEquals(List.of("bar", "baz", "foo", "whizz"), fixed.stream().collect(toList()));
    }

    @Test
    @Tag("Q6")
    public void testVecStreamToList() {
        Vec<Integer> vec = Vec.fixed(Integer::compareTo, 5, 2, 8, 3);
        assertEquals(List.of(2, 3, 5, 8), vec.stream().collect(toList()));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamSimple() {
        var mono = Vec.toMono(Integer::compareTo, Stream.of(007));
        assertEquals(007, mono.stream().findFirst().orElseThrow());
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamEmpty() {
        var mono = Vec.toMono(String::compareTo, Stream.of());
        assertAll(() -> assertEquals(0, mono.size()), () -> assertTrue(mono.min().isEmpty()),
                () -> assertTrue(mono.max().isEmpty()), () -> assertTrue(mono.stream().findFirst().isEmpty()));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamSingleton() {
        var mono = Vec.toMono(Integer::compareTo, Stream.of(10));
        assertAll(() -> assertEquals(1, mono.size()), () -> assertEquals(10, mono.min().orElseThrow()),
                () -> assertEquals(10, mono.max().orElseThrow()),
                () -> assertEquals(10, mono.stream().findFirst().orElseThrow()));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamWithToManyElements() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Vec.toMono(Integer::compareTo, Stream.of(1, 2))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Vec.toMono(Integer::compareTo, Stream.of(1, 2, 3))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Vec.toMono(Integer::compareTo, Stream.of(1, 2, 3, 4))));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamEvaluateToManyElements() {
        assertThrows(IllegalArgumentException.class, () -> {
            Vec.toMono(Integer::compareTo, range(0, 10_000).peek(i -> {
                if (i / 100 == 1) {
                    fail();
                }
            }).boxed());
        });
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamSignature() {
        class Cmp implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }
        Cmp cmp = new Cmp();
        Mono<Integer> mono = Vec.toMono(cmp, Stream.of(55));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStreamSignature2() {
        class Cmp implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }
        Cmp cmp = new Cmp();
        Mono<Object> mono = Vec.<Object>toMono(cmp, Stream.of(55));
    }

    @Test
    @Tag("Q7")
    public void testToMonoFromStream() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> Vec.toMono(null, Stream.of())),
                () -> assertThrows(NullPointerException.class, () -> Vec.toMono(String::compareTo, null)),
                () -> assertThrows(NullPointerException.class,
                        () -> Vec.toMono(String::compareTo, Stream.of((String) null))));
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStreamSimple() {
        var fixed = Vec.toFixed(Integer::compareTo, Stream.of(007));
        assertEquals(007, fixed.stream().findFirst().orElseThrow());
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStreamEmpty() {
        var fixed = Vec.toFixed(String::compareTo, Stream.of());
        assertAll(() -> assertEquals(0, fixed.size()), () -> assertTrue(fixed.min().isEmpty()),
                () -> assertTrue(fixed.max().isEmpty()), () -> assertTrue(fixed.stream().findFirst().isEmpty()));
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStreamWith100Values() {
        var fixed = Vec.toFixed(Integer::compareTo, range(0, 100).boxed());
        assertAll(() -> assertEquals(100, fixed.size()), () -> assertEquals(0, fixed.min().orElseThrow()),
                () -> assertEquals(99, fixed.max().orElseThrow()), () -> assertEquals(100, fixed.stream().count()),
                () -> assertEquals(4950, fixed.stream().mapToInt(x -> x).sum()));
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStreamSignature() {
        class Cmp implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }
        Cmp cmp = new Cmp();
        Fixed<Integer> fixed = Vec.toFixed(cmp, range(0, 100).boxed());
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStreamSignature2() {
        class Cmp implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }
        Cmp cmp = new Cmp();
        Fixed<Object> fixed = Vec.<Object>toFixed(cmp, range(0, 100).boxed());
    }

    @Test
    @Tag("Q8")
    public void testToFixedFromStream() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> Vec.toFixed(null, Stream.of())),
                () -> assertThrows(NullPointerException.class, () -> Vec.toFixed(String::compareTo, null)),
                () -> assertThrows(NullPointerException.class,
                        () -> Vec.toFixed(String::compareTo, Stream.of("foo", "bar", null, "baz"))));
    }

    @Test
    @Tag("Q9")
    public void testFilterMonoOne() {
        Mono<Integer> mono = Vec.mono(Integer::compareTo, 63110);
        Mono<Integer> mono2 = mono.filter(i -> i % 2 == 0, Vec::toMono);
        assertEquals(List.of(63110), mono2.stream().collect(toList()));
    }

    @Test

    @Tag("Q9")
    public void testFilterMonoEmpty() {
        Mono<Integer> mono = Vec.mono(Integer::compareTo);
        Mono<Integer> mono2 = mono.filter(i -> i / 0 == 0, Vec::toMono);
        assertEquals(List.of(), mono2.stream().collect(toList()));
    }

    @Test
    @Tag("Q9")
    public void testFilterFixed() {
        Fixed<String> fixed = Vec.fixed(String::compareTo, "hello", "fixed", "vec");
        Fixed<String> fixed2 = fixed.filter(not(s -> s.startsWith("v")), Vec::toFixed);
        assertEquals(List.of("fixed", "hello"), fixed2.stream().collect(toList()));
    }

    @Test
    @Tag("Q9")
    public void testFilterMonoOneIsEmpty() {
        var mono = Vec.mono(String::compareTo, "hello");
        var mono2 = mono.filter(s -> s.startsWith("z"), Vec::toMono);
        assertEquals(List.of(), mono2.stream().collect(toList()));
    }

    @Test
    @Tag("Q9")
    public void testFilterMonoEmptyIsEmpty() {
        var mono = Vec.mono(String::compareTo);
        var mono2 = mono.filter(s -> s.startsWith("z"), Vec::toMono);
        assertEquals(List.of(), mono2.stream().collect(toList()));
    }

    @Test
    @Tag("Q9")
    public void testFilterSupremacy() {
        Fixed<Integer> fixed = Vec.fixed(Integer::compareTo, 10);
        Mono<Integer> mono = Vec.mono(Integer::compareTo, 10);

        Mono<Integer> fixedAsMono = fixed.filter(__ -> true, Vec::toMono);
        Fixed<Integer> monoAsFixed = mono.filter(__ -> true, Vec::toFixed);

        assertAll(() -> assertEquals(List.of(10), fixedAsMono.stream().collect(toList())),
                () -> assertEquals(List.of(10), monoAsFixed.stream().collect(toList())));
    }

    @Test
    @Tag("Q9")
    public void testFilter() {
        Mono<Integer> mono0 = Vec.mono(Integer::compareTo);
        Mono<Integer> mono1 = Vec.mono(Integer::compareTo, 63110);
        Fixed<String> fixed = Vec.fixed(String::compareTo, "hello", "fixed", "vec");
        assertAll(() -> assertThrows(NullPointerException.class, () -> mono0.filter(null, Vec::toMono)),
                () -> assertThrows(NullPointerException.class, () -> mono0.filter(null, Vec::toFixed)),
                () -> assertThrows(NullPointerException.class, () -> mono0.filter(__ -> true, null)),
                () -> assertThrows(NullPointerException.class, () -> mono1.filter(null, Vec::toMono)),
                () -> assertThrows(NullPointerException.class, () -> mono1.filter(null, Vec::toFixed)),
                () -> assertThrows(NullPointerException.class, () -> mono1.filter(__ -> true, null)),
                () -> assertThrows(NullPointerException.class, () -> fixed.filter(null, Vec::toMono)),
                () -> assertThrows(NullPointerException.class, () -> fixed.filter(null, Vec::toFixed)),
                () -> assertThrows(NullPointerException.class, () -> fixed.filter(__ -> true, null)));
    }

    @Test
    @Tag("Q10")
    public void testOneParameterFilterMonoOne() {
        Mono<String> mono = Vec.mono(String::compareTo, "hello");
        Mono<String> mono2 = mono.filter(s -> s.startsWith("h"));
        assertEquals(List.of("hello"), mono2.stream().collect(toList()));
    }

    @Test
    @Tag("Q10")
    public void testOneParameterFilterMonoEmpty() {
        Mono<String> mono = Vec.mono(String::compareTo);
        Mono<String> mono2 = mono.filter(s -> s.startsWith("h"));
        assertEquals(List.of(), mono2.stream().collect(toList()));
    }

    @Test
    @Tag("Q10")
    public void testOneParameterFilterFixed() {
        Fixed<Integer> fixed = Vec.fixed(Integer::compareTo, 12, 37, 6);
        Fixed<Integer> fixed2 = fixed.filter(i -> i % 2 == 0);
        assertEquals(List.of(6, 12), fixed2.stream().collect(toList()));
    }

    @Test
    @Tag("Q10")
    public void testOneParameterFilterVec() {
        Vec<String> vec = Vec.fixed(String::compareTo, "foo", "bar");
        Vec<String> vec2 = vec.filter(__ -> true);
        assertEquals(List.of("bar", "foo"), vec2.stream().collect(toList()));
    }

    @Test
    @Tag("Q10")
    public void testOneParameterFilter() {
        Mono<Integer> mono0 = Vec.mono(Integer::compareTo);
        Mono<Integer> mono1 = Vec.mono(Integer::compareTo, 63110);
        Fixed<String> fixed = Vec.fixed(String::compareTo, "hello", "fixed", "vec");
        assertAll(() -> assertThrows(NullPointerException.class, () -> mono0.filter(null)),
                () -> assertThrows(NullPointerException.class, () -> mono1.filter(null)),
                () -> assertThrows(NullPointerException.class, () -> fixed.filter(null)));
    }
}

 */