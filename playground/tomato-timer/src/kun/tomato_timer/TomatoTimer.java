package kun.tomato_timer;

public class TomatoTimer {

	public static void main(String[] arg) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SettingsFrame.display();
			}
		});

	}

}
