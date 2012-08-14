package ananas.app.rfc_tw.gui;

import javax.swing.JInternalFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;

public class ProjectViewController {

	private final JInternalFrame mRootView;

	// private IDocument mDoc;

	public ProjectViewController() {

		// load
		String uri = "resource:///gui/ProjectView.xml";
		IDocument doc = Blueprint.getInstance().loadDocument(uri);
		// this.mDoc = doc;

		JInternalFrame mainFrame = (JInternalFrame) doc
				.findTargetById(R.id.root_view);
		this.mRootView = mainFrame;

	}

	public JInternalFrame getJInternalFrame() {
		return this.mRootView;
	}

}
