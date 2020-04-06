package fr.umlv.irig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import org.junit.Test;

@SuppressWarnings("static-method")
public class ContainerTest {
  // Q1
  
  
  @Test
  public void testContainerSimple() {
    Container<String> c = new Container<>(x -> x.length());
    c.add("hello");
    c.add("container");
  }
  @Test
  public void testContainerInteger() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(3);
    c.add(7);
  }
  @Test
  public void testContainerProjection() {
    Container<Integer> c = new Container<Integer>((Object o) -> o.hashCode());
    c.add(2);
    c.add(21);
  }
  @Test
  public void testSize() {
    Container<String> c = new Container<>(x -> x.length());
    assertEquals(0, c.size());
    c.add("hello");
    assertEquals(1, c.size());
    c.add("container");
    assertEquals(2, c.size());
  }
  @Test(expected = NullPointerException.class)
  public void testAddNull() {
    new Container<>(o -> 1).add(null);
  }
  @Test
  public void testAddTwiceTheSameObject() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("banzai");
    try {
      c.add("banzai");
      fail();
    } catch(IllegalStateException e) {
      // ok
    }
  }
  @Test
  public void testAddTwiceTheSameProjection() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("foo");
    try {
      c.add("bar");
      fail();
    } catch(IllegalStateException e) {
      // ok
    }
  }
  @Test(expected = IllegalStateException.class)
  public void testAddNegativeProjection() {
    Container<Object> c = new Container<>(__ -> -1);
    c.add(new Object());
  }
  @Test
  public void testAddALot() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(1_000_001);  // presize to speedup
    IntStream.range(0, 1_000_000).forEach(i -> c.add(i));
    assertEquals(1_000_001, c.size());
  }
  /*@Test(timeout = 5000)
  public void testAddALotNoPresize() {
    Container<Integer> c = new Container<>(x -> x);
    IntStream.range(0, 1_000_000).forEach(i -> c.add(i));
    assertEquals(1_000_000, c.size());
  }*/
  
  
  // Q2
  
  @Test
  public void testContainsSimple() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("foo");
    assertTrue(c.contains("foo"));
  }
  @Test
  public void testContainsNotEquals() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("foo");
    assertFalse(c.contains("bar"));
  }
  @Test
  public void testContainsNotEqualsNotSameLength() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("baz");
    assertFalse(c.contains("djljhqsdhkuddjhfdqsluucdhhqdsu"));
  }
  @Test(expected = NullPointerException.class)
  public void testContainsNPE() {
    Container<String> c = new Container<>(s -> s.length());
    c.contains(null);
  }
  @Test(expected = IllegalStateException.class)
  public void testContainsNegativeProjection() {
    Container<Object> c = new Container<>(__ -> -1);
    c.contains(new Object());
  }
  
  
  
  // Q3
  
  @Test
  public void testForEachSimple() {
    Container<String> c = new Container<>(s -> s.length());
    c.add("hello");
    c.forEach(s -> assertEquals("hello", s));
  }
  @Test
  public void testForEachWithHoles() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(17);
    c.add(777);
    c.add(56);
    ArrayList<Integer> list = new ArrayList<>();
    c.forEach(i -> list.add(i));
    assertEquals(List.of(17, 56, 777), list);
  }
  @Test
  public void testForEachOrder() {
    Container<String> c = new Container<>(x -> x.length());
    c.add("aaa");
    c.add("bb");
    c.add("c");
    ArrayList<String> list = new ArrayList<>();
    c.forEach(s -> list.add(s));
    assertEquals(List.of("c", "bb", "aaa"), list);
  }
  
  @Test
  public void testForEachEmpty() {
    Container<Object> c = new Container<>(o -> 1);
    c.forEach(__ -> fail());
  }
  
  
  // Q4
  
  @Test
  public void testToStringSimple() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(1);
    c.add(2);
    c.add(3);
    assertEquals("{1, 2, 3}", c.toString());
  }
  @Test
  public void testToStringEmpty() {
    Container<Integer> c = new Container<>(x -> x);
    assertEquals("{}", c.toString());
  }
  
  
  // Q5
  
  @Test
  public void testIteratorSimple() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(4);
    c.add(42);
    c.add(7);
    Iterator<Integer> it = c.iterator();
    assertTrue(it.hasNext());
    assertEquals(4, (int)it.next());
    assertEquals(7, (int)it.next());
    assertEquals(42, (int)it.next());
    assertFalse(it.hasNext());
  }
  @Test
  public void testIteratorEmpty() {
    Container<Integer> c = new Container<>(x -> 1);
    Iterator<Integer> it = c.iterator();
    assertFalse(it.hasNext());
    try {
      it.next();
    } catch(NoSuchElementException e) {
      // ok
    }
  }
  @Test
  public void testIteratorHasNextIdempotent() {
    Container<String> c = new Container<>(x -> x.length());
    c.add("4");
    c.add("777");
    c.add("42");
    Iterator<String> it = c.iterator();
    IntStream.range(0, 10).forEach(__ -> assertTrue(it.hasNext()));
    assertEquals("4", it.next());
    IntStream.range(0, 10).forEach(__ -> assertTrue(it.hasNext()));
    assertEquals("42", it.next());
    IntStream.range(0, 10).forEach(__ -> assertTrue(it.hasNext()));
    assertEquals("777", it.next());
    IntStream.range(0, 10).forEach(__ -> assertFalse(it.hasNext()));
  }
  @Test
  public void testIteratorIterateTooFar() {
    Container<Integer> c = new Container<>(x -> 100);
    c.add(6);
    Iterator<Integer> it = c.iterator();
    assertEquals(6, (int)it.next());
    try {
      it.next();
    } catch(NoSuchElementException e) {
      // ok
    }
  }
  @Test
  public void testIteratorALot() {
    Container<Integer> c = new Container<>(x -> x);
    IntStream.range(0, 10_000).forEach(i -> c.add(i));
    Iterator<Integer> it = c.iterator();
    for(int value = 0; it.hasNext(); value++) {
      assertEquals(value, (int)it.next());
    }
  }
  
  /*
  // Q6
  
  @Test
  public void testIteratorRemoveSimple() {
    Container<Integer> c = new Container<>(x -> x);
    c.add(3);
    c.add(4);
    c.add(5);
    Iterator<Integer> it = c.iterator();
    it.next();
    it.remove();
    assertFalse(c.contains(3));
    assertEquals(2, c.size());
    it.next();
    it.remove();
    assertFalse(c.contains(4));
    assertEquals(1, c.size());
    it.next();
    it.remove();
    assertFalse(c.contains(5));
    assertEquals(0, c.size());
  }
  @Test(expected = IllegalStateException.class)
  public void testIteratorRemoveNothingToRemove() {
    Container<String> c = new Container<>(s -> s.charAt(0));
    c.add("foo");
    c.add("bar");
    Iterator<String> it = c.iterator();
    it.remove();
  }
  @Test
  public void testIteratorRemoveTryToRemoveTwice() {
    Container<String> c = new Container<>(s -> s.charAt(0));
    c.add("foo");
    c.add("bar");
    Iterator<String> it = c.iterator();
    it.next();
    it.remove();
    try {
      it.remove();
    } catch(IllegalStateException e) {
      // ok
    }
  }
  
  
  // Q7
  
  @Test
  public void testAsSafeCollectionSimple() {
    Container<Integer> container = new Container<>(x -> x);
    container.add(3);
    container.add(4);
    container.add(5);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    assertEquals(3, collection.size());
    assertEquals(List.of(3, 4, 5), new ArrayList<>(collection));
  }
  @Test
  public void testAsSafeCollectionAsView() {
    Container<Integer> container = new Container<>(x -> x);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    container.add(3);
    container.add(4);
    container.add(5);
    assertEquals(3, collection.size());
    assertEquals(List.of(3, 4, 5), new ArrayList<>(collection));
  }
  
  // Q8
  
  @Test
  public void testAsSafeCollectionAdd() {
    Container<Integer> container = new Container<>(x -> x);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    assertTrue(collection.add(3));
    assertTrue(collection.add(4));
    assertTrue(collection.add(5));
    assertEquals(3, container.size());
    ArrayList<Integer> list = new ArrayList<>();
    container.forEach(i -> list.add(i));
    assertEquals(List.of(3, 4, 5), list);
  }
  @Test(expected = ClassCastException.class)
  public void testAsSafeCollectionAddWrongType() {
    Container<String> container = new Container<>(x ->  x.length());
    container.add("hello");
    Collection<String> safe = container.asSafeCollection(String.class);
    Collection<Integer> unsafe = (Collection<Integer>)(Collection<?>)safe;
    unsafe.add(3);
  }
  @Test(expected = IllegalStateException.class)
  public void testAsSafeCollectionAddTwiceSameValue() {
    Container<Integer> container = new Container<>(x -> x);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    collection.add(3);
    collection.add(3);
  }
  @Test(expected = IllegalStateException.class)
  public void testAsSafeCollectionAddNegativeProjection() {
    Container<Object> container = new Container<>(x ->  -1);
    Collection<Object> collection = container.asSafeCollection(Object.class);
    collection.add(3);
  }
  @Test(expected = NullPointerException.class)
  public void testAsSafeCollectionAddNull() {
    Container<Object> container = new Container<>(x ->  -1);
    Collection<Object> collection = container.asSafeCollection(Object.class);
    collection.add(null);
  }
  
  
  // Q9
  
  @Test
  public void testAsSafeCollectionContainsSimple() {
    Container<Integer> container = new Container<>(x -> x);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    collection.add(45);
    assertTrue(container.contains(45));
    assertTrue(collection.contains(45));
  }
  @Test
  public void testAsSafeCollectionContainsAlreadyPresent() {
    Container<Integer> container = new Container<>(x -> x);
    container.add(31);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    assertTrue(container.contains(31));
    assertTrue(collection.contains(31));
  }
  @Test
  public void testAsSafeCollectionContainsNotSameType() {
    Container<Integer> container = new Container<>(x ->  x);
    container.add(331);
    Collection<Integer> collection = container.asSafeCollection(Integer.class);
    assertFalse(collection.contains("EEI"));
  }
  @Test(expected = NullPointerException.class)
  public void testAsSafeCollectionContainsNull() {
    Container<Object> container = new Container<>(x ->  -1);
    Collection<Object> collection = container.asSafeCollection(Object.class);
    collection.add(null);
  }
  
  
  // Q10
  // write your own tests
  */
}