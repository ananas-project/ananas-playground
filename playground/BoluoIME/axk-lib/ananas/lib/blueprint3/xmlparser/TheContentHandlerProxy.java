package ananas.lib.blueprint3.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class TheContentHandlerProxy implements ContentHandler {

	private final ContentHandler target;
	private TheAttributeList mAttList;
	private TheElementStack mElementStack;

	public TheContentHandlerProxy(ContentHandler target) {
		this.target = target;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (length <= 0) {
			return;
		}
		boolean isok = false;
		int p1, p2;
		p1 = start;
		p2 = start + length - 1;
		for (; p1 <= p2;) {
			char c1, c2;
			c1 = ch[p1];
			c2 = ch[p2];
			if (!this.isSpaceChar(c1)) {
				isok = true;
				break;
			}
			if (!this.isSpaceChar(c2)) {
				isok = true;
				break;
			}
			p1++;
			p2--;
		}
		if (isok)
			target.characters(ch, start, length);
	}

	private boolean isSpaceChar(char ch) {
		switch (ch) {
		case ' ':
		case 0x0a:
		case 0x0d:
		case '\t':
			return true;
		default:
			return false;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		target.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		final TheElementStack estack = this.getElementStack();

		final int pos = TheElementStack.parseQName(qName);
		localName = TheElementStack.parseQNameGetLocalName(qName, pos);
		final String prefix = TheElementStack.parseQNameGetPrefix(qName, pos);
		uri = estack.prefixToURI(prefix);

		String qname2 = estack.popElement();
		if (!qName.equals(qname2)) {
			throw new SAXException("the element not closed, qName=" + qname2);
		}

		target.endElement(uri, localName, qName);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator locator) {
		target.setDocumentLocator(locator);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		this.getElementStack().reset();
		target.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		final int pos = TheElementStack.parseQName(qName);
		localName = TheElementStack.parseQNameGetLocalName(qName, pos);
		final String prefix = TheElementStack.parseQNameGetPrefix(qName, pos);

		TheAttributeList attList = this.mAttList;
		if (attList == null) {
			attList = new TheAttributeList(32);
			this.mAttList = attList;
		}
		attList.reset();

		final TheElementStack eleStack = this.getElementStack();
		eleStack.pushElement(qName);

		int len = atts.getLength();
		for (int i = 0; i < len; i++) {
			String qname = atts.getQName(i);
			String value = atts.getValue(i);
			if ("xmlns".equals(qname)) {
				eleStack.pushNS(qname, value);
				// attList.addAttr(qname, value);
			} else if (qname.startsWith("xmlns:")) {
				eleStack.pushNS(qname, value);
				// attList.addAttr(qname, value);
			} else {
				attList.addAttr(qname, value);
			}
		}

		INamespaceMapper nsMap = eleStack;
		attList.doNSMapping(nsMap);

		uri = nsMap.prefixToURI(prefix);
		target.startElement(uri, localName, qName, attList);
	}

	private TheElementStack getElementStack() {
		TheElementStack eleStack = this.mElementStack;
		if (eleStack == null) {
			eleStack = new TheElementStack(32);
			this.mElementStack = eleStack;
		}
		return eleStack;
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}
}