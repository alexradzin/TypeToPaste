package org.typetopaste.install;

import static org.typetopaste.util.StringUtil.join;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.typetopaste.key.KeyUtil;
import org.typetopaste.util.IOUtil;
import org.typetopaste.util.TemplateEngine;

/**
 * Implementation of {@link ShortcutCreator} for MS Windows.<br/>
 * 
 * Generates and runs VB script that creates desktop shortcut with predefined keyboard shortcut.
 * 
 * @author alex
 */
public class WindowsShortcutCreator implements ShortcutCreator {
	private static final String ICON_RESOURCE = "keyboard.ico";
	
	private static final String ARGS = "typetopaste.args";
	private static final String ICON = "typetopaste.icon";
	private static final String SHORTCUT = "typetopaste.shortcut";

	private static final String SCRIPT_TIMPLATE = "CreateShortcutTemplate.vbs";
	private static final String SCRIPT_NAME = "CreateTypeToPasteShortcut.vbs";
	private static final String CSCRIPT = "cscript";
	
	private TemplateEngine templateEngine = new TemplateEngine();
	
	// http://stackoverflow.com/questions/17061482/jdk-7-reports-itself-as-a-jre-via-java-home-system-property
	
	@Override
	public void createShortcut(String jvmPath, String[] args, int[] shortcut) throws IOException {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File icon = extractIcon(tmpDir);
		Properties props = new Properties(System.getProperties());
		props.setProperty(ARGS, join(" ", args));
		props.setProperty(ICON, icon.getAbsolutePath());
		props.setProperty(SHORTCUT, KeyUtil.toString(shortcut));
		
		
		String scriptTemplate = IOUtil.readAllAsString(getClass().getResourceAsStream('/' + SCRIPT_TIMPLATE));
		String scriptSrc = templateEngine.process(scriptTemplate, props);
		
		File script = new File(tmpDir, SCRIPT_NAME);
		
		try (OutputStream scriptOut = new FileOutputStream(script)) {
			IOUtil.copy(new ByteArrayInputStream(scriptSrc.getBytes()), scriptOut);
		}
		
		ProcessBuilder pb  = new ProcessBuilder();
		pb.command(CSCRIPT, script.getAbsolutePath());
		Process process = pb.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	
	private File extractIcon(File toDir) throws IOException {
		File iconFile = new File(toDir, ICON_RESOURCE);
		try (OutputStream out = new FileOutputStream(iconFile)) {
			IOUtil.copy(getClass().getResourceAsStream("/" + ICON_RESOURCE), out);
		}
		return iconFile;
	}
}
