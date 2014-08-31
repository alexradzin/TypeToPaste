package org.typetopaste.key;

/**
 * Implementation of {@link KeyCommandFactory} that holds several other {@code KeyCommandFactories} 
 * and tries them one-by-one until the first returns not-null result.  
 * @author alex
 */
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
