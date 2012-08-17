package ananas.lib.blueprint.elements.awt.util;

import java.util.EventObject;

public interface IEventChainNode {

	void setNextNode(IEventChainNode nextNode);

	void processEvent(EventObject event);

}
