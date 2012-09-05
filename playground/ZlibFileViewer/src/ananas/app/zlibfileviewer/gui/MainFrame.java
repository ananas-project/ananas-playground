package ananas.app.zlibfileviewer.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import ananas.app.zlibfileviewer.core.ConverToBin;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;
import ananas.lib.blueprint.elements.swing.IEJMenuBar;

public class MainFrame implements IEventChainNode {

	private final JFrame mFrame;
	private File mCurDir;

	public MainFrame() {
		// load
		final IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.main_frame_xml);
		this.mFrame = (JFrame) doc.findTargetById(R.id.root);
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// bind listener
		IEJMenuBar bar = (IEJMenuBar) doc.findElementById(R.id.menu_bar);
		bar.getEventChainNode().setNextNode(this);
	}

	public void show() {
		this.mFrame.setVisible(true);
	}

	@Override
	public void setNextNode(IEventChainNode nextNode) {
	}

	@Override
	public void processEvent(EventObject event) {
		if (event instanceof ActionEvent) {
			String cmd = ((ActionEvent) event).getActionCommand();
			if (cmd == null) {
			} else if (cmd.equals(R.command.do_file_open)) {
				this._doFileOpen();
			} else {
				System.out.println("unknow : " + event);
			}
		}
	}

	private void _doFileOpen() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.mCurDir);
		/*
		 * FileNameExtensionFilter filter = new FileNameExtensionFilter(
		 * "JPG & GIF Images", "jpg", "gif"); // fc.setFileFilter(filter);
		 */
		int returnVal = fc.showOpenDialog(this.mFrame);
		this.mCurDir = fc.getCurrentDirectory();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			System.out
					.println("You chose to open this file: " + file.getName());
			this._doFileOpen(file);
		}
	}

	private void _doFileOpen(File file) {
		(new ConverToBin()).binaryToXml(file);
	}
}
