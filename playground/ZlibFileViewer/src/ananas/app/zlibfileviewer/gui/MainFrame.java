package ananas.app.zlibfileviewer.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ananas.app.zlibfileviewer.core.ConverToBin;
import ananas.lib.blueprint2.Blueprint2;
import ananas.lib.blueprint2.dom.IDocument;
import ananas.lib.blueprint2.swing_ex.JDirectoryTreeNode;

public class MainFrame {

	private final JFrame mFrame;
	private File mCurDir;
	private File mCurFile;
	private final JTree mDirTree;
	private final JTextArea mTextOverview;
	private final JTextArea mTextContent;

	public MainFrame() {
		// load
		Blueprint2 bp = Blueprint2.getInstance();
		IDocument doc;
		try {
			doc = bp.loadDocument(R.file.main_frame_xml);
		} catch (IOException e) {
			e.printStackTrace();
			doc = null;
		}
		this.mFrame = (JFrame) doc.findTargetById(R.id.root);
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mDirTree = (JTree) doc.findTargetById("dirTree");
		this.mTextOverview = (JTextArea) doc.findTargetById("textOverview");
		this.mTextContent = (JTextArea) doc.findTargetById("textContent");

		// bind listener

		this._bindListener();

	}

	private void _bindListener() {
		this.mDirTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent arg0) {
						TreePath path = MainFrame.this.mDirTree
								.getSelectionPath();
						JDirectoryTreeNode comp = (JDirectoryTreeNode) path
								.getLastPathComponent();
						File file = comp.getFile();
						MainFrame.this._doFileOpen(file);
					}
				});
	}

	public void show() {
		this.mFrame.setVisible(true);
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

		if (file.isDirectory()) {
			return;
		}

		this.mCurFile = file;

		StringBuilder sb = new StringBuilder();
		sb.append(file.getName() + "\n");
		sb.append("\n");
		sb.append("path:" + file.getAbsolutePath() + "\n");
		sb.append("length:" + file.length() + "\n");
		this.mTextOverview.setText(sb.toString());

		// (new ConverToBin()).binaryToXml(file);
	}
}
