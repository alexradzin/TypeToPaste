package org.typetopaste.typist;

import java.awt.Robot;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.typetopaste.key.CodeFactory;
import org.typetopaste.key.CompositeKeyCommandFactory;
import org.typetopaste.key.EnglishKeyboard;
import org.typetopaste.key.KeyCommand;
import org.typetopaste.key.KeyCommandFactory;
import org.typetopaste.key.SwitchKeyCommandFactory;

/**
 * Factory that creates and initiates instance of string {@link Typist}. 
 * @author alex
 */
public class TypistFactory {
	private static final int[] EMPTY_INT_ARRAY = new int[0];
	
	
	private static Comparator<Integer> intComparator = new Comparator<Integer>() {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1 - i2;
		}
	};
	
	private static final Comparator<int[]> intArrayComparator = new ArrayComparator<int[]>(intComparator);
	private static final Comparator<int[][]> int2dArrayComparator = new ArrayComparator<int[][]>(intArrayComparator);
	

	public Typist<String> createTypist(Robot robot, int[][][] allKeyCodeKeys, Callable<int[][]> switchGetter) {
		Map<int[][], KeyCommandFactory> commandFactories = new TreeMap<int[][], KeyCommandFactory>(int2dArrayComparator);
		
		for (int[][] codeKeys : allKeyCodeKeys) {
			int[] holdKeys = codeKeys[0];
			int[] typeKeys = codeKeys.length > 1 ? codeKeys[1] : EMPTY_INT_ARRAY;
			commandFactories.put(codeKeys, new CodeFactory(holdKeys, typeKeys));
		}
		
		SwitchKeyCommandFactory<int[][]> switchKeyFactory = new SwitchKeyCommandFactory<int[][]>(commandFactories, switchGetter);
		
		// maps character to key code
		KeyCommandFactory keyCommandFactory = new CompositeKeyCommandFactory(new EnglishKeyboard(), switchKeyFactory);
		
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
