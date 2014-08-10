package org.typetopaste.key;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



import static org.typetopaste.key.CharKeyMappingUtil.initRange;
import static org.typetopaste.key.CharKeyMappingUtil.typeCommand;


public class CodeFactory implements KeyCommandFactory {
	private final KeyCommand[] before;
	private final KeyCommand[] after;
	
	private static final Map<Character, Integer> numPadKeyCommands = new HashMap<>();
	
	static {
		initKeyPad();
	}
	
	private static void initKeyPad() {
		initRange('0', '9', KeyEvent.VK_NUMPAD0, numPadKeyCommands);
	}
	
	
	
	public CodeFactory(int[] holdKeys, int[] typeKeys) {
		super();
		KeyCommand[] holdKeyCommands = sequence(KeyStrike.PRESS, holdKeys);
		KeyCommand[] typeKeyCommands = sequence(KeyStrike.TYPE, typeKeys);
		
		before = new KeyCommand[holdKeyCommands.length + typeKeyCommands.length];
		System.arraycopy(holdKeyCommands, 0, before, 0, holdKeyCommands.length);
		System.arraycopy(typeKeyCommands, 0, before, holdKeyCommands.length, typeKeyCommands.length);
		
		after = sequence(KeyStrike.RELEASE, holdKeys);
	}
	
	private static KeyCommand[] sequence(KeyStrike strike, int[] codeKeys) {
		KeyCommand[] commands = new KeyCommand[codeKeys.length]; 
		for (int i = 0; i < codeKeys.length; i++) {
			commands[i] = new KeyCommand(strike, codeKeys[i]);
		}
		return commands;
	}

	@Override
	public Iterable<KeyCommand> createCommands(char c) {
		char[] digitChars = Integer.toString((int)c).toCharArray();
		KeyCommand[] commands = new KeyCommand[before.length + digitChars.length + after.length];
		System.arraycopy(before, 0, commands, 0, before.length);
		commands[before.length] = new KeyCommand(KeyStrike.TYPE, c);
		for (int d = 0; d < digitChars.length; d++) {
			char digitChar = digitChars[d];
			int code = numPadKeyCommands.get(digitChar);
			commands[before.length + d] = typeCommand(code);
		}
		System.arraycopy(after, 0, commands, before.length + digitChars.length, after.length);
		return Arrays.asList(commands);
	}
}
