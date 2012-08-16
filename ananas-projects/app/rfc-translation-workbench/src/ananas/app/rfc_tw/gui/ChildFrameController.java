package ananas.app.rfc_tw.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

import ananas.app.rfc_tw.gui.base.AbstractCommandChainNode;
import ananas.app.rfc_tw.gui.base.IChildFrameController;
import ananas.app.rfc_tw.gui.base.ICommand;
import ananas.app.rfc_tw.gui.base.ICommandChainNode;
import ananas.app.rfc_tw.gui.base.IViewController;

public class ChildFrameController implements IChildFrameController,
		FocusListener {

	private final JInternalFrame mInFrame;

	public ChildFrameController() {
		this.mInFrame = new JInternalFrame("Untitled", true, true, true, true);
		this.mInFrame.setBounds(0, 0, 640, 480);

		this._setupListener();

	}

	private void _setupListener() {

		this.mInFrame.addFocusListener(this);

	}

	@Override
	public JMenuBar getJMenuBar() {
		return null;
	}

	@Override
	public JInternalFrame getJInternalFrame() {
		return this.mInFrame;
	}

	@Override
	public ICommandChainNode getCommandChainNode() {
		return this.mCmdChainNode;
	}

	private final ICommandChainNode mCmdChainNode = new AbstractCommandChainNode() {

		@Override
		protected ICommand onExecuteCommand(ICommand cmd) {
			return cmd;
		}
	};
	private IViewController mContent;

	@Override
	public IViewController getContent() {
		return this.mContent;
	}

	@Override
	public void setContent(IViewController vc) {
		this.mContent = vc;
		this.mInFrame.getContentPane().add(vc.getView());
		ICommandChainNode pNode = this.getCommandChainNode();
		ICommandChainNode cNode = vc.getCommandChainNode();
		cNode.setNextNode(pNode);

		this.getJInternalFrame().setJMenuBar(vc.getJMenuBar());
	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}
}
