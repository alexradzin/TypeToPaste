package org.typetopaste.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.typetopaste.install.ShortcutCreator;
import org.typetopaste.install.WindowsShortcutCreator;

public class Installer {
	private ShortcutCreator shortcutCreator;
	
	public void install() throws IOException {
		shortcutCreator = shortcutCreator();
		if (shortcutCreator == null) {
			showUnsupportedMessage();
			return;
		}
		
		showShortcutDialog();
	}
	
	
	private void showUnsupportedMessage() {
		final String message = "We cannot create TypeToPaste shortcut automatically on your platform";
		final String title = "Unsupported operation";
		
		try {
			String cmd = String.format("%s -jar %s", getJvmPath(), getJarPath());
			JOptionPane.showInputDialog(
	                null,
				    message + "\n" + "Please map your favorite shortcut to the following command line",
				    title,
				    JOptionPane.NO_OPTION,
				    UIUtil.getTypeToPasteIcon(), 
				    null,  
	                cmd);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
				    message,
				    title,
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showShortcutDialog() {
		JFrame configuDialog = new ConfigDialog(this);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		configuDialog.setLocation(dim.width/2-configuDialog.getSize().width/2, dim.height/2-configuDialog.getSize().height/2);		
		configuDialog.setVisible(true);
	}

	public void createShortcut(int[] shortcut) {
		try {
			String jar = getJarPath();
			String[] args = {"-jar", jar}; 
			shortcutCreator.createShortcut(getJvmPath(), args, shortcut);
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
				    "Automatic shortcut creation failed",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	private String getJvmPath() throws IOException {
		return String.format("%s/bin/java", System.getProperty("java.home"));
	}
	
	
	private String getJarPath() throws IOException {
		URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		return new File(location.getFile()).getCanonicalPath();
	}
	
	private ShortcutCreator shortcutCreator() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows") ? new WindowsShortcutCreator() : null;
	}
	
	
	public static void main(String[] args) throws IOException {
		new Installer().install();
	}

}
