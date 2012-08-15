package ananas.app.rfc_tw.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class SetOriginalTextDialogController implements IViewController {

	private final JDialog mJDialog;
	private final IDocument mDoc;

	public SetOriginalTextDialogController(IProject project) {

		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.set_original_text_dialog_xml);
		this.mDoc = doc;

		JDialog dlg = (JDialog) doc.findTargetById(R.id.root_view);
		this.mJDialog = dlg;

		this._setupEventListeners();
	}

	private void _setupEventListeners() {

		JButton btn = (JButton) this.mDoc.findTargetById(R.id.button_browse);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				int result = fc.showOpenDialog(null);

			}
		});

	}

	@Override
	public Component getRootView() {
		return this.mJDialog;
	}

	@Override
	public void show() {
		this.mJDialog.setModal(true);
		this.mJDialog.setVisible(true);
	}

}
