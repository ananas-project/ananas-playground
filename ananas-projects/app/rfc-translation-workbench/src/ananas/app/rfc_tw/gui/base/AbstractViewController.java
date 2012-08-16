package ananas.app.rfc_tw.gui.base;


public abstract class AbstractViewController implements IViewController {

	protected abstract ICommand onExecuteCommand(ICommand cmd);

	@Override
	public final ICommandChainNode getCommandChainNode() {
		return this.mCmdChainNode;
	}

	private final ICommandChainNode mCmdChainNode = new AbstractCommandChainNode() {

		@Override
		protected ICommand onExecuteCommand(ICommand cmd) {
			return AbstractViewController.this.onExecuteCommand(cmd);
		}
	};

}
