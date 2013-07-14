package ananas.lib.axk.stanza;

import org.w3c.dom.Element;

public class DefaultStanzaContext implements StanzaContext {

	private String m_string;
	private Element m_element;
	private Object m_object;
	private final StanzaConvertor m_convertor;

	public DefaultStanzaContext(StanzaConvertor convertor) {
		this.m_convertor = convertor;
	}

	@Override
	public String stanzaToString() {
		String ret = this.m_string;
		if (ret == null) {
			ret = this.m_convertor.elementToString(this.m_element);
			this.m_string = ret;
		}
		return ret;
	}

	@Override
	public Element stanzaToElement() {
		Element ret = this.m_element;
		if (ret == null) {
			ret = this.m_convertor.stringToElement(this.m_string);
			this.m_element = ret;
		}
		return ret;
	}

	@Override
	public Object stanzaToObject() {
		Object ret = this.m_object;
		if (ret == null) {
			Element element = this.stanzaToElement();
			ret = this.m_convertor.elementToObject(element);
			this.m_object = ret;
		}
		return ret;
	}

	@Override
	public void setStanza(Object stanza) {
		this.m_element = null;
		this.m_object = stanza;
		this.m_string = null;
	}

	@Override
	public void setStanza(Element stanza) {
		this.m_element = stanza;
		this.m_object = null;
		this.m_string = null;
	}

	@Override
	public void setStanza(String stanza) {
		this.m_element = null;
		this.m_object = null;
		this.m_string = stanza;
	}

	@Override
	public StanzaConvertor getStanzaConvertor() {
		return this.m_convertor;
	}

}
