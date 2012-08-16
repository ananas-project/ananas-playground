package ananas.lib.blueprint.elements.swing;

import java.util.EventObject;

public class DefaultEventChainNode implements IEventChainNode {

	private IEventChainNode mNextNode;

	public DefaultEventChainNode() {
	}

	@Override
	public final void setNextNode(IEventChainNode nextNode) {
		this.mNextNode = nextNode;
	}

	@Override
	public final void processEvent(EventObject event) {
		event = this.onEvent(event);
		IEventChainNode nextNode = this.mNextNode;
		if (event != null && nextNode != null) {
			nextNode.processEvent(event);
		}
	}

	protected EventObject onEvent(EventObject event) {
		return event;
	}

}
