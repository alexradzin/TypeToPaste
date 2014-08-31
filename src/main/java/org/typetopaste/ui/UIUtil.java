package org.typetopaste.ui;

import javax.swing.ImageIcon;

/**
 * Utility class for various UI related utility methods. 
 * @author alex
 */
public class UIUtil {
	private static final ImageIcon keyboardIcon = new ImageIcon(UIUtil.class.getResource("/keyboard.png"));
	
	public static ImageIcon getTypeToPasteIcon() {
		return keyboardIcon;
	}
}
