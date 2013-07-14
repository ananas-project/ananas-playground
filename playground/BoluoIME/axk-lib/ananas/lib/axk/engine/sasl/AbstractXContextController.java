package ananas.lib.axk.engine.sasl;

import java.io.IOException;
import java.io.OutputStream;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextController;
import ananas.lib.axk.engine.XPhase;

public abstract class AbstractXContextController implements XContextController {

	public String getElementFullURI(Element element) {
		String p1, p2;
		p1 = element.getNamespaceURI();
		p2 = element.getLocalName();
		return (p1 + "#" + p2);
	}

	public void send(XContext context, String s) {
		try {
			byte[] buff = s.getBytes("UTF-8");
			OutputStream out = context.getSocketContext().getOutput();
			out.write(buff);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStanzaElement(XContext context, Element stanza) {
		DOMImplementation impl = stanza.getOwnerDocument().getImplementation();
		DOMImplementationLS ls = (DOMImplementationLS) impl.getFeature("LS",
				"3.0");
		LSSerializer seri = ls.createLSSerializer();
		System.out.println(this + ".unsupportedElement:"
				+ seri.writeToString(stanza));

	}

	@Override
	public void onPhaseChanged(XContext context, XPhase phase) {
		context.getEngineListener().onPhaseChanged(context, phase);
	}

}
