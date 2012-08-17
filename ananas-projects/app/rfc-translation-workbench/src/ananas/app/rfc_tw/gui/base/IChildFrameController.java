package ananas.app.rfc_tw.gui.base;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IChildFrameController {

	JMenuBar getJMenuBar();

	IViewController getContent();

	void setContent(IViewController vc);

	JInternalFrame getJInternalFrame();

	IEventChainNode getEventChainNode();

	IWorkbenchFrameController getWorkbench();

}
