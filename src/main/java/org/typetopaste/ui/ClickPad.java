package org.typetopaste.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * {@code ClickPad} is a small head-less window dynamically positioned under the mouse pointer and 
 * receives mouse click that triggers typing emulation instead of pasting using clipboard.
 * 
 * {@code ClickPad} is positioned automatically while mouse is being moved but can be also moved using 
 * using arrow keys. Typing is triggered either by click or by regular {@code Ctrl+V} or {@code Shift+Insert} keyboard shortcuts.
 * {@code ClickPad} contains 2 radio buttons that allow switching among 2 prefixes for Unicode typing: {@code Alt} and {@code Ctrl+Shft+U}.   
 *         
 * @author alex
 */
@SuppressWarnings("serial")
public class ClickPad extends JFrame {
	private ResourceDiscoverer resourceDiscoverer = new ResourceDiscoverer();
	private int width;
	private int height;
	private JRadioButton alt;
	private JRadioButton ctlrShiftU;
	private JSpinner delay;
	private JButton help;
	
	
	private static final int[][][] allCodeKeys = new int[][][] {
			new int[][] {{KeyEvent.VK_ALT}},
			new int[][] {{KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT}, {KeyEvent.VK_U}}
	};
	
	

	
	
	public ClickPad(int closeOperation) {
		this(220, 120, closeOperation);
	}
	
	
	public ClickPad(int width, int height, int closeOperation) {
		this.width = width;
		this.height = height;
		
		setLayout(new BorderLayout(0, 0));
		setAlwaysOnTop(true);
		JLabel icon = new JLabel(UIUtil.getTypeToPasteIcon());
		add(icon, BorderLayout.CENTER);
		add(toolbar(), BorderLayout.NORTH);
		setSize(width, height);
		positionPad();
		
        setDefaultCloseOperation(closeOperation);
        setUndecorated(true);

        // Set the window to 55% opaque (45% translucent).
        final float defaultOpacity = 0.55f;
        setOpacity(defaultOpacity);
	}
	
	private JPanel toolbar() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		alt = new JRadioButton("Alt");
		alt.setMnemonic(KeyEvent.VK_A);
		ctlrShiftU = new JRadioButton("Ctrl-Shift-U");
		ctlrShiftU.setMnemonic(KeyEvent.VK_C);
		ButtonGroup group = new ButtonGroup();
		group.add(alt);
		group.add(ctlrShiftU);
		
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			alt.setSelected(true); 
		} else if (osName.toLowerCase().contains("linux")) {
			 ctlrShiftU.setSelected(true);
		}
		
		//TODO disable unicode input for unsupported platform
		
		delay = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		delay.setEnabled(false);
		help = new JButton("?");
		Font font = help.getFont();
		Map<TextAttribute, Integer> fontAttributes = Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		help.setFont(font.deriveFont(fontAttributes));
		help.setMnemonic(KeyEvent.VK_SLASH);
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(resourceDiscoverer.getHelpFile().toURI());
				} catch (IOException e1) {
					UIUtil.showErrorMessage("Cannot show help");
				}
			}
		});
		
		JComponent field = ((JSpinner.DefaultEditor) delay.getEditor());
		Dimension prefSize = field.getPreferredSize();
		int width = field.getFontMetrics(field.getFont()).stringWidth("1000");
		field.setPreferredSize(new Dimension(width, prefSize.height));
		
		panel.add(delay);
		panel.add(alt);
		panel.add(ctlrShiftU);
		panel.add(help);
		
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

	
	int[][][] getAllCodeKeys() {
		return allCodeKeys;
	}
	
	int[][] getCodeKeys() {
		if(alt.isSelected()) {
			return allCodeKeys[0];
		} 
		if (ctlrShiftU.isSelected()) {
			return allCodeKeys[1];
		}
		return null;
	}

	
	int configureDelay(int increment) {
		int newValue = ((Integer)delay.getValue()).intValue() + increment;
		delay.setValue(newValue);
		return newValue;
	}
	
	int getDelay() {
		return ((Integer)delay.getValue()).intValue();
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
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(eventManager);		
		
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

