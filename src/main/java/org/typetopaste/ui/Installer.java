package org.typetopaste.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.typetopaste.install.ShortcutCreator;
import org.typetopaste.install.WindowsShortcutCreator;
import org.typetopaste.util.IOUtil;

/**
 * {@code Installer} implements logic of installation of {@code TypeToPaste}. 
 * 
 * In fact it downloads that jar file and creates custom shortcut that can be used to launch {@code TypeToPaste} 
 * if current platform is supported.  
 * @author alex
 */
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
			e.printStackTrace();
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
			e.printStackTrace();
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
		File locationFile = new File(location.getPath());
		String protocol = location.getProtocol();
		
		if (protocol.toLowerCase().startsWith("http")) {
			// If we are running using JNLP the jar file is too small to trigger java web start to cache it. 
			// So we download the file again and store it in current directory in order to configure shortcut. 
			//TODO: although it is fine for Windows using current directory on other platforms may cause problems. Test it!
			// If current directory is not writable we try to use temporary directory as a work around. 
			File cacheDir = new File(".").getCanonicalFile();
			if (!cacheDir.canWrite()) {
				cacheDir = new File(System.getProperty("java.io.tmpdir")); 
			}
			File cachedJar = new File(cacheDir, locationFile.getName());
			FileOutputStream faos = new FileOutputStream(cachedJar);
			
			IOUtil.copy(location.openStream(), faos);
			faos.flush();
			faos.close();
			locationFile = cachedJar; 
		}
		
		return locationFile.getAbsolutePath();
	}
	
	private ShortcutCreator shortcutCreator() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows") ? new WindowsShortcutCreator() : null;
	}
	
	
	public static void main(String[] args) throws IOException {
		new Installer().install();
	}

}
