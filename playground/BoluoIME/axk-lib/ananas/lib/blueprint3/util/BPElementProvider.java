package ananas.lib.blueprint3.util;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElement;

public interface BPElementProvider {

	BPElement createElement(BPDocument doc, String uri, String localName);

}
