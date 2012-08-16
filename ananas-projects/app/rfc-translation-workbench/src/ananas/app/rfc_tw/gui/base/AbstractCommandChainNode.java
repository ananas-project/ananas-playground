package ananas.app.rfc_tw.gui.base;


public abstract class AbstractCommandChainNode implements ICommandChainNode {

	private ICommandChainNode mNextNode;

	public AbstractCommandChainNode() {
	}

	@Override
	public final ICommandChainNode getNextNode() {
		return this.mNextNode;
	}

	@Override
	public final void setNextNode(ICommandChainNode node) {
		this.mNextNode = node;
	}

	@Override
	public final void executeCommand(ICommand cmd) {
		cmd = this.onExecuteCommand(cmd);
		ICommandChainNode nextNode = this.mNextNode;
		if (nextNode != null && cmd != null) {
			nextNode.executeCommand(cmd);
		}
	}

	protected abstract ICommand onExecuteCommand(ICommand cmd);

}
