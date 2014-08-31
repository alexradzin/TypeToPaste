package org.typetopaste.ui;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.Container;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Dialog that allows user to configure the shortcut assigned to {@code TypeToPaste}. 
 * @author alex
 */
@SuppressWarnings("serial")
public class ConfigDialog extends JFrame implements KeyEventDispatcher {
	private static final int[] DEFAULT_SHORTCUT = new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_C}; 
	private final Installer installer;
	private final ShortcutEditor shortcutEditor;
	
	public ConfigDialog(final Installer installer) {
		super("TypeToText Configuraion");
	
		this.installer = installer;
		
		JLabel label = new JLabel("Shortcut key:");
		shortcutEditor = new ShortcutEditor();

		shortcutEditor.setShortcut(DEFAULT_SHORTCUT);
		
		
		JButton create = new JButton("Create");
		create.setMnemonic('c');
		JButton cancel = new JButton("Cancel");
		cancel.setMnemonic('a');
		
		Container pane = getContentPane();
		GroupLayout layout = new GroupLayout(getContentPane());
		pane.setLayout(layout);
		


		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addComponent(label)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(shortcutEditor))
				.addGroup(
						layout.createParallelGroup(LEADING)
								.addComponent(create)
								.addComponent(cancel)));

		layout.linkSize(SwingConstants.HORIZONTAL, create, cancel);

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(BASELINE)
								.addComponent(label).addComponent(shortcutEditor)
								.addComponent(create))
				.addGroup(
						layout.createParallelGroup(LEADING)
								.addComponent(cancel)));

		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
		});

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				create();
			}
		});
		
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);		
		
		
		
		// iconURL is null when not found
		setIconImage(UIUtil.getTypeToPasteIcon().getImage());		
		setTitle("Create TypeToPaste Shortcut");
		pack();
	}

	private void create() {
		installer.createShortcut(shortcutEditor.getShortcut());
		setVisible(false);
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (KeyEvent.VK_ENTER == e.getKeyCode()) {
			create();
			return true;
		}
		return false;
	}
}
