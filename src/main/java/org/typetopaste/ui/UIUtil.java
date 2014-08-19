package org.typetopaste.ui;

import javax.swing.ImageIcon;

public class UIUtil {
	private static final ImageIcon keyboardIcon = new ImageIcon(UIUtil.class.getResource("/keyboard.png"));
	
	public static ImageIcon getTypeToPasteIcon() {
		return keyboardIcon;
	}
}
