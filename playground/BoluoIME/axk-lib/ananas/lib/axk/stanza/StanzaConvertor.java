package ananas.lib.axk.stanza;

import org.w3c.dom.Element;

public interface StanzaConvertor {

	String elementToString(Element element);

	Object elementToObject(Element element);

	Element stringToElement(String string);
}
