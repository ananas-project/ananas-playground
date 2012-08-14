package ananas.app.rfc_tw.gui;

import java.io.InputStream;

import javax.swing.JInternalFrame;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.elements.swing.IEJInternalFrame;

public class ProjectViewController {

	private final JInternalFrame mRootView;

	// private IDocument mDoc;

	public ProjectViewController() {

		// load
		String path = "/gui/ProjectView.xml";
		InputStream is = "".getClass().getResourceAsStream(path);
		String docURI = null;
		IDocument doc = Blueprint.getInstance().loadDocument(is, docURI);
		// this.mDoc = doc;

		IEJInternalFrame mainFrame = (IEJInternalFrame) doc
				.findElementById(R.id.root_view);
		this.mRootView = mainFrame.toJInternalFrame();

	}

	public JInternalFrame getJInternalFrame() {
		return this.mRootView;
	}

}
