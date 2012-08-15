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
				System.out.println("run " + TestMDI.class);
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
				R.file.test_mdi_xml);

		// bind listener
		this.mFrame = (JFrame) doc.findTargetById(R.id.root);
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		IEMenuItem menuItem = (IEMenuItem) doc
				.findElementById(R.id.__id_menu_new_window);
		menuItem.toMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TestMDI.this.onClickNewWindow();
			}
		});

		this.mDesktop = (JDesktopPane) doc.findTargetById(R.id.__id_desktop);

	}

	protected void onClickNewWindow() {

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.test_mdi_child_xml);
		JInternalFrame iFrame = (JInternalFrame) doc.findTargetById(R.id.root);
		iFrame.setVisible(true);
		iFrame.setBounds(0, 0, 640, 480);
		this.mDesktop.add(iFrame);
	}

	protected void show() {
		this.mFrame.setVisible(true);
	}

}
