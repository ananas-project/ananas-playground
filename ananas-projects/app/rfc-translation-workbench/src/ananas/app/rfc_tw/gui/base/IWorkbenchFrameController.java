package ananas.app.rfc_tw.gui.base;

import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;

public interface IWorkbenchFrameController {

	JMenuBar getJMenuBar();

	void addChildFrame(IChildFrameController child);

	JDesktopPane getJDesktopPane();

	ICommandChainNode getCommandChainNode();
}
