package ananas.lib.impl.axk.client.target;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.api.IExStanzaConvertorProvider;
import ananas.lib.axk.stanza.StanzaConvertor;

public class Tar_stanza_convertor extends Tar_abstractClient implements
		IExStanzaConvertorProvider {

	private StanzaConvertor m_convertor = new MyConvertor();

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass) {
		if (apiClass == null) {
			return null;
		} else if (apiClass.equals(IExStanzaConvertorProvider.class)) {
			IExStanzaConvertorProvider api = this;
			return api;
		} else {
			return super.getExAPI(apiClass);
		}
	}

	@Override
	public StanzaConvertor getStanzaConvertor() {
		return this.m_convertor;
	}

	class MyConvertor implements StanzaConvertor {

		@Override
		public String elementToString(Element element) {
			DOMImplementation impl = element.getOwnerDocument()
					.getImplementation();
			DOMImplementationLS ls = (DOMImplementationLS) impl.getFeature(
					"LS", "3.0");
			LSSerializer seri = ls.createLSSerializer();
			return seri.writeToString(element);
		}

		@Override
		public Object elementToObject(Element element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element stringToElement(String string) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
