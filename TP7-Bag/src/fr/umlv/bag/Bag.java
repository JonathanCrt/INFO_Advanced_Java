package fr.umlv.bag;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;

public interface Bag<E> extends Iterable<E> {
    public int add(E element, int count);
    public int count(Object element);
    public void forEach(Consumer<? super E> consumer);
    public Iterator<E> iterator();
    public AbstractCollection<E> asCollection();
    //public int size();
    
    
    /**
     * La methode nCopies retourne une Collection immutable
     */
    
    /*
    default Collection<E> asCollection(){
    	var bag = this;
    	
    	return new AbstractCollection<E>() {

			@Override
			public Iterator<E> iterator() {
				return bag.iterator();
			}

			@Override
			public int size() {
				return bag.size();
			}
			
			@Override
			public boolean contains(Object obj) {
				return bag.count(obj) != 0;
			}
			
		};
    }
    */
    
    /**
     * 
     * @param <T>
     * @return
     */
    public static <T> Bag<T> createSimpleBag() {
    	return new BagImpl<T>(); //return classe which implement Bag
    }
    /**
     * 
     * @param <T>
     * @return
     */
    public static <T> Bag<T> createOrderedByInsertionBag(){
		return new BagImpl<T>(new LinkedHashMap<>());
	}
    
    /**
     * 
     * @param <T>
     * @param comparator
     * @return
     */
    public static<T> Bag<T> createOrderedByElementBag(Comparator<? super T> comparator){
    	return new BagImpl<T>(new TreeMap<T, Integer>(comparator));
    }
    
    /*
	@SuppressWarnings("unchecked")
    public static<T> Bag<T> createOrderedByElementBagFromCollection(Collection<? super T> collection){
    	Objects.requireNonNull(collection);
		Bag<T> orderedBag = createOrderedByElementBag((Comparator<? super T>) Comparator.naturalOrder());
    	collection.forEach(element -> orderedBag.add((T) element, 1));
    	return orderedBag;
	}
	*/
    
    
	public static<T extends Comparable<? super T>>Bag<T> createOrderedByElementBagFromCollection(
		Collection<? extends T> collection){
		Objects.requireNonNull(collection);
		var bag = createOrderedByElementBag(Comparator.<T>naturalOrder());
		collection.forEach(element -> bag.add(element, 1));
		return bag;
	}
	
}