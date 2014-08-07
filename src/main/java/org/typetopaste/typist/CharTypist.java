package org.typetopaste.typist;

import org.typetopaste.key.KeyCommand;
import org.typetopaste.key.KeyCommandFactory;

public class CharTypist implements Typist<Character> {
	private final Typist<Iterable<KeyCommand>> typist;
	private final KeyCommandFactory factory;

	
	public CharTypist(Typist<Iterable<KeyCommand>> typist, KeyCommandFactory factory) {
		super();
		this.typist = typist;
		this.factory = factory;
	}


	@Override
	public void type(Character c) {
		typist.type(factory.createCommands(c));
	}

}
