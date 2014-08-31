package org.typetopaste.util;

/**
 * String utilities
 * @author alex
 */
public class StringUtil {
	/**
	 * Joins given {@code parts} using {@code delimiter}
	 * @param delimiter
	 * @param parts
	 * @return the joined string
	 */
	public static String join(String delimiter, String ... parts) {
		if (parts == null) {
			return null;
		}
		if (parts.length == 0) {
			return "";
		}
		
		StringBuilder buff = new StringBuilder(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			buff.append(delimiter).append(parts[i]);
		}
		
		return buff.toString();
	}

}
