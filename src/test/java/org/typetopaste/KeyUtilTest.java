package org.typetopaste;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Z;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.typetopaste.key.KeyUtil;
// import java.awt.event.KeyEvent;
import org.junit.Test;


public class KeyUtilTest {
	@Test
	public void fromStringA() {
		fromString("A", new int[] {VK_A});
	}
	
	@Test
	public void fromStringCtrl() {
		fromString("Ctrl+A", new int[] {VK_CONTROL, VK_A});
	}
	
	@Test
	public void fromStringCtrlAltT() {
		fromString("Ctrl+Alt+T", new int[] {VK_CONTROL, VK_ALT, VK_T});
	}
	
	@Test
	public void toStringX() {
		toString(new int[] {VK_X}, "X");
	}

	@Test
	public void toStringCtrlZ() {
		toString(new int[] {VK_CONTROL, VK_Z}, "Ctrl+Z");
	}
	
	private void fromString(String str, int[] expectedCodes) {
		int[] actualCodes = KeyUtil.fromString(str);
		assertArrayEquals(expectedCodes, actualCodes);
	}
	
	private void toString(int[] codes, String expected) {
		String actual = KeyUtil.toString(codes);
		assertEquals(expected, actual);
	}
}
