package fr.umlv.seq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq2<E> implements Iterable<E> {
	private E[] seq;
	private Function<Object,  E> myFunction;
	
	@SuppressWarnings("unchecked")
	private Seq2(List<Object> seq) {
		this(seq, (elt) ->(E) elt);
	}
	
	@SuppressWarnings("unchecked")
	private Seq2(List<Object> seq, Function <Object,  E> func) {
		this.seq = (E[]) seq.toArray();
		this.myFunction = func;
	}
	
	
	/**
	 * I want to add elements into sequence
	 * parameter -> We want same type of T or sub-Type
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public static <T> Seq2<T> from(List<? extends T> list) {
		return new Seq2<>(List.copyOf(list), x -> (T)x); // T not mandatory
	}
	
	/**
	 * initialize a Seq with element split by comma
	 * @param <T>
	 * @param args
	 */
	@SafeVarargs
	public  static <T> Seq2<T> of(T...args){ // we have garanty of only vargs of T
		Objects.requireNonNull(args);
		return new Seq2<>(List.of(args));
	}
	
	/**
	 * memo -> P-E-C-S or RTFM to see doc of Collections 
	 * @param consumer
	 */
	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		for(var elt : seq) {
			consumer.accept((E) this.myFunction.apply(elt)); 
		}
	}
	
	/**
	 * 
	 * @param <W>
	 * @param mapper
	 * @return
	 */
	public <W> Seq2<W> map (Function<? super E, ? extends W> mapper) {
		Objects.requireNonNull(mapper);
		return new Seq2<W>(List.of(seq), this.myFunction.andThen(mapper)); 

	}
	
	/**
	 * return element which match with index gives in parameter
	 * @param index
	 * @return
	 */
	public E get(int index) {
		var elt = seq[index];
		return myFunction.apply(elt);
	}
	
	/**
	 * return length of seq
	 * @return
	 */
	public int size() {
		return this.seq.length;
	}
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "<", ">");
		
		Arrays.stream(seq)
		.forEach((elt) -> {
			joiner.add(myFunction.apply(elt).toString());
		});
		return joiner.toString();
	}
	
	/**
	 * return first element of the seq
	 * @return
	 */
	public Optional<E> first() {
		if (seq.length == 0) {
			return Optional.empty();
		}
		var mappedElt = myFunction.apply(seq[0]);
		return (Optional<E>) Optional.of(mappedElt);
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int curs;
			@Override
			public boolean hasNext() { // Does it exists a next element?
				return this.curs != seq.length;
			}

			@Override
			public E next() {  // returns the current element and moves to the next or NoSuchElementException
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				
				var toIndex = myFunction.apply(seq[curs]);
				curs++;
				return toIndex;
			}
			
		};
		
	}
	
	/**
	 * returns a stream of seq elements
	 * @return
	 */
	public Stream<E> stream() {
		return StreamSupport.stream(Spliterators.spliterator(seq,
				  Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED), false);
	}
	
	
}
