package org.typetopaste.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JTextField;

import org.typetopaste.key.KeyUtil;
import org.typetopaste.util.ArrayUtil;

@SuppressWarnings("serial")
public class ShortcutEditor extends JTextField implements KeyListener {
	private static final int MAX_SHORTCUT_LENGTH = 4; // 3 control keys and one other key, e.g. Ctrl+Alt+Shift-X
	
	
	private int[] currentShortcut;
	private int currentShortcutLength;
	
	private boolean acceptNewShortcut = true;
	
	public ShortcutEditor() {
		super(16);
		currentShortcut = new int[MAX_SHORTCUT_LENGTH];
		currentShortcutLength = 0;
		addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (acceptNewShortcut) {
			currentShortcutLength = 0;
			Arrays.fill(this.currentShortcut, 0);
		} 
		
		if (!isDelete(code)) {
			acceptNewShortcut = false;
			
			if (ArrayUtil.indexOf(code, currentShortcut) >= 0) {
				return;
			}
			
			currentShortcut[currentShortcutLength] = code;
			currentShortcutLength++;
		}
		
		
		setShortcutText();
	}
	
	private boolean isDelete(int code) {
		return code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		acceptNewShortcut = true;
	}

	public void setShortcut(int[] shortcut) {
		Arrays.fill(this.currentShortcut, 0);
		currentShortcutLength = shortcut.length;
		System.arraycopy(shortcut, 0, currentShortcut, 0, shortcut.length);
		setShortcutText();
	}
	
	private void setShortcutText() {
		String shorctutAsString = KeyUtil.toString(Arrays.copyOf(currentShortcut, currentShortcutLength));
		setText(shorctutAsString);
	}
	
	public int[] getShortcut() {
		return currentShortcut;
	}
}
