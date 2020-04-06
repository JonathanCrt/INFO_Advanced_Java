package fr.umlv.xl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
	
	// même type parametre que Calc / interface fonctionelle -> lambda
	@FunctionalInterface // Avec cette annotation @FunctionalInterface, on indique au compilateur de bien vérifier que l’interface possède bien une seule méthode abstraite.
	public interface Group<T> { // interface static -> pas le meme type parametree
		
		public Stream<T> values(); // Renvoi un stream d elts V - methode abstraite
		
		@SafeVarargs
		public static  <E> Group<E> of(E...elts) {
			var list = List.of(elts); // Fais une copie defensive Transforme le tableau en liste non mutable qui ne contient pas de null 
			//A calculer avant ! (fais unn requireNonNull) Sinon il est calcule au mauvais moment, 
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
		 * @param <T>
		 * @param startLine
		 * @param endLine
		 * @param startCol
		 * @param endCol
		 * @return
		 */
		public static <T>  Group<T> cellMatrix(int startLine, int endLine, char startCol, char endCol){
			var array = new ArrayList<String>();
			for(var i = startLine ;  i <= endLine; i++) {
				for(var j =(int )startCol; j <= endCol; j++) {
					array.add(" " + (char)j + i );
				}
				
			}
			return null;
			//return Group.of(array);
		}
		
		
	}
	
	
	
	
	

}
