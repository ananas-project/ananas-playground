package ananas.lib.blueprint3.loader.reflect;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import ananas.lib.blueprint3.loader.reflect.dom.RefDocument;
import ananas.lib.blueprint3.loader.reflect.dom.RefElement;
import ananas.lib.blueprint3.loader.reflect.dom.RefNode;

public class MyReflectConfigBuilder implements ContentHandler {

	private final Stack<RefNode> mStack = new Stack<RefNode>();
	private RefDocument mDoc;

	public RefDocument getDocument() {
		return this.mDoc;
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws SAXException {
		this.mStack.clear();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		RefNode child = this.mStack.pop();
		RefNode parent = this.mStack.peek();
		boolean rlt = parent.appendChild(child);
		if (!rlt) {
			throw new RuntimeException("Cannot append child[" + child
					+ "] to parent [" + parent + "]");
		}
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		RefDocument doc = new RefDocument();
		this.mDoc = doc;
		this.mStack.clear();
		this.mStack.push(doc);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		RefElement element = this.mDoc.createElement(uri, localName);
		if (element == null) {
			throw new RuntimeException("Cannot create element " + uri + "#"
					+ localName);
		}
		this.mStack.push(element);

		int len = atts.getLength();
		for (int i = 0; i < len; i++) {
			String attrURI = atts.getURI(i);
			String attrLName = atts.getLocalName(i);
			String attrValue = atts.getValue(i);
			boolean rlt = element.setAttribute(attrURI, attrLName, attrValue);
			if (!rlt) {
				throw new RuntimeException("Cannot set attribute '" + attrLName
						+ "' for element " + element);
			}
		}

	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub

	}

}
