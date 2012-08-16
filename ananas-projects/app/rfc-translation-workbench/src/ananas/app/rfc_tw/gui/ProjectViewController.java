package ananas.app.rfc_tw.gui;

import java.awt.Component;

import javax.swing.JMenuBar;
import javax.swing.JTextArea;

import ananas.app.rfc_tw.event.Event;
import ananas.app.rfc_tw.event.IEventListener;
import ananas.app.rfc_tw.gui.base.AbstractViewController;
import ananas.app.rfc_tw.gui.base.ICommand;
import ananas.app.rfc_tw.model.IProject;
import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class ProjectViewController extends AbstractViewController {

	private final IProject mProject;
	private final JTextArea mOriginalTextView;
	private final JMenuBar mMenuBar;
	private final Component mRootView;

	// private IDocument mDoc;

	public ProjectViewController(IProject project) {
		this.mProject = project;

		// load
		IDocument doc = Blueprint.getInstance().loadDocument(
				R.file.project_view_xml);
		// this.mDoc = doc;

		this.mRootView = (Component) doc.findTargetById(R.id.root_view);
		this.mOriginalTextView = (JTextArea) doc
				.findTargetById(R.id.original_text_view);
		this.mMenuBar = (JMenuBar) doc.findTargetById(R.id.menu_bar);

		this._setupEventHandlers();

		this.mOriginalTextView.setText(project.getOriginalText());

	}

	private void _setupEventHandlers() {

		this.mProject.addOriginalTextListener(new IEventListener() {

			@Override
			public void onEvent(Event event) {
				ProjectViewController pthis = ProjectViewController.this;
				String text = pthis.mProject.getOriginalText();
				pthis.mOriginalTextView.setText(text);
			}
		});

	}

	@Override
	public JMenuBar getJMenuBar() {
		return this.mMenuBar;
	}

	@Override
	public Component getView() {
		return this.mRootView;
	}

	@Override
	protected ICommand onExecuteCommand(ICommand cmd) {
		return cmd;
	}

}
