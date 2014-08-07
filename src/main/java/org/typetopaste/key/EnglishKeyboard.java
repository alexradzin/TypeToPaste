package org.typetopaste.key;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import static org.typetopaste.key.CharKeyMappingUtil.init;
import static org.typetopaste.key.CharKeyMappingUtil.initRange;
import static org.typetopaste.key.CharKeyMappingUtil.typeCommand;

public class EnglishKeyboard implements KeyCommandFactory {
	private static final KeyCommand SHIFT_PRESS = new KeyCommand(KeyStrike.PRESS, KeyEvent.VK_SHIFT);
	private static final KeyCommand SHIFT_RELEASE = new KeyCommand(KeyStrike.RELEASE, KeyEvent.VK_SHIFT);
	private static final Map<Character, Integer> baseKeys = new HashMap<>();
	private static final Map<Character, Integer> shiftedKeys = new HashMap<>();
	
	static {
		initBasicCharacters();
	}

	
	private static void initBasicCharacters() {
		initRange('a', 'z', KeyEvent.VK_A, baseKeys);
		initRange('A', 'Z', KeyEvent.VK_A, shiftedKeys);
		initRange('0', '9', KeyEvent.VK_0, baseKeys);

		init('\n', KeyEvent.VK_ENTER, baseKeys);
		init('\t', KeyEvent.VK_TAB, baseKeys);

		init(',', KeyEvent.VK_COMMA, baseKeys);
		init('-', KeyEvent.VK_MINUS, baseKeys);
		init('.', KeyEvent.VK_PERIOD, baseKeys);
		init('/', KeyEvent.VK_SLASH, baseKeys);
		init(';', KeyEvent.VK_SEMICOLON, baseKeys);
		init('\'', KeyEvent.VK_AMPERSAND, baseKeys);
		init('[', KeyEvent.VK_OPEN_BRACKET, baseKeys);
		init('[', KeyEvent.VK_CLOSE_BRACKET, baseKeys);
		init('`', KeyEvent.VK_BACK_QUOTE, baseKeys);
		init('=', KeyEvent.VK_EQUALS, baseKeys);
		
		init('<', KeyEvent.VK_COMMA, shiftedKeys);
		init('_', KeyEvent.VK_MINUS, shiftedKeys);
		init('>', KeyEvent.VK_PERIOD, shiftedKeys);
		init('?', KeyEvent.VK_SLASH, shiftedKeys);
		init(':', KeyEvent.VK_SEMICOLON, shiftedKeys);
		init('|', KeyEvent.VK_AMPERSAND, shiftedKeys);
		init('{', KeyEvent.VK_OPEN_BRACKET, shiftedKeys);
		init('}', KeyEvent.VK_CLOSE_BRACKET, shiftedKeys);
		init('~', KeyEvent.VK_BACK_QUOTE, shiftedKeys);
		init('+', KeyEvent.VK_EQUALS, shiftedKeys);
		
		
		initRange(")!@#$%^&*(".toCharArray(), KeyEvent.VK_0, shiftedKeys);
	}

	
	
	
	@Override
	public Iterable<KeyCommand> createCommands(char c) {
		Integer code = baseKeys.get(c);
		if (code != null) {
			return Collections.singleton(typeCommand(code));
		}

		code = shiftedKeys.get(c);
		if (code != null) {
			return Arrays.asList(SHIFT_PRESS, typeCommand(code), SHIFT_RELEASE);
		}
		
		return null;
	}
	
}
