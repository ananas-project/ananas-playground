package ananas.lib.axk.engine;

import org.w3c.dom.Element;

public interface XEngineListener {

	void onStanzaElement(XContext context, Element element);

	void onPhaseChanged(XContext context, XPhase phase);

}
