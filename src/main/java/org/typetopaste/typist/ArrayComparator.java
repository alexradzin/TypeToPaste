package org.typetopaste.typist;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * Generic array comparator that can work either with arrays of objects or with arrays of primitives.
 * @author alex
 *
 * @param <A>
 */
public class ArrayComparator<A> implements Comparator<A> {
	private final Comparator<?> elementsComparator;

	public ArrayComparator(Comparator<?> elementsComparator) {
		this.elementsComparator = elementsComparator;
	}

	
	@Override
	public int compare(A a1, A a2) {
		if (a1 == null) {
			return a2 == null ? 0 : -1;
		}
		if (a2 == null) {
			return a1 == null ? 0 : 1;
		}
		
		
		int length1 = Array.getLength(a1);
		int length2 = Array.getLength(a1);
		
		if (length1 != length2) {
			return length1 - length2;
		}
		
		for (int i = 0; i < length1; i++) {
			int diff = compare(a1, a2, i);
			if (diff != 0) {
				return diff;
			}
		}
		
		return 0;
	}
	

	/**
	 * Compares elements of 2 arrays. Throws exception if element comparator is of wrong type.
	 * Localizes scope of suppressed warnings that are the price of generic solution that supports primitives. 
	 * @param a1
	 * @param a2
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int compare(A a1, A a2, int i) {
		return ((Comparator<Object>)elementsComparator).compare(Array.get(a1, i), Array.get(a2, i));
	}

}
