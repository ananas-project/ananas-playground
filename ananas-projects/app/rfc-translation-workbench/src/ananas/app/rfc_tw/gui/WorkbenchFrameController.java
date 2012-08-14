package ananas.app.rfc_tw.gui;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class WorkbenchFrameController {

	// private IDocument mDoc;
	private final JFrame mMainFrame;
	private final JDesktopPane mDesktop;

	private WorkbenchFrameController() {

		// load
		String uri = "resource:///gui/WorkbenchFrame.xml";
		IDocument doc = Blueprint.getInstance().loadDocument(uri);

		JFrame mainFrame = (JFrame) doc.findTargetById(R.id.root_view);
		this.mMainFrame = mainFrame;

		JDesktopPane desktop = (JDesktopPane) doc.findTargetById(R.id.desktop);
		this.mDesktop = desktop;

		MenuItem menuItem = (MenuItem) doc.findTargetById(R.id.menu_file_new);
		menuItem.addActionListener(this.mMenuFileNewListener);

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
