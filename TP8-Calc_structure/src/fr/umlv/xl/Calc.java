package fr.umlv.xl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// V = case type 
public class Calc<V> {

	private final HashMap<String, Supplier<? extends V>> map = new HashMap<>();

	/**
	 * 
	 * @param nameCase
	 * @param function
	 */
	public void set(String nameCase, Supplier<V> function) {
		Objects.requireNonNull(nameCase);
		Objects.requireNonNull(function);
		map.put(nameCase, function); // get -> Supplier
	}

	/**
	 * 
	 * @param nameCase
	 * @return
	 */
	public Optional<V> eval(String nameCase) {
		Objects.requireNonNull(nameCase);
		var element = map.get(nameCase);
		// Un Optional est un if
		return (element == null) ? Optional.empty() : Optional.of(element.get());
		
	}
	
	/**
	 * 
	 */
	public void forEach(BiConsumer<? super String, ? super V> consumer) { // tout type qui est String ou qui herite de String
		Objects.requireNonNull(consumer);
		map
		.entrySet()
		.stream()
		.forEach((mapper) -> consumer.accept(mapper.getKey(), mapper.getValue().get()));
	}
	
	/**
	 * 
	 */
	public String toString() {
		var joiner = new StringJoiner(", ", "{", "}");
		map.forEach((element, value) -> joiner.add(element + "=" + value.get()));
		return joiner.toString();
	}
	

	@FunctionalInterface // 
	public interface Group<T> { 
		
		public Stream<T> values(); // Renvoi un stream d elts V - methode abstraite
		
		@SafeVarargs
		public static  <E> Group<E> of(E...elts) {
			var list = List.of(elts); // Fais une copie defensive Transforme le tableau en liste non mutable qui ne contient pas de null 
			return () -> {
				return list.stream();
			};
			//return List.of(elts)::stream;
		}
		
		/**
		 * default = car interface fonctionelle (code de la methode a l interieur de linterface, uniquement avece ce qu on connait)
		 * @param consumer
		 */
		public default void forEach(Consumer<? super T> consumer) {
			Objects.requireNonNull(consumer);
			values().forEach(consumer); // appliquer le consumer sur toutes les valeurs
		}
		
		/**
		 * 
		 * @param startLine
		 * @param endLine
		 * @param startCol
		 * @param endCol
		 * @return
		 */
		public static  Group<String> cellMatrix(int startLine, int endLine, char startCol, char endCol){
			if(startLine> endLine || startCol > endCol) {
				throw new IllegalArgumentException("invalid arguments");
			}
			return () -> IntStream.range(startLine, endLine + 1).boxed()
					.flatMap(
					line -> IntStream.range(startCol, endCol+1)
					.mapToObj(e -> ((char) e + "" + line))
			);
		}
		
		
		/**
		 * 
		 * @param of
		 * @return
		 */
		public default Group<T> ignore(Set<? super T> of) {
			Objects.requireNonNull(of);
			return () -> values()
					    .filter(elt -> !of.contains(elt));
		}
		
		/**
		 * 
		 * @param <E>
		 * @param function
		 * @return
		 */
		public default <E> Stream<E> eval(Function<T, Optional<E>> function) {
			Objects.requireNonNull(function);
			return values()
					.flatMap(elt -> function.apply(elt).stream());
		}
		
	}
	
	
	
	
	

}
