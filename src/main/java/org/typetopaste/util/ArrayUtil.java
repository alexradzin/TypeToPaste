package org.typetopaste.util;

public class ArrayUtil {
	public static int indexOf(int e, int ... arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == e) {
				return i;
			}
		}
		return -1;
	}
}
