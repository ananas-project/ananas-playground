package ananas.app.rfc_tw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ananas.app.rfc_tw.gui.base.AbstractCommandChainNode;
import ananas.app.rfc_tw.gui.base.IChildFrameController;
import ananas.app.rfc_tw.gui.base.ICommand;
import ananas.app.rfc_tw.gui.base.ICommandChainNode;
import ananas.app.rfc_tw.gui.base.IViewController;
import ananas.app.rfc_tw.gui.base.IWorkbenchFrameController;
import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class WorkbenchFrameController implements IWorkbenchFrameController {

	private final IDocument mDoc;
	private final JFrame mMainFrame;
	private final JDesktopPane mDesktop;
	private final JMenuBar mMenuBar = null;

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

			@Override
			public void actionPerformed(ActionEvent e) {

				WorkbenchFrameController.this._exeCmdFileNewProject();

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

	private void _exeCmdFileNewProject() {

		IProject project = IProject.Factory.newProject();
		NewProjectDialogController dlg = new NewProjectDialogController(project);
		JDialog jdlg = dlg.getJDialog();
		jdlg.setModal(true);
		jdlg.setVisible(true);
		if (project.getOriginalText() == null) {
			return;
		}

		IViewController prjView = new ProjectViewController(project);
		IChildFrameController childFrame = new ChildFrameController();
		childFrame.setContent(prjView);
		this.addChildFrame(childFrame);

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
	public JMenuBar getJMenuBar() {
		return this.mMenuBar;
	}

	@Override
	public void addChildFrame(IChildFrameController child) {
		JInternalFrame iframe = child.getJInternalFrame();
		this.mDesktop.add(iframe);
		iframe.setVisible(true);
		ICommandChainNode cNode = child.getCommandChainNode();
		ICommandChainNode pNode = this.getCommandChainNode();
		cNode.setNextNode(pNode);
	}

	@Override
	public JDesktopPane getJDesktopPane() {
		return this.mDesktop;
	}

	@Override
	public ICommandChainNode getCommandChainNode() {
		return this.mCmdChainNode;
	}

	private final ICommandChainNode mCmdChainNode = new AbstractCommandChainNode() {

		@Override
		protected ICommand onExecuteCommand(ICommand cmd) {
			return cmd;
		}
	};

}
