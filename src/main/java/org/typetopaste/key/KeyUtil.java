package org.typetopaste.key;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities that help to process key codes.  
 * @author alex
 */
public abstract class KeyUtil {
	private static final char separator = '+';
	private static final Pattern split = Pattern.compile("[+]");
	private static final Map<Integer, String> codeToName = new HashMap<>();
	private static final Map<String, Integer> nameToCode = new HashMap<>();

	private static final String CTRL = "CTRL";
	private static final String CONTROL = "CONTROL";
	
	private static final Map<String, String> internalToExternalName = new HashMap<>();
	private static final Map<String, String> externalToInternalName = new HashMap<>();
	
	
	private static final String KEY_PREFIX = "VK_";
	private static final int KEY_PREFIX_LENGTH = KEY_PREFIX.length();
	
	static {
		try {
			initCodes();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		
		initAliases();
	}
	
	/**
	 * Translates string into sequence of key codes. For example translates {@code Ctrl+C} to corresponding key codes.   
	 * @param str 
	 * @return array of codes of characters that compose the given string
	 * @see #fromStrings(String[])
	 * @see #toString(int[])
	 */
	public static int[] fromString(String str) {
		return fromStrings(split.split(str));
	}

	/**
	 * Creates given key names into corresponding key codes. For example translates array {@code ["Ctrl", "C"]} into key codes. 
	 * @param names
	 * @return key codes
	 * @see #fromString(String)
	 */
	public static int[] fromStrings(String[] names) {
		int[] codes = new int[names.length];
		for (int i = 0; i < names.length; i++) {
			String externalName = names[i].toUpperCase();
			String internalName = externalToInternalName.get(externalName);
			if (internalName == null) {
				internalName = externalName;
			}
			
			Integer code = nameToCode.get(internalName);
			if (code == null) {
				return null;
			}
					
			codes[i] = code;
		}
		return codes; 
	}

	/**
	 * Translates array of key codes into corresponding string representation. 
	 * @param codes
	 * @return string representation of given key codes
	 */
	public static String toString(int[] codes) {
		if (codes.length == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(toString(codes[0]));
		for (int i = 1; i < codes.length; i++) {
			String s = toString(codes[i]);
			if (s != null) {
				buffer.append(separator).append(s);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Creates string representation of separate character (e.g. {@code Ctrl}, {@code Alt} etc).  
	 * @param code - code of the character
	 * @return string representation. 
	 */
	public static String toString(int code) {
		String internalName = codeToName.get(code);
		if (internalName == null) {
			return null;
		}
		String externalName = internalToExternalName.get(internalName);
		if (externalName == null) {
			externalName = internalName;
		}
		return capitalize(externalName);
	}
	
	private static String capitalize(String str) {
		String prefix = str.substring(0, 1).toUpperCase();
		String suffix = str.length() > 1 ? str.substring(1).toLowerCase() : "";
		return prefix + suffix;
	}
	
	private static void initCodes() throws IllegalAccessException {
		for (Field field : KeyEvent.class.getFields()) {
			if (field.getName().startsWith(KEY_PREFIX) && Modifier.isStatic(field.getModifiers())) {
				String fieldName = field.getName();
				String keyName = fieldName.substring(KEY_PREFIX_LENGTH);
				int keyCode = field.getInt(null);
				if (keyCode != KeyEvent.VK_UNDEFINED) { // undefined is 0. We prefer map 0 to null (see toString(int))
					codeToName.put(keyCode, keyName);
					nameToCode.put(keyName, keyCode);
				}
			}
		}
	}
	
	private static void initAliases() {
		internalToExternalName.put(CONTROL, CTRL);
		externalToInternalName.put(CTRL, CONTROL);
	}
	
}
