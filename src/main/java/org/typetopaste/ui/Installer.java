package org.typetopaste.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.typetopaste.install.ShortcutCreator;
import org.typetopaste.install.WindowsShortcutCreator;

/**
 * {@code Installer} implements logic of installation of {@code TypeToPaste}. 
 * 
 * In fact it downloads that jar file and creates custom shortcut that can be used to launch {@code TypeToPaste} 
 * if current platform is supported.  
 * @author alex
 */
public class Installer {
	private ShortcutCreator shortcutCreator;
	private ResourceDiscoverer resourceDiscoverer = new ResourceDiscoverer();
	
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
			UIUtil.showErrorMessage(message, title);
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
			File cp = resourceDiscoverer.getJarFile();
			String path = cp.getAbsolutePath();
			String[] args = cp.isDirectory() ? new String[] {"-cp", path, ClickPad.class.getName()} : new String[] {"-jar", path};
			shortcutCreator.createShortcut(getJvmPath(), args, shortcut);
		} catch (IOException e) {
			UIUtil.showErrorMessage("Automatic shortcut creation failed");
		}
	}
	
	ResourceDiscoverer getResourceDiscoverer() {
		return resourceDiscoverer;
	}
	
	
	private String getJvmPath() throws IOException {
		return String.format("%s/bin/java", System.getProperty("java.home"));
	}
	
	private String getJarPath() throws IOException {
		return resourceDiscoverer.getJarFile().getAbsolutePath();
	}

	
	private ShortcutCreator shortcutCreator() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows") ? new WindowsShortcutCreator() : null;
	}
	
	
	public static void main(String[] args) throws IOException {
		new Installer().install();
	}

}
