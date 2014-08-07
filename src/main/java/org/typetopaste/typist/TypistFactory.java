package org.typetopaste.typist;

import java.awt.Robot;

import org.typetopaste.key.CodeFactory;
import org.typetopaste.key.CompositeKeyCommandFactory;
import org.typetopaste.key.EnglishKeyboard;
import org.typetopaste.key.KeyCommand;
import org.typetopaste.key.KeyCommandFactory;

public class TypistFactory {
	public Typist<String> createTypist(Robot robot, int[] codeKeys) {
		// maps character to key code
		KeyCommandFactory keyCommandFactory = new CompositeKeyCommandFactory(new EnglishKeyboard(), new CodeFactory(codeKeys));
		
		// emulates individual key event
		Typist<KeyCommand> robotTypist = new RobotTypist(robot);
		// emulates sequence of key events
		Typist<Iterable<KeyCommand>> keyCommandsTypist = new SequenceTypist<KeyCommand>(robotTypist);
		
		// types individual character
		Typist<Character> charTypist = new CharTypist(keyCommandsTypist, keyCommandFactory);
		
		// types sequence of characters
		SequenceTypist<Character> charsequenceTypist = new SequenceTypist<Character>(charTypist);
		
		// types string
		Typist<String> stringTypist = new StringTypist(charsequenceTypist);
		
		return stringTypist;
	}
}
