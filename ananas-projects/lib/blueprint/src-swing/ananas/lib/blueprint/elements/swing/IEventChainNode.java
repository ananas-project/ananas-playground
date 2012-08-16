package ananas.lib.blueprint.elements.swing;

import java.util.EventObject;

public interface IEventChainNode {

	void setNextNode(IEventChainNode nextNode);

	void processEvent(EventObject event);

}
