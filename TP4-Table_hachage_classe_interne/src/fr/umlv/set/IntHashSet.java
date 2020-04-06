package fr.umlv.set;

import java.util.Collection;
import java.util.Objects;
import java.util.function.IntConsumer;

public class IntHashSet {
	private static final int SIZE_INIT = 8;
	private Entry[] table;
	private int counter;
	
	// intern class
	static class Entry {
		final int value;
		final Entry next;
		
		public Entry(int value, Entry next) {
			this.value = value;
			this.next = next;
		}
	}
	
	public IntHashSet() {
		this.table = new Entry[SIZE_INIT];
		this.counter = 0;
	}
	
	/**
	 * compute hashcode
	 * @return int hashcode of value parameter
	 */
	public int hash (int value) {
		return value &(table.length-1);
	}
	
	/**
	 * add value parameter into InthashSet
	 * @param index value to add
	 */
	public void add(int valparam) {
		Objects.requireNonNull(valparam);
		var position = hash(valparam);
		var tmp = this.table[position];
		while(tmp != null) {
			if(tmp.value == valparam) {
				return;
			}
			tmp = tmp.next;
		}
		table[position] = new Entry(valparam, table[position]);
		counter++;
		
		/*
		if(this.table[index] == null) {
			table[index] = new Entry(value, null);
			counter++;
		}
		else {
			var temp  =  table[index];
			table[index] = new Entry(value, temp);
		}
		*/
	}
	
	/**
	 * returns the number of integers contained in the set.
	 * @return int size of hashSet list
	 */
	public int size () {
		return counter;
	}
	
	/**
	 * takes a  lambda parameter, it will be called for each whole present in all
	 * @param consumer
	 */
	public void forEach(IntConsumer consumer) {
		Objects.requireNonNull(consumer);
		for(var elt: table) {
			for(var entry = elt; entry != null; entry = entry.next) {
				consumer.accept(entry.value);
			}
		}
	}
	
	/**
	 * returns true if the whole is all, false otherwise
	 * @param value
	 * @return boolean result
	 */
	public boolean contains(int value) {
		Objects.requireNonNull(value);
		var position= hash(value);
		for(var tmp = table[position]; tmp != null; tmp = tmp.next) {
			if(tmp.value == value) {
				return true;
			}
	
		}
		return false;
	}
	
}
