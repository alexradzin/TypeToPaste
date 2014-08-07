package org.typetopaste.key;

public interface KeyCommandFactory {
	Iterable<KeyCommand> createCommands(char c);
}
