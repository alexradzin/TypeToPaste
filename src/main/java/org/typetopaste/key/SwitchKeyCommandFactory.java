package org.typetopaste.key;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Implementation of {@link KeyCommandFactory} that holds a map of other {@code KeyCommandFactories} and forwards calls to one of them
 * according to current value of switch key retrieved from given {@code switchGetter}. 
 *  
 * @author alex
 *
 * @param <K>
 */
public class SwitchKeyCommandFactory<K> implements KeyCommandFactory {
	private final Map<K, KeyCommandFactory> commandFactories;
	private final Callable<K> switchGetter;
	
	
	public SwitchKeyCommandFactory(Map<K, KeyCommandFactory> commandFactories, Callable<K> switchGetter) {
		this.commandFactories = commandFactories;
		this.switchGetter = switchGetter;
	}

	
	@Override
	public Iterable<KeyCommand> createCommands(char c) {
		try {
			KeyCommandFactory commandFactory = commandFactories.get(switchGetter.call());
			if (commandFactory == null) {
				return null;
			}
			return commandFactory.createCommands(c);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
