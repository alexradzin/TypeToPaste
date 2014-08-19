package org.typetopaste.install;

import java.io.IOException;

public interface ShortcutCreator {
	void createShortcut(String jvmPath, String[] args, int[] shortcut) throws IOException;
}
