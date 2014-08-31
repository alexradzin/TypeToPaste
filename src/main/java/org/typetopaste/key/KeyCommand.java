package org.typetopaste.key;

import java.awt.Robot;

/**
 * {@link KeyCommand} holds key code and key action type ({@link KeyStrike}) and can perform that key action using {@link Robot}.  
 * @author alex
 */
public class KeyCommand {
	private final KeyStrike strike;
	private final int keycode;
	
	
	public KeyCommand(KeyStrike strike, int keycode) {
		super();
		this.strike = strike;
		this.keycode = keycode;
	}
	
	public void perform(Robot robot) {
		strike.strike(robot, keycode);
	}
	
	public String toString() {
		return "KeyCommand@" + strike.name() + "#" + keycode;
	}
}
