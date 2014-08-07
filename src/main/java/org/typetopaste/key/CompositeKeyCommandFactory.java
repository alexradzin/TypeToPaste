package org.typetopaste.key;

public class CompositeKeyCommandFactory implements KeyCommandFactory {
	private final KeyCommandFactory[] commandFactories;
	
	

	public CompositeKeyCommandFactory(EnglishKeyboard keyboard, CodeFactory codeFactory) {
		super();
		this.commandFactories = new KeyCommandFactory[] {keyboard, codeFactory}; 
	}


	@Override
	public Iterable<KeyCommand> createCommands(char c) {
		for (KeyCommandFactory commandFactory : commandFactories) {
			Iterable<KeyCommand> commands = commandFactory.createCommands(c);
			if (commands != null) {
				return commands;
			}
		}
		
		//TODO: may be exception is better here?
		return null;
	}

}
