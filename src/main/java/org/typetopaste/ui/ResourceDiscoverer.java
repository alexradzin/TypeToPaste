package org.typetopaste.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.typetopaste.util.IOUtil;

public class ResourceDiscoverer {
	private File jarFile;
	
	
	public synchronized File getJarFile() throws IOException {
		if (jarFile == null) {
			jarFile = discoverJarFile();
		}
		return jarFile;
	}
	
	
	public File getHelpFile() throws IOException {
		File cp = getJarFile();
		File base = cp.isDirectory() ? cp : cp.getParentFile();
		return new File(base, "help.html");
	}
	
	private File discoverJarFile() throws IOException {
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
		
		return locationFile;
	}

}
