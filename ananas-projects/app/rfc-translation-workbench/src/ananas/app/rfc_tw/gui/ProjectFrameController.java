package ananas.app.rfc_tw.gui;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;

import ananas.app.rfc_tw.event.Event;
import ananas.app.rfc_tw.event.IEventListener;
import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class ProjectFrameController implements IInternalFrameController {

	private final IProject mProject;

	private final JInternalFrame mRootView;
	private final JTextArea mOriginalTextView;

	private JMenuBar mMenuBar;

	// private IDocument mDoc;

	public ProjectFrameController(IProject project) {
		this.mProject = project;

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.project_frame_xml);
		// this.mDoc = doc;

		this.mRootView = (JInternalFrame) doc.findTargetById(R.id.root_view);
		this.mOriginalTextView = (JTextArea) doc
				.findTargetById(R.id.original_text_view);

		this._setEventHandlers();

		this.mOriginalTextView.setText(project.getOriginalText());

	}

	private void _setEventHandlers() {

		this.mProject.addOriginalTextListener(new IEventListener() {

			@Override
			public void onEvent(Event event) {
				ProjectFrameController pthis = ProjectFrameController.this;
				String text = pthis.mProject.getOriginalText();
				pthis.mOriginalTextView.setText(text);
			}
		});

	}

	public JInternalFrame getJInternalFrame() {
		return this.mRootView;
	}

	@Override
	public JComponent getRootView() {
		return this.mRootView;
	}

	@Override
	public JMenuBar getJMenuBar() {
		return this.mMenuBar;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
