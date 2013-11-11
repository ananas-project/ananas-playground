package kun.tomato_timer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import kun.tomato_timer.TheTimer.Listener;

public class SettingsFrame extends JFrame implements Listener {

	private static final long serialVersionUID = 2275914250918271910L;

	public static void display() {
		SettingsFrame frame = new SettingsFrame();
		frame.setBounds(100, 100, 320, 480);
		frame.setTitle("Tomato Timer - Settings");
		frame.initLayout();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private TheTimer _timer;
	private SleepFrame _sleep_frame;

	private void initLayout() {

		this._timer = new TheTimer(this);
		this.setLayout(new FlowLayout());

		JButton btn_start = new JButton("Start");
		JButton btn_stop = new JButton("Stop");

		this.add(btn_start);
		this.add(btn_stop);

		// listener

		btn_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsFrame.this._timer.start();
			}
		});
		btn_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsFrame.this._timer.stop();
			}
		});

	}

	@Override
	public void onStateChanged(TheTimer timer, int state) {

		SleepFrame sf = this._sleep_frame;
		if (sf == null) {
			sf = new SleepFrame();
			sf.initLayout();
			this._sleep_frame = sf;
		}

		switch (state) {
		case TheTimer.State.pause: {
			sf.setVisible(true);
			sf.toFront();
			break;
		}
		case TheTimer.State.working: {
			sf.setVisible(false);
			break;
		}
		default:
		}

	}

	@Override
	public void onTick(TheTimer timer) {

		SleepFrame sf = this._sleep_frame;
		if (sf != null) {
			sf.toFront();
		}

	}

}
