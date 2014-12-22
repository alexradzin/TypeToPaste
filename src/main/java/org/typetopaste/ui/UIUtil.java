package org.typetopaste.ui;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Utility class for various UI related utility methods. 
 * @author alex
 */
class UIUtil {
	private static final ImageIcon keyboardIcon = new ImageIcon(UIUtil.class.getResource("/keyboard.png"));
	
	static ImageIcon getTypeToPasteIcon() {
		return keyboardIcon;
	}


	static void showErrorMessage(String message) {
		showErrorMessage(message, "Error");
	}

	static void showErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
