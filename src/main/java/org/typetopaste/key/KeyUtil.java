package org.typetopaste.key;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
	
	
	public static int[] fromString(String str) {
		return fromStrings(split.split(str));
	}
	
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
