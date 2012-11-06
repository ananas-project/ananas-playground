package ananas.app.zlibfileviewer.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.InflaterInputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

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

		// (new ConverToBin()).binaryToXml(file);

		String sha1 = null;

		if (file.length() > (1024 * 1024)) {
			this.mTextContent.setText("[Warning:Too Large]");

		} else {

			final byte[] raw = this._readFromFile(file);
			byte[] plain = this._decode(raw);
			if (plain == null) {
				plain = raw;
			}
			sha1 = this._calcSHA1_string(plain);

			String s;
			try {
				s = new String(plain, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				s = e.toString();
			}
			this.mTextContent.setText(s);

		}

		StringBuilder sb = new StringBuilder();
		sb.append(file.getName() + "\n");
		sb.append("\n");
		sb.append("path:" + file.getAbsolutePath() + "\n");
		sb.append("length:" + file.length() + "\n");
		sb.append("plain-content-sha1:" + sha1 + "\n");
		this.mTextOverview.setText(sb.toString());

	}

	private byte[] _decode(byte[] data) {
		InputStream is = null;
		InputStream is2 = null;
		ByteArrayOutputStream os = null;
		try {
			is = new ByteArrayInputStream(data);
			is2 = new InflaterInputStream(is);
			os = new ByteArrayOutputStream(1024);
			int cb = 1024;
			final byte[] buf = new byte[cb];
			while ((cb = is2.read(buf)) > 0) {
				os.write(buf, 0, cb);
			}
			os.close();
			is2.close();
			is.close();
			byte[] ba = os.toByteArray();
			if (ba.length <= 0) {
				return null;
			}
			return ba;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] _readFromFile(File file) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			is = new FileInputStream(file);
			os = new ByteArrayOutputStream(1024);
			int cb = 1024;
			final byte[] buf = new byte[cb];
			while ((cb = is.read(buf)) > 0) {
				os.write(buf, 0, cb);
			}
			os.close();
			is.close();
			byte[] ba = os.toByteArray();
			return ba;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String _calcSHA1_string(byte[] ba) {
		ba = _calcSHA1(ba);
		final char[] chs = "0123456789abcdef".toCharArray();
		final StringBuilder sb = new StringBuilder();
		for (int b : ba) {
			char c;
			c = chs[(b >> 4) & 0x0f];
			sb.append(c);
			c = chs[b & 0x0f];
			sb.append(c);
		}
		return sb.toString();
	}

	private byte[] _calcSHA1(byte[] ba) {
		try {
			MessageDigest md = MessageDigest.getInstance("sha1");
			md.update(ba);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
