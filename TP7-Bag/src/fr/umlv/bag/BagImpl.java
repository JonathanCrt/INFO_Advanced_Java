package fr.umlv.bag;

import java.util.AbstractCollection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
//import java.util.stream.IntStream;
//import java.util.NoSuchElementException;
import java.util.function.Supplier;


public class BagImpl<T>  implements Bag<T>{
	private Map<T, Integer> map;
	private int size;

	public BagImpl() {
		this.map = new HashMap<>();
	}
	
	public BagImpl(Map<T, Integer> hMap) {
		this.map = hMap;
		this.size=0;
	}

	@Override
	public int add(T element, int count) {
		Objects.requireNonNull(element);
		if(count <= 0) {
			throw new IllegalArgumentException("count must be positive ! ");
		}
		//map.merge(element, count, Integer::sum);
		this.size+=count;
		map.compute(element, (key, oldValue)-> {
			if(oldValue == null) {
				return count;
			}
			else {
				return oldValue + count;
			}
		});
		return count(element);
		
	}

	@Override
	public int count(Object element) {
		Objects.requireNonNull(element);
		if(map.get(element) == null) {
			return 0;
		}
		return map.get(element);
	}
	
	/*
	@Override
	public int size() {
		return map.size();
	}
	*/

	@Override
	public void forEach(Consumer<? super T> consumer) {
		Objects.requireNonNull(consumer);
		//map.keySet().forEach(consumer);
		//v1
		/*
		for(var entry: map.entrySet()) { //entrySet : liste des clés /valeurs
			T key = entry.getKey();
			for(int i = 0; i < entry.getValue(); i++) {
				consumer.accept(key);
			}
		}
		*/
		//v2
		map.forEach((key, value) -> {
			for(var i = 0; i < value; i++) {
				consumer.accept(key); // execute la méthode d'instance de consumer (interface fonctionnelle)
			}
		});
		//v3
		/*
		map.forEach((key, value) -> {
			IntStream.range(0, value).forEach(__ -> consumer.accept(key));
		});
		*/
	}
	
	
	@Override
	public Iterator<T> iterator() {
		
		var mapIterator = map.entrySet().iterator();
		return new Iterator<>() { // se souvenir dans l'elt ou l'on est
			
			private T current = null; // dernier element deja renvoye
			private int remaining = 0;  // nombre d'elements restants a voir - Si remaining = 0 je suis a la fin  
			// Etat : se positionner sur la prochaine valeur que l on va renvoyer : plus complique
									  
			@Override
			public boolean hasNext() { // Does it exists a next element? never change state of iterator
				return remaining != 0 || mapIterator.hasNext();
			}

			@Override
			public T next() {
				if(!hasNext()) {
					throw new NoSuchElementException(); // test si il y a bien un elt a renvoyer
				}
				if(remaining != 0) {
					remaining--;
					return current;
				}
				
				var next = mapIterator.next();
				current = next.getKey();
				remaining = next.getValue() -1;
				return current;
			}
			
		};
	}
	
	
	/*
	@Override
	public Iterator<T> iterator(){
		return map.entrySet()
		.stream()
		.flatMap(mapper -> Collections.nCopies(mapper.getValue(), mapper.getKey()).stream()).iterator();
	}
	*/

	public AbstractCollection<T> asCollection(){
    	var bag = this;
    	return new AbstractCollection<T>() {

			@Override
			public Iterator<T> iterator() {
				return bag.iterator();
			}
			@Override
			public int size() {
				return bag.size;
			}
			
			@Override
			public boolean contains(Object obj) {
				return bag.count(obj) != 0;
			}
			
		};
    }
	
	@Override
    public boolean equals(Object o) {
        if (!(o instanceof BagImpl<?>)) return false;
        BagImpl<?> bag1 = (BagImpl<?>) o;
        return size == bag1.size &&
                Objects.equals(map, bag1.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, size);
    }
    
    
	/**
	 * 
	 * @param sup
	 */
	public static <T> void printAndTime(Supplier <T> sup) {
		long start = System.currentTimeMillis();
		T result = sup.get();
	    long end = System.currentTimeMillis();
	    System.out.println(" elapsed time  " + (end - start) + " millisecondes");
	}
	
	
	public static void main(String[] args) {
		var bag = new BagImpl<Long>();
		printAndTime(() -> bag.iterator());
	}
   
	
}
