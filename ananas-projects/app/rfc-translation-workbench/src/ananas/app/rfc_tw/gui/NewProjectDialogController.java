package ananas.app.rfc_tw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;

import ananas.app.rfc_tw.gui.base.IDialogController;
import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class NewProjectDialogController implements IDialogController {

	private final JDialog mJDialog;
	private final IDocument mDoc;
	private final JTextComponent mTextOriginal;
	private final JTextComponent mTextPath;
	private final IProject mProject;
	private String mText;

	public NewProjectDialogController(IProject project) {

		this.mProject = project;

		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.new_project_dialog_xml);
		this.mDoc = doc;

		this.mJDialog = (JDialog) doc.findTargetById(R.id.root_view);
		this.mTextPath = (JTextComponent) doc.findTargetById(R.id.text_path);
		this.mTextOriginal = (JTextComponent) doc
				.findTargetById(R.id.text_original);

		this._setupEventListeners();
	}

	private void _setupEventListeners() {

		JButton btn;

		btn = (JButton) this.mDoc.findTargetById(R.id.button_browse);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NewProjectDialogController.this._onClickBrowse();
			}
		});

		btn = (JButton) this.mDoc.findTargetById(R.id.button_cancel);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NewProjectDialogController.this._onClickCancel();
			}
		});

		btn = (JButton) this.mDoc.findTargetById(R.id.button_ok);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NewProjectDialogController.this._onClickOK();
			}
		});
	}

	protected void _onClickBrowse() {
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(this.mJDialog);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			this._loadOriginal(file);
		}
	}

	protected void _onClickOK() {
		String text = this.mText;
		if (text != null) {
			this.mProject.setOriginalText(text);
		}
		this.mJDialog.setVisible(false);
	}

	protected void _onClickCancel() {
		this.mJDialog.setVisible(false);
	}

	protected void _loadOriginal(File file) {

		this.mTextPath.setText(file.getAbsolutePath());

		try {
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			StringBuffer sbuf = new StringBuffer();
			char[] cbuf = new char[512];
			for (int cc = isr.read(cbuf); cc > 0; cc = isr.read(cbuf)) {
				sbuf.append(cbuf, 0, cc);
			}

			final String text = sbuf.toString();
			this.mText = text;
			this.mTextOriginal.setText(text);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JDialog getJDialog() {
		return this.mJDialog;
	}

}
