package fr.umlv.seq;

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

public class Seq <E> implements Iterable<E>{
	
	private final List<Object> seq;
	private Function<Object,  E> myFunction; // Object -> anything, function return E 
	
	/**
	 * this contructor is private because of our factory method 
	 * @param seq
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	private Seq(List<Object> seq) {
		//this.seq = Objects.requireNonNull(seq);
		this(seq, (elt) ->(E) elt);
	}
	
	private Seq(List<Object> seq, Function <Object,  E> func) {
		this.seq = seq;
		//this.function = Function.identity(); // Function.indenity renvoie la même chose qu'en entrée avec le même type
		this.myFunction = func;
	}
	
	
	/**
	 * I want to add elements into sequence
	 * parameter -> We want same type of T or sub-Type
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public static <T> Seq<T> from(List<? extends T> list) {
		return new Seq<>(List.copyOf(list), x -> (T)x); // T not mandatory
	}
	
	/**
	 * initialize a Seq with element split by comma
	 * @param <T>
	 * @param args
	 */
	@SafeVarargs
	public  static <T> Seq<T> of(T...args){ // we have garanty of only vargs of T
		Objects.requireNonNull(args);
		return new Seq<>(List.of(args));
	}
	
	/**
	 * memo -> P-E-C-S or RTFM to see doc of Collections 
	 * @param consumer
	 */
	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		//seq.forEach(consumer);
		for(var elt : seq) {
			consumer.accept((E) this.myFunction.apply(elt))  ; 
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
	
	public <W> Seq<W> map (Function<? super E, ? extends W> mapper) {
		Objects.requireNonNull(mapper);
		return new Seq<W>(seq, this.myFunction.andThen(mapper)); // compose -> compose avec l'argument puis avec le this
		
		//return null; -> first to test
	}
	
	/**
	 * return element which match with index gives in parameter
	 * @param index
	 * @return
	 */
	public E get(int index) {
		//return seq.get(index);
		var elt = seq.get(index);
		return myFunction.apply(elt);
	}
	
	/**
	 * 
	 * return size of seq
	 * @return
	 */
	public int size() {
		return this.seq.size();
	}
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "<", ">");
		seq.forEach((elt) -> {
			joiner.add(myFunction.apply(elt).toString());
		});
		return joiner.toString();
	}
	
	/**
	 * return first element of the seq
	 * @return
	 */
	public Optional<E> findFirst() {
		if (seq.isEmpty()) {
			return Optional.empty();
		}
		var mappedElement = myFunction.apply(seq.get(0));
		return (Optional<E>) Optional.of(mappedElement);
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int curs;
			@Override
			public boolean hasNext() { // Does it exists a next element?
				return this.curs != seq.size();
			}

			@Override
			public E next() {  // returns the current element and moves to the next or NoSuchElementException
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				
				var toIndex = myFunction.apply(seq.get(curs));
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
		return StreamSupport.stream(Spliterators.spliterator(seq.toArray(),
				  Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED), false);
	}
	
	
	
	
	public static void main(String[] args) {
		  var seq = Seq.from(List.of(78, 56, 34, 23));
	      System.out.println(seq.size());  // 4
	      System.out.println(seq.get(2));  // 34
	      
	}

	
}
