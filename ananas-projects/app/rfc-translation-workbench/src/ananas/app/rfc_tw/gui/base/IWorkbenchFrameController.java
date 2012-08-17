package ananas.app.rfc_tw.gui.base;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IWorkbenchFrameController {

	JMenuBar getJMenuBar();

	void addChildFrame(IChildFrameController child);

	JDesktopPane getJDesktopPane();

	JFrame getJFrame();

	IEventChainNode getEventChainNode();
}
