package kun.tomato_timer;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SleepFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5782842394022530213L;

	public SleepFrame() {
	}

	public void initLayout() {

		JLabel la = new JLabel("ZZZ");

		this.setBounds(200, 200, 512, 512);
		this.setBackground(Color.red);
		this.setTitle("Tomato Timer - ZZZ");
		this.add(la);

	}
}
