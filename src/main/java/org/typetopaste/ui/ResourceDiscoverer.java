package org.typetopaste.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.typetopaste.util.IOUtil;

import com.github.rjeschke.txtmark.Processor;

public class ResourceDiscoverer {
	private static final String UTF8 = "UTF8";
	private static final String HTML_START = "<html><head><meta charset=\"UTF-8\"></head>";
	private static final String HTML_END = "</html>";
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
	
	
	public void extractHelp() {
		try (
				InputStream md = getClass().getResourceAsStream("/help.md"); 
				InputStream img = getClass().getResourceAsStream("/clickpadhelp.png");
				Writer w = new OutputStreamWriter(new FileOutputStream(getHelpFile()), UTF8);
				OutputStream faos = new FileOutputStream(getHelpFile())) {
			
			w.write(HTML_START);
			w.write(Processor.process(md, UTF8));
			w.write(HTML_END);
		} catch (IOException e) {
			UIUtil.showErrorMessage("Cannot extract help file");
		}
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
