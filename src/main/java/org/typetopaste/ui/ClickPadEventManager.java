package org.typetopaste.ui;

import java.awt.FontMetrics;
import java.awt.KeyEventDispatcher;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.typetopaste.typist.Typist;
import org.typetopaste.typist.TypistFactory;

/**
 * This is a "model" of {@code ClickPad} in terms of MVC pattern where {@code ClickPad} is a view.
 * 
 * This class implements logic of handling events captured by {@code ClickPad}.
 * 
 * @see ClickPad
 * @author alex
 */
public class ClickPadEventManager implements MouseListener, MouseMotionListener, KeyListener, KeyEventDispatcher {
	private final ClickPad pad;
	private final Robot robot;
	private final Typist<String> typist;
	
	private volatile boolean mouseOut = false;
	private Object positionerLock = new Object();
	
	private Collection<Integer> copyKeyCommandSequence = new ArrayList<>(); 
	
	
	public ClickPadEventManager(ClickPad pad, Robot robot) {
		super();
		this.pad = pad;
		this.robot = robot;
		
		Callable<int[][]> switchGetter = new Callable<int[][]>() {
			@Override
			public int[][] call() throws Exception {
				return ClickPadEventManager.this.pad.getCodeKeys();
			}
		}; 
	
		Callable<Long> delayGetter = new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return (long)ClickPadEventManager.this.pad.getDelay();
			}
			
		};
			
		this.typist = new TypistFactory().createTypist(robot, pad.getAllCodeKeys(), switchGetter, delayGetter);
		new PositionFixer().start();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		switch(e.getID()) {
			case KeyEvent.KEY_PRESSED:
				keyPressed(e);
				break;
			case KeyEvent.KEY_RELEASED:
				keyReleased(e);
				break;
			case KeyEvent.KEY_TYPED:
				keyTyped(e);
				break;
		}
		
		return false;
	}	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (processSpecialKeysPressed(e)) {
			return;
		}
		move(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//TODO: keyReleased with Ctrl-v should start typing only when Ctrl is released. Otherwise if we react on releasing of V and ctrl is still pressed we start typing for example ctrl-s instead of s
		close(processSpecialKeysReleased(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pad.positionPad();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pad.positionPad();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		paste();
		close(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// nothing to do here. mouseClicked does the job
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing to do here. mouseClicked does the job
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		positionPad(false);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		positionPad(true);
	}
	
	private void positionPad(boolean setPosition) {
		mouseOut = setPosition;
		if (mouseOut) {
			synchronized(positionerLock) {
				positionerLock.notify();
			}
		} else {
			pad.positionPad();
		}
	}

	
	private boolean move(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				relativeMouseMove(-1, 0);
				return true;
			case KeyEvent.VK_UP:
				relativeMouseMove(0, -1);
				return true;
			case KeyEvent.VK_RIGHT:
				relativeMouseMove(1, 0);
				return true;
			case KeyEvent.VK_DOWN:
				relativeMouseMove(0, 1);
				return true;
			default: return false;
		}
	}

	private void relativeMouseMove(int relativeX, int relativeY) {
		FontMetrics fontMetrics = pad.getFontMetrics(pad.getFont());
		int height = fontMetrics.getHeight();
		int width = fontMetrics.stringWidth("a");
		
		Point currentLocation = MouseInfo.getPointerInfo().getLocation();
		int x = currentLocation.x + relativeX * width;
		int y = currentLocation.y + relativeY * height;
		robot.mouseMove(x, y);
	}

	private boolean processSpecialKeysPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(KeyEvent.VK_V == code  && (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
		    // ctrl-v
			copyRequest(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
			return true;
		}
		
		if(KeyEvent.VK_INSERT == code  && (e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
		    // shift-insert
			copyRequest(KeyEvent.VK_SHIFT, KeyEvent.VK_INSERT);
			return true;
		}

		
		if(KeyEvent.VK_UP == code  && (e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) {
			configureDelay(+1);
			return true;
		}
		if(KeyEvent.VK_DOWN == code  && (e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) {
			configureDelay(-1);
			return true;
		}
		
		
		return false;
	}
	

	private boolean processSpecialKeysReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		synchronized(copyKeyCommandSequence) {
			if (copyKeyCommandSequence.remove(code)) {
				if (copyKeyCommandSequence.isEmpty()) {
					paste();
					return true;
				}
			} else {
				copyKeyCommandSequence.clear();
			}
	
			return KeyEvent.VK_ESCAPE == code;
		}
	}
	
	
	private void copyRequest(int ... keys) {
		synchronized(copyKeyCommandSequence) {
			copyKeyCommandSequence.clear();
			for (int key : keys) {
				copyKeyCommandSequence.add(key);
			}
		}
	}
	
	private void configureDelay(int increment) {
		pad.configureDelay(increment);
	}

	private void paste() {
		String clipboardText = getFromClipboard();
		float currentOpacity = pad.getOpacity();
    	pad.setOpacity(0f);
    	
    	robot.mousePress(InputEvent.BUTTON1_MASK);
    	delay();
    	robot.mouseRelease(InputEvent.BUTTON1_MASK);
		typist.type(clipboardText);
    	
    	pad.setOpacity(currentOpacity);
	}

	//TODO: improve error handling
	private String getFromClipboard() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} 
	}

	private void delay() {
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void close(boolean close) {
		if (close) {
			pad.close();
		}
	}
	
	/**
	 * When mouse is moving fast it can "escape" the {@code ClickPad}. 
	 * To avoid this we "fix" the position of {@code ClickPad} when mouse is out asynchronously.     
	 * author alex
	 */
	private class PositionFixer extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized(positionerLock) {
					try {
						positionerLock.wait();
					} catch (InterruptedException e) {
						// nothing to do here
					}
					// It is not enough to fix position only once. 
					// If mouse continues moving out while fixing the position 
					// it can escape the area while ClickPad is being positioned.
					// Looping fixes this problem.
					while (mouseOut) {
						pad.positionPad();
					}
				}
			}
		}
	}

}
