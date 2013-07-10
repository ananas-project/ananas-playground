package ananas.lib.axk.engine.impl;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XEngineListener;

class MyDomBuilderImpl implements XBuilder {

	private final ContentHandler m_ch;
	private final ErrorHandler m_eh;

	public MyDomBuilderImpl(XContext context) {
		this.m_ch = new myContentHandler(context);
		this.m_eh = new myErrorHandler(context);
	}

	@Override
	public ContentHandler getContentHandler() {
		return this.m_ch;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return this.m_eh;
	}

	class myContentHandler implements ContentHandler {

		private final XContext mContext;
		private final Stack<Element> mElementStack;
		private Document mDoc;

		public myContentHandler(XContext context) {
			this.mContext = context;
			this.mElementStack = new Stack<Element>();
		}

		@Override
		public void characters(char[] chs, int offset, int length)
				throws SAXException {

			if (this.mElementStack.size() > 0) {
				String s = new String(chs, offset, length);
				Element element = this.mElementStack.peek();
				Text text = this.mDoc.createTextNode(s);
				element.appendChild(text);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			this.mElementStack.clear();
			this.mDoc = null;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			Element child = this.mElementStack.pop();
			if (this.mElementStack.size() > 0) {
				Element parent = this.mElementStack.peek();
				parent.appendChild(child);
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
			String namespaceURI = "http://etherx.jabber.org/streams";
			String qualifiedName = "stream:stream";
			DocumentType doctype = null;
			Document doc = this.mContext.getDOMImplementation().createDocument(
					namespaceURI, qualifiedName, doctype);

			this.mDoc = doc;
			this.mElementStack.clear();

			Element root = new MyStanzaListener(this.mContext);
			this.mElementStack.push(root);
		}

		final static String streamQName = "stream:stream";
		final static String streamURI = "http://etherx.jabber.org/streams";
		final static String streamLocalName = "stream";

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {

			if (qName.equals(streamQName))
				if (uri.equals(streamURI))
					if (localName.equals(streamLocalName)) {
						// skip the <stream:stream />
						return;
					}

			Element element = this.mDoc.createElementNS(uri, qName);
			int len = atts.getLength();
			for (int i = 0; i < len; i++) {
				String attrQName, attrValue;
				attrQName = atts.getQName(i);
				attrValue = atts.getValue(i);
				element.setAttribute(attrQName, attrValue);
			}
			this.mElementStack.push(element);
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1)
				throws SAXException {
			// TODO Auto-generated method stub

		}
	}

	class myErrorHandler implements ErrorHandler {

		public myErrorHandler(XContext context) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void error(SAXParseException arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void fatalError(SAXParseException arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void warning(SAXParseException arg0) throws SAXException {
			// TODO Auto-generated method stub

		}
	}

	class MyStanzaListener extends AbstractElement {

		private final XEngineListener mEngineListener;
		private final XContext mContext;

		public MyStanzaListener(XContext context) {
			this.mContext = context;
			this.mEngineListener = context.getContextControllerAgent();
		}

		@Override
		public Node appendChild(Node newChild) {
			if (newChild instanceof Element) {
				Element element = (Element) newChild;
				this.mEngineListener.onStanzaElement(this.mContext, element);
			}
			return null;
		}
	}
}
