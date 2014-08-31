package org.typetopaste.key;

/**
 * Interface that defines contract for creating of instances of {@link KeyCommand}. 
 * @author alex
 *
 */
public interface KeyCommandFactory {
	Iterable<KeyCommand> createCommands(char c);
}
