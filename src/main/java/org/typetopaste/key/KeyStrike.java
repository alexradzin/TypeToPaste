package org.typetopaste.key;

import java.awt.Robot;

/**
 * Enum that defines key strike (event, action) types: {@link #PRESS}, {@link #RELEASE}, {@link #TYPE}. <br/>
 * 
 * This is very useful for command pattern implementation. 
 * @author alex
 *
 */
public enum KeyStrike {
	PRESS {
		public void strike(Robot robot, int keycode) {
			robot.keyPress(keycode);
		}
	},
	RELEASE {
		public void strike(Robot robot, int keycode) {
			robot.keyRelease(keycode);
		}
	},
	TYPE {
		public void strike(Robot robot, int keycode) {
			robot.keyPress(keycode);
			robot.keyRelease(keycode);
		}
	},
	;
	
	public abstract void strike(Robot robot, int keycode);
}
