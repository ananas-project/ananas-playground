package ananas.app.rfc_tw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ananas.app.rfc_tw.gui.base.IChildFrameController;
import ananas.app.rfc_tw.gui.base.IViewController;
import ananas.app.rfc_tw.gui.base.IWorkbenchFrameController;
import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public class WorkbenchFrameController implements IWorkbenchFrameController {

	private final IDocument mDoc;
	private final JFrame mMainFrame;
	private final JDesktopPane mDesktop;
	private final JMenuBar mMenuBar;

	private WorkbenchFrameController() {

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.workbench_frame_xml);
		this.mDoc = doc;

		JFrame mainFrame = (JFrame) doc.findTargetById(R.id.root_view);
		this.mMainFrame = mainFrame;
		this.mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mMenuBar = (JMenuBar) doc.findTargetById(R.id.menu_bar);

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
		IChildFrameController childFrame = new ChildFrameController(this);
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
		IEventChainNode cNode = child.getEventChainNode();
		IEventChainNode pNode = this.getEventChainNode();
		cNode.setNextNode(pNode);
	}

	@Override
	public JDesktopPane getJDesktopPane() {
		return this.mDesktop;
	}

	@Override
	public IEventChainNode getEventChainNode() {
		return this.mEventChainNode;
	}

	protected EventObject onEvent(EventObject event) {
		if (event instanceof ActionEvent) {
			return this.onActionEvent((ActionEvent) event);
		} else {
			return event;
		}
	}

	private ActionEvent onActionEvent(ActionEvent event) {

		System.out.println(this + ".onActionEvent():" + event);
		return event;
	}

	private final IEventChainNode mEventChainNode = new DefaultEventChainNode() {

		@Override
		protected EventObject onEvent(EventObject event) {
			return WorkbenchFrameController.this.onEvent(event);
		}
	};

	@Override
	public JFrame getJFrame() {
		return this.mMainFrame;
	}

}
