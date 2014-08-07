 package org.typetopaste.typist;

import java.awt.Robot;

import org.typetopaste.key.KeyCommand;

public class RobotTypist implements Typist<KeyCommand> {
	protected final Robot robot;

	public RobotTypist(Robot robot) {
		super();
		this.robot = robot;
	}

	@Override
	public void type(KeyCommand c) {
		c.perform(robot);
	}
}
