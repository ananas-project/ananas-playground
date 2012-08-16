package ananas.app.rfc_tw.gui.base;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

public interface IChildFrameController {

	JMenuBar getJMenuBar();

	IViewController getContent();

	void setContent(IViewController vc);

	JInternalFrame getJInternalFrame();

	ICommandChainNode getCommandChainNode();

}
