package ananas.lib.axk.stanza;

import org.w3c.dom.Element;

public interface StanzaContext {

	String stanzaToString();

	Element stanzaToElement();

	Object stanzaToObject();

	void setStanza(Object stanza);

	void setStanza(Element stanza);

	void setStanza(String stanza);

	StanzaConvertor getStanzaConvertor();

}
