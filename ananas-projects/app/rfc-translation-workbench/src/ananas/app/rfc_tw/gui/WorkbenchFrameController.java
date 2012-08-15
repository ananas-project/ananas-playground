package ananas.app.rfc_tw.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class WorkbenchFrameController implements IViewController {

	private final IDocument mDoc;
	private final JFrame mMainFrame;
	private final JDesktopPane mDesktop;

	private WorkbenchFrameController() {

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.workbench_frame_xml);
		this.mDoc = doc;

		JFrame mainFrame = (JFrame) doc.findTargetById(R.id.root_view);
		this.mMainFrame = mainFrame;
		this.mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JDesktopPane desktop = (JDesktopPane) doc.findTargetById(R.id.desktop);
		this.mDesktop = desktop;

		this._setupEventHandlers();

	}

	private void _setupEventHandlers() {

		JMenuItem menuItem;

		menuItem = (JMenuItem) this.mDoc.findTargetById(R.id.menu_file_new);
		menuItem.addActionListener(new ActionListener() {

			private int mX = 0;
			private int mY = 0;

			@Override
			public void actionPerformed(ActionEvent e) {

				IProject project = IProject.Factory.newProject();

				SetOriginalTextDialogController dlg = new SetOriginalTextDialogController(
						project);
				dlg.show();

				int x = ((this.mX++) % 10) * 30;
				int y = ((this.mY++) % 10) * 30;

				ProjectFrameController pv = new ProjectFrameController(project);
				JInternalFrame itf = pv.getJInternalFrame();
				itf.setVisible(true);
				itf.setBounds(x, y, 640, 480);
				WorkbenchFrameController.this.mDesktop.add(itf);
				itf.moveToFront();
			}
		});

		menuItem = (JMenuItem) this.mDoc.findTargetById(R.id.menu_file_exit);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

	}

	public void show() {
		this.mMainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.mMainFrame.setVisible(true);
	}

	public static void showFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				WorkbenchFrameController wfc = new WorkbenchFrameController();
				wfc.show();
			}
		});
	}

	@Override
	public Component getRootView() {
		return this.mMainFrame;
	}

}
