package ananas.app.rfc_tw.gui.base;

public interface ICommandChainNode {

	ICommandChainNode getNextNode();

	void setNextNode(ICommandChainNode node);

	void executeCommand(ICommand cmd);
}
