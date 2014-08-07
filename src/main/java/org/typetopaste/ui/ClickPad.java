package org.typetopaste.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ClickPad extends JFrame {
	private int width;
	private int height;
	
	
	public ClickPad(int closeOperation) {
		this(150, 120, closeOperation);
	}
	
	
	public ClickPad(int width, int height, int closeOperation) {
		this.width = width;
		this.height = height;
		
		setLayout(new BorderLayout(0, 0));
		setAlwaysOnTop(true);
		JLabel icon = new JLabel(new ImageIcon(getClass().getResource("/keyboard.png")));
		add(icon, BorderLayout.CENTER);
		//add(toolbar(), BorderLayout.NORTH);
		setSize(width, height);
		positionPad();
		
        setDefaultCloseOperation(closeOperation);
        setUndecorated(true);

        // Set the window to 55% opaque (45% translucent).
        final float defaultOpacity = 0.55f;
        setOpacity(defaultOpacity);
	}
	
	private JPanel toolbar() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JRadioButton alt = new JRadioButton("Alt");
		alt.setMnemonic(KeyEvent.VK_A);
		JRadioButton ctlrShiftU = new JRadioButton("Ctrl-Shift-U");
		ctlrShiftU.setMnemonic(KeyEvent.VK_C);
		ButtonGroup group = new ButtonGroup();
		group.add(alt);
		group.add(ctlrShiftU);
		
		alt.setSelected(true); //TODO do this via mode. The default must depend on current platform 
		
		panel.add(alt);
		panel.add(ctlrShiftU);
		
		alt.requestFocus();
		
		return panel;
	}
	
	void positionPad() {
        setLocation(getPadPosition(MouseInfo.getPointerInfo().getLocation()));
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	setLocation(getPadPosition(MouseInfo.getPointerInfo().getLocation()));
            }
        });

	}

	
	private Point getPadPosition(Point mousePosition) {
        return new Point(mousePosition.x - width / 2, mousePosition.y - height / 2);
	}

	// TODO: add radio button that allows changing code keys and initialize the default selection according to the current platform
	int[] getCodeKeys() {
		return new int[] {KeyEvent.VK_ALT};
	}

	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public static void main(String[] args) throws AWTException {
		ClickPad pad = new ClickPad(JFrame.EXIT_ON_CLOSE);
		Robot robot = new Robot();
		ClickPadEventManager eventManager = new ClickPadEventManager(pad, robot);
		pad.addMouseListener(eventManager);
		pad.addMouseMotionListener(eventManager);
		pad.addKeyListener(eventManager);
		pad.setVisible(true);
	}
	
}


// http://askubuntu.com/questions/101226/where-does-ubuntu-store-its-keyboard-shortcut-configuration
//Until Ubuntu 12.04
//The shortcuts are placed differently depending on witch desktop you are using (gnome/unity, kde, xfce, lxde,etc).
//For gnome, they are under
//~/.gconf/desktop/gnome/keybindings (custom shortcuts)
//and under
//~/.gconf/apps/metacity.
//Since Ubuntu 12.10
//~/.config/dconf/user (in dconf-editor: org.gnome.settings-daemon.plugins.media-keys.custom-keybindings (custom shortcuts)
//~/.config/compiz-1/compizconfig

