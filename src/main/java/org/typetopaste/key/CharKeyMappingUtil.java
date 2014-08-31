package org.typetopaste.key;

import java.util.Map;

/**
 * Utilities that help to create ranges of characters. 
 * @author alex
 */
abstract class CharKeyMappingUtil {
	private CharKeyMappingUtil() {
		// empty private constructor to avoid instantiation of this class because it is pure utility. 
	}

	/**
	 * Fills given {@code map} with codes of characters from {@code codes} starting from {@link fromKey}
	 * @param chars
	 * @param fromKey
	 * @param map
	 */
	static void initRange(char[] chars, int fromKey, Map<Character, Integer> map) {
		int code = fromKey;
		for (int i = 0; i < chars.length; i++, code++) {
			init(chars[i], code, map);
		}
	}
	
	
	
	static void initRange(char from, char to, int fromKey, Map<Character, Integer> map) {
		int code = fromKey;
		for (char c = from; c <= to; c++, code++) {
			init(c, code, map);
		}
	}

	static void init(char c, int code, Map<Character, Integer> map) {
		map.put(c, code);
	}

	
	static KeyCommand typeCommand(int c) {
		return new KeyCommand(KeyStrike.TYPE, c);
	}
	
}


