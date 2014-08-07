package org.typetopaste.ui;

import java.awt.FontMetrics;
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

import org.typetopaste.typist.Typist;
import org.typetopaste.typist.TypistFactory;

public class ClickPadEventManager implements MouseListener, MouseMotionListener, KeyListener {
	private final ClickPad pad;
	private final Robot robot;
	private final Typist<String> typist;
	
	private volatile boolean mouseOut = false;
	private Object positionerLock = new Object();
	
	public ClickPadEventManager(ClickPad pad, Robot robot) {
		super();
		this.pad = pad;
		this.robot = robot;
		this.typist = new TypistFactory().createTypist(robot, pad.getCodeKeys());
		new PositionFixer().start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		move(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//TODO: keyReleased with Ctrl-v should start typing only when Ctrl is realased. Otherwise if we react on reasing of V and ctrl is still pressed we start typing for example ctrl-s instead of s
		close(process(e));
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

	private boolean process(KeyEvent e) {
		int code = e.getKeyCode();
		System.out.println(code + ", " + KeyEvent.VK_ESCAPE);

		if(KeyEvent.VK_V == code  && (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
		    // ctrl-v
			paste();
			return true;
		}
		if(KeyEvent.VK_INSERT == code  && e.getModifiers() == InputEvent.SHIFT_DOWN_MASK) {
		    // shift-insert
			paste();
			return true;
		}

		if(KeyEvent.VK_ESCAPE == code) {
			// just terminate
			return true;
		}
		
		return false;
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
					while (mouseOut) {
						pad.positionPad();
					}
				}
			}
			
		}
		
	}
}
