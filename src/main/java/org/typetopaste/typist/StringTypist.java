package org.typetopaste.typist;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of {@link Typist} that types strings.<p/>
 * 
 * Transforms string into sequence of characters and types it using {@link SequenceTypist}.  
 * @author alex
 */
public class StringTypist implements Typist<String> {
	private final SequenceTypist<Character> charTypist;

	public StringTypist(SequenceTypist<Character> charTypist) {
		super();
		this.charTypist = charTypist;
	}

	@Override
	public void type(String str) {
		charTypist.type(chars(str));
	}
	
	private Iterable<Character> chars(String str) {
		Collection<Character> chars = new ArrayList<>(str.length());
		for (char c : str.toCharArray()) {
			chars.add(c);
		}
		return chars;
	}

}
