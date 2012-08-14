package ananas.lib.blueprint.elements.awt;

import java.awt.LayoutManager;

public interface IELayoutManager extends IEObject {

	LayoutManager toLayoutManager();

	void addComponentToContainer(IEComponent component, IEContainer container,
			IE_position position);

}
