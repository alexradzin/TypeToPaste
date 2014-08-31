package org.typetopaste.install;

import java.io.IOException;

/**
 * Interface that defines contract for creating of custom shortcut that runs JVM identified 
 * by given {@code jvmPath} and {@code arg}uments.  
 * @author alex
 *
 */
public interface ShortcutCreator {
	void createShortcut(String jvmPath, String[] args, int[] shortcut) throws IOException;
}
