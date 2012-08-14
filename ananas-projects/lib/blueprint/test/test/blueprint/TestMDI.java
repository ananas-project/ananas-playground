package test.blueprint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.awt.IEMenuItem;

public class TestMDI {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TestMDI mdi = new TestMDI();
				mdi.show();
			}
		});
	}

	private JFrame mFrame;
	private JDesktopPane mDesktop;

	public TestMDI() {

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				"resource:///test-mdi.xml");

		// bind listener
		this.mFrame = (JFrame) doc.findTargetById("root");
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		IEMenuItem menuItem = (IEMenuItem) doc
				.findElementById("@+id/menu_new_window");
		menuItem.toMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TestMDI.this.onClickNewWindow();
			}
		});

		this.mDesktop = (JDesktopPane) doc.findTargetById("@+id/desktop");

	}

	protected void onClickNewWindow() {

		// load
		String uri = "resource:///test-mdi-child.xml";
		IDocument doc = Blueprint.getInstance().loadDocument(uri);
		JInternalFrame iFrame = (JInternalFrame) doc.findTargetById("root");
		iFrame.setVisible(true);
		iFrame.setBounds(0, 0, 640, 480);
		this.mDesktop.add(iFrame);
	}

	protected void show() {
		this.mFrame.setVisible(true);
	}

}
