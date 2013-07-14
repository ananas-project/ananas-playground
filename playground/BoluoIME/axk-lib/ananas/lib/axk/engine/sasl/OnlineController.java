package ananas.lib.axk.engine.sasl;

import org.w3c.dom.Element;

import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextController;
import ananas.lib.axk.engine.XPhase;

public class OnlineController implements XContextController {

	@Override
	public void onStanzaElement(XContext context, Element element) {
		context.getEngineListener().onStanzaElement(context, element);
	}

	@Override
	public void onPhaseChanged(XContext context, XPhase phase) {
		context.getEngineListener().onPhaseChanged(context, phase);
	}

}
