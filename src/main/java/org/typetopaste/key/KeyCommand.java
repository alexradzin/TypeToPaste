package org.typetopaste.key;

import java.awt.Robot;

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
