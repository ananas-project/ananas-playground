package test.blueprint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.awt.IEMenuItem;
import ananas.lib.blueprint.elements.swing.IEJButton;

public class DemoMain {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DemoFrameCtrl ctrl = new DemoFrameCtrl();
				ctrl.show();
			}
		});
	}

	private static class DemoFrameCtrl {
		private JFrame mFrame;

		public DemoFrameCtrl() {

			IDocument doc = Blueprint.getInstance().loadDocument(
					"resource:///test.xml");

			JFrame frame = (JFrame) doc.findElementById("root").getTarget();
			this.mFrame = frame;
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			IEMenuItem menuItemExit = (IEMenuItem) doc
					.findElementById("id_menu_exit");
			menuItemExit.toMenuItem().addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});

			IEJButton button = (IEJButton) doc
					.findElementById("id_button_hello");
			button.toJButton().addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					javax.swing.JOptionPane.showMessageDialog(
							DemoFrameCtrl.this.mFrame, "Yeah!");

				}
			});

		}

		public void show() {
			this.mFrame.setVisible(true);
		}
	}
}
