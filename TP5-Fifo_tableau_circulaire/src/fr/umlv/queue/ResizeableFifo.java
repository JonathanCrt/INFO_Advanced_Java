package fr.umlv.queue;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.StringJoiner;

public class ResizeableFifo<E> extends AbstractQueue<E> implements Iterable<E>, Queue<E> {
	private E [] fifo;
	private int tail;  // position of next element to insert (pointer)
	private int head;  // position of the first element (pointer)
	private int size;  // counter to test size
	private int maxSize;
	
	/**
	 * preconditions to init ResizeableFifo
	 * @param capacity
	 */
	public void shouldHasAnPositiveCapacityNotZero(int capacity) {
		if (capacity < 0 || capacity == 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param fifo
	 */
	@SuppressWarnings("unchecked")
	public ResizeableFifo(int capacity) {
		this.shouldHasAnPositiveCapacityNotZero(capacity);
		this.fifo = (E[]) new Object[capacity];
		this.head = 0;
		this.tail = 0;
		this.size = 0;
		this.maxSize = capacity;
	}
	

	@SuppressWarnings("unchecked")
    private void grow() {
        int newSize = 2 * this.maxSize;
        E[] tmpFifo = (E[]) new Object[newSize];
        System.arraycopy(fifo, head, tmpFifo, 0, size - head);
        System.arraycopy(fifo, 0, tmpFifo, size - head, size - tail);
        this.maxSize = newSize;
        fifo = tmpFifo;
        head = 0;
        tail = size;
    }
	
	@Override
	public E poll() {
		if(this.size == 0) {
			 return null;
		}
		E eltToRemove = fifo[head];
		fifo[head] = null;
		head = (head + 1) % fifo.length;
		this.size--;
		return eltToRemove;
	}
	
	
	/**
	 * return size of fifo
	 * @return
	 */
	public int size() {
		return size;
	}
	
	/**
	 * body of method to test if fifo is empty
	 * @return
	 */
	private boolean emptyFifo() {
        return size == 0;
    }
	
	/**
	 * public method from emptyFifo
	 * @return
	 */
	public boolean isEmpty() {
		return this.emptyFifo();
	}
	
	
	
	@Override
	public String toString() {
		StringJoiner st =  new StringJoiner(", ", "[", "]");
		int i = head;
		for(int j = 0; j < this.size; j++  ) {
			st.add(fifo[i].toString());
			i =   (i +1) % fifo.length;
		}
		return st.toString();
	}
	
	
	public Iterator<E> iterator() {
		return new Iterator<E>(){
			private int iteratorCounter; // number of elements already seen
			private int index = head; // next element in the iterator to return
			
			@Override
			public boolean hasNext() {
				// test if we have next
				//return (iteratorCounter < size);
				return iteratorCounter != size;
			}

			@Override
			public E next() {
			   if(!hasNext()) {
				   throw new NoSuchElementException();
			   }
			   var tmp = fifo[index];
			   this.index =  (index +1) % fifo.length; // recalculate the index for the next
			   iteratorCounter++;
			   return tmp;
			}
		};
		
	}
	
	/**
	 * add an E element into queue
	 * @param e element to add
	 */
	@Override
	public boolean offer(E e) {
		Objects.requireNonNull(e);
		if(this.size == this.maxSize) {
			grow();
		}
		fifo[tail] = e;
		tail = (tail + 1) % fifo.length; // recalculates the position of tail, tail return FIFO (0)
		this.size++;
		return true;
	}
	
	/**
	 * retrieve first element of fifo without remove it.
	 */
	@Override
	public E peek() {
		if(this.isEmpty()) {
			return null;
		}
		return fifo[head];
	}
}
