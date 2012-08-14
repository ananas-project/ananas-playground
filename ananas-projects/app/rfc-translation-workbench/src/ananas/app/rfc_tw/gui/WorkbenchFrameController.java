package ananas.app.rfc_tw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.awt.IEMenuItem;
import ananas.lib.blueprint.elements.swing.IEJDesktopPane;
import ananas.lib.blueprint.elements.swing.IEJFrame;

public class WorkbenchFrameController {

	// private IDocument mDoc;
	private final JFrame mMainFrame;
	private final JDesktopPane mDesktop;

	private WorkbenchFrameController() {

		// load
		String path = "/gui/WorkbenchFrame.xml";
		InputStream is = "".getClass().getResourceAsStream(path);
		String docURI = null;
		IDocument doc = Blueprint.getInstance().loadDocument(is, docURI);
		// this.mDoc = doc;

		IEJFrame mainFrame = (IEJFrame) doc.findElementById(R.id.root_view);
		this.mMainFrame = mainFrame.toJFrame();

		IEJDesktopPane desktop = (IEJDesktopPane) doc
				.findElementById(R.id.desktop);
		this.mDesktop = desktop.toJDesktopPane();

		IEMenuItem menuItem;
		menuItem = (IEMenuItem) doc.findElementById(R.id.menu_file_new);
		menuItem.toMenuItem().addActionListener(this.mMenuFileNewListener);

	}

	private final ActionListener mMenuFileNewListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			ProjectViewController pv = new ProjectViewController();
			JInternalFrame itf = pv.getJInternalFrame();
			itf.setVisible(true);
			itf.setBounds(0, 0, 640, 480);
			WorkbenchFrameController.this.mDesktop.add(itf);
		}
	};

	protected void show() {
		this.mMainFrame.setVisible(true);
	}

	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				WorkbenchFrameController wfc = new WorkbenchFrameController();
				wfc.show();
			}
		});
	}

}
