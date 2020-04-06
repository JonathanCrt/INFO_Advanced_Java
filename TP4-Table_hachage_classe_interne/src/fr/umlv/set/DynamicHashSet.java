package fr.umlv.set;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;


public final class DynamicHashSet <V> {
	

	private Entry<V>[] hashSet;
	private int size;

	// intern class
	static class Entry<E> {
		final E value;
		final Entry<E> next;
		
		public Entry(E value, Entry<E> next) {
			this.value = value;
			this.next = next;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry<?> other = (Entry<?>) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public DynamicHashSet() {
		this.hashSet = (Entry<V>[]) new Entry<?>[10000];
		this.size = 0;
	}
	
	/**
	 * calculate hashcode of value parameter
	 * @param value
	 * @return
	 */
	private int hash (V value) {
		return value.hashCode() &(hashSet.length-1);
	}
	
	/**
	 * if size of DynamicHash is filled with half, increase size to keep performance.
	 */
	@SuppressWarnings("unchecked")
	private void grow() {
		var length= this.hashSet.length * 2;
		var cloneHashSet =  this.hashSet.clone();
		var newHashSet =  (Entry<V>[]) new Entry<?>[length];
		
		this.hashSet = newHashSet;
		for(int i = 0; i < cloneHashSet.length; i++) {
			for(var entry = cloneHashSet[i]; entry != null; entry = entry.next) {
				this.add(entry.value);
			}
		}
	}
	
	/**
	 * add value to HashSet , private method
	 * @param valparam value to add
	 */
	private void addIntoHashSet(V valparam) {
		var position = hash(valparam);
		var tmp = this.hashSet[position];
		
		if(this.contains(valparam)) {
			return;
		}
		
		if(size > hashSet.length/2) {
			this.grow();
		}
		
		while(tmp != null) {
			if(tmp.value == valparam) {
				return;
			}
			tmp = tmp.next;
		}
		var ent = new Entry<V>(valparam, hashSet[position]);
		this.hashSet[position] = ent;
		size++;
	}
	
	/**
	 * add value to HashSet , public method
	 * @param v 
	 */
	public void add(V v) {
		Objects.requireNonNull(v);
		this.addIntoHashSet(v);
	}
	
	
	/**
	 * return size of hashSet
	 * @return int size of hashSet
	 */
	public int size () {
		return size;
	}
	
	/**
	 * takes a lambda parameter, it will be called for each whole present in all
	 * @param consumer
	 */
	public void forEach(Consumer<? super V> consumer) {
		Objects.requireNonNull(consumer);
		for(var elt: hashSet) {
			for(var entry = elt; entry != null; entry = entry.next) {
				consumer.accept(entry.value);
			}
		}
	}
	/**
	 * returns true if the whole is all, false otherwise (private method)
	 * @param value
	 * @return boolean depends if hasSet contains value or not.
	 */
	private boolean containsIntoHashSet(V value) {
		var position= hash(value);
		for(var tmp = hashSet[position]; tmp != null; tmp = tmp.next) {
			if(tmp.value.equals(value)) {
				return true;
			}
	
		}
		return false;
	}
	
	/**
	 * returns true if the whole is all, false otherwise (public method)
	 * @param value
	 * @return boolean depends if hasSet contains value or not.
	 */
	public boolean contains(V v) {
		Objects.requireNonNull(v);
		return this.containsIntoHashSet(v);
	}
	
	
	/**
	 * copy a collection of items in the dynamic current HashSet
	 * @param collection
	 */
	public void addAll(Collection<? extends V> collection){
	    for(V value: collection){
	        add(value);
	    }
	}
}
