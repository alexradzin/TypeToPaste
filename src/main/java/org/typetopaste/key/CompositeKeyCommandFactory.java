package org.typetopaste.key;

public class CompositeKeyCommandFactory implements KeyCommandFactory {
	private final KeyCommandFactory[] commandFactories;
	
	

	public CompositeKeyCommandFactory(KeyCommandFactory ... factories) {
		super();
		this.commandFactories = new KeyCommandFactory[factories.length];
		System.arraycopy(factories, 0, this.commandFactories, 0, factories.length); 
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
