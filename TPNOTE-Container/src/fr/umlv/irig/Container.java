package fr.umlv.irig;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class Container<E> {
	private final static int SIZE_INT = 400;
	private final ToIntFunction<? super E> myIntegerfunction;
	private E[] container;
	private int nbElt;

	@SuppressWarnings("unchecked")
	public Container(ToIntFunction<? super E> myFunction) {
		this.nbElt = 0;
		this.myIntegerfunction = Objects.requireNonNull(myFunction);
		this.container = (E[]) new Object[SIZE_INT];
	}
	
	/**
	 * test if length should be grow
	 * @param index
	 * @return true if size should be grow, false otherwise
	 */
	private boolean containerShouldBeGrow(int index) {
		return (this.container.length/2) < index;
	}
	
	private void checkIfIndexIsPositive(int index) {
		if(index < 0) {
			throw new IllegalStateException("index must be positive");
		}
	}
	
	/**
	 * grow length of container
	 */
	private void growcontainer() {
		var newLength = this.container.length * 2;
		this.container = Arrays.copyOf(container, newLength);
	}
	
	/**
	 * Add an element into container
	 * @param elt
	 */
	public void add(E elt) {
		Objects.requireNonNull(elt);
		var index = myIntegerfunction.applyAsInt(elt); 
		this.checkIfIndexIsPositive(index);
		while(containerShouldBeGrow(index)) { // Mais attention a la mémoire
			this.growcontainer();
		}
		
		if(container[index] != null) {
			throw new IllegalStateException("case is already taken");
		}
		
		container[index] = elt;
		this.nbElt++;
	}
	
	/**
	 * Q2 -> Contains prend un E car sinon on ne peut pas appeler la fonction de projection
	 * @param elt
	 * @return
	 */
	public boolean contains(E elt) {
		Objects.requireNonNull(elt);
		var index = myIntegerfunction.applyAsInt(elt);
		this.checkIfIndexIsPositive(index);
		if(container[index] != null && container[index].equals(elt)) {
			return true;  // element already exist into container
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param action
	 */
	private void forEachContainer(Consumer<? super E> action) {
		Objects.requireNonNull(action);	
		for(var elt:container) {
			if(elt != null) {
				action.accept(elt);
			}
		}
	
	}
	
	public void forEach(Consumer<? super E> action) {
		this.forEachContainer(action);
	}
	
	/**
	 * return size of container
	 * @return int size of container
	 */
	public int size() {
		return this.nbElt;
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterator<E> iterator(){
		return new Iterator<E>() {
			private int cursor = this.nextToReturn(0);
			
			/*
			 * trouver le premier élément non null
			 */
			private int nextToReturn(int from) { 
				for(var firstElt = from; firstElt < container.length; firstElt++) {
					if(container[firstElt] != null) {
						return firstElt;
					}
				}
				return -1; // -1 si plus personne
			}
			
			@Override
			public boolean hasNext() {
				return cursor != -1;
			}

			@Override
			public E next() {
				if(!this.hasNext()) {
					throw new NoSuchElementException();
				}
				var tmp = cursor;  
				cursor = this.nextToReturn(cursor + 1);
				return container[tmp];
				
			}
		};
	}
	
		
	@Override
	public String toString() {
		var st = new StringJoiner(", ", "{", "}");
		this.forEach((element) -> st.add(element.toString()));
		return st.toString();
	}

}
