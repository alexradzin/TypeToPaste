package org.typetopaste.util;

/**
 * Class that contains array related utility methods.
 * @author alex
 */
public class ArrayUtil {
	/**
	 * Finds given element into given {@code int} array. Returns the element index or -1 if element is not found.  
	 * @param e
	 * @param arr
	 * @return
	 */
	public static int indexOf(int e, int ... arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == e) {
				return i;
			}
		}
		return -1;
	}
}
