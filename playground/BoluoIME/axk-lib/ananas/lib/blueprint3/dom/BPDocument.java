package ananas.lib.blueprint3.dom;

import java.net.URI;

public interface BPDocument extends BPNode {

	BPElement getRootElement();

	void setRootElement(BPElement root);

	URI getDocumentURI();

	String getDocumentType();

	void setDocumentType(String type);

	BPText createText(String data);

	BPAttribute createAttribute(String uri, String localName, String value);

	BPElement createElement(String uri, String localName);

	BPElement findElementById(String id);

	BPElement findElementByURI(String uri);

	BPDocumentGroup getDocumentGroup();

	Object findTargetById(String id);

	Object findTargetByURI(String uri);

	BPElementMap getElementRegistrar();

	void setElementRegistrar(BPElementMap reg);

}
