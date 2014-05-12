package ananas.app.dlm2kml.gui;

import javax.swing.JFrame;

public class DLM2KML_MainFrame extends JFrame {

	private static final long serialVersionUID = -212486718905877263L;

	public static void main(String[] arg) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				UICore uiCore = new UICore();
				DLM2KML_MainFrame frame = new DLM2KML_MainFrame();
				frame.onCreate(uiCore);
				frame.setVisible(true);

			}
		});

	}

	protected void onCreate(UICore uiCore) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.getClass().getName());
		this.setSize(640, 480);

	}
}
