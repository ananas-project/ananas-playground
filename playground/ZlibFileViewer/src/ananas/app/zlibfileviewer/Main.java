package ananas.app.zlibfileviewer;

import ananas.app.zlibfileviewer.gui.MainFrame;

public class Main {

	public static void main(String[] arg) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("run " + MainFrame.class);
				MainFrame mdi = new MainFrame();
				mdi.show();
			}
		});
	}
}
