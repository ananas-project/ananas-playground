package ananas.lib.impl.blueprint3.core;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPNode;
import ananas.lib.blueprint3.dom.BPText;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.util.BPBuilder;
import ananas.lib.blueprint3.util.BPBuilderFactory;
import ananas.lib.blueprint3.util.BPElementProvider;
import ananas.lib.blueprint3.util.BPErrorHandler;

public class BuilderFactoryImpl implements BPBuilderFactory {

	@Override
	public BPBuilder newBuilder(BPDocument doc) {
		return new MyBuilderCore(doc);
	}

	@Override
	public BPBuilder newBuilder(BPElement element) {
		return new MyBuilderCore(element);
	}

	class MyStack extends Stack<BPNode> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7054127141543580379L;

		public MyStack(int size) {
			// TODO Auto-generated constructor stub
		}

	}

	static class MyExceptionFactory {

		public static Exception _elementNotAcceptAttr(BPElement element,
				String uri, String localName, String value) {

			return new BlueprintException(
					"element not accept attribute : [element]=" + element
							+ " [attr]=" + uri + "#" + localName + "='" + value
							+ "'");
		}

		public static Exception _noAttr(BPElement element, String attrURI,
				String attrLName, String attrValue) {

			return new BlueprintException("no attribute : " + attrURI + "#"
					+ attrLName);
		}

		public static Exception _noElement(String uri, String localName) {
			return new BlueprintException("no element : " + uri + "#"
					+ localName);
		}

		public static Exception _parentNotAcceptChild(BPNode parent,
				BPElement child) {
			return new BlueprintException("parent not accept child : [parent]="
					+ parent + "; [child]=" + child);
		}

		public static Exception _elementNotAcceptText(BPNode element, String str) {
			return new BlueprintException(
					"element not accept text : [element]=" + element
							+ " [text]='" + str + "'");
		}
	}

	private class MyBuilderCore implements BPBuilder, ContentHandler {

		private ErrorHandler mErrorHandler;
		private ContentHandler mContentHandler;
		private final BPDocument mDoc;
		private final BPNode mRoot;
		private BPErrorHandler mBPErrorHdr;
		private BPElementProvider mBPElementProvider;
		private final Stack<BPNode> mStack = new MyStack(128);

		public MyBuilderCore(BPNode root) {

			if (root instanceof BPDocument) {
				this.mDoc = (BPDocument) root;
			} else {
				this.mDoc = root.getOwnerDocument();
			}
			this.mRoot = root;
		}

		@Override
		public ContentHandler getContentHandler() {
			ContentHandler hdl = this.mContentHandler;
			if (hdl == null) {
				hdl = new MyFrontContentHandler(this);
				this.mContentHandler = hdl;
			}
			return hdl;
		}

		@Override
		public ErrorHandler getErrorHandler() {
			ErrorHandler hdl = this.mErrorHandler;
			if (hdl == null) {
				hdl = new MyFrontErrorHandler(this);
				this.mErrorHandler = hdl;
			}
			return hdl;
		}

		@Override
		public BPElementProvider getBPElementProvider() {
			BPElementProvider hdr = this.mBPElementProvider;
			if (hdr == null) {
				hdr = new MyDefaultBPElementProvider(this);
				this.mBPElementProvider = hdr;
			}
			return hdr;
		}

		@Override
		public BPErrorHandler getBPErrorHandler() {
			BPErrorHandler hdr = this.mBPErrorHdr;
			if (hdr == null) {
				hdr = new MyDefaultBPErrorHandler(this);
				this.mBPErrorHdr = hdr;
			}
			return hdr;
		}

		@Override
		public BPNode getRootNode() {
			return this.mRoot;
		}

		@Override
		public void setBPElementProvider(BPElementProvider provider) {
			this.mBPElementProvider = provider;
		}

		@Override
		public void setBPErrorHandler(BPErrorHandler errorHandler) {
			this.mBPErrorHdr = errorHandler;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			boolean isEmpty = true;
			int p1 = start;
			int p2 = start + length - 1;
			for (; p1 <= p2;) {
				if (!this.isSpaceChar(ch[p1])) {
					isEmpty = false;
					break;
				}
				if (!this.isSpaceChar(ch[p2])) {
					isEmpty = false;
					break;
				}
				p1++;
				p2--;
			}
			if (isEmpty) {
				return;
			}

			if (this.mStack.size() > 0) {
				String str = new String(ch, start, length);
				BPNode ele = this.mStack.peek();
				BPText txt = this.mDoc.createText(str);
				boolean rlt = ele.appendChild(txt);
				if (!rlt) {
					Exception e = MyExceptionFactory._elementNotAcceptText(ele,
							str);
					this._onError(e);
				}
			}
		}

		private boolean isSpaceChar(char c) {
			switch (c) {
			case 0x0a:
			case 0x0d:
			case '\t':
			case ' ':
				return true;
			default:
				return false;
			}
		}

		@Override
		public void endDocument() throws SAXException {
			this.mStack.clear();

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			final BPElement child = (BPElement) this.mStack.pop();
			final BPNode parent = this.mStack.peek();

			if (parent == null) {
				return;
			}
			if (child == null) {
				return;
			}
			child.setParent(parent);
			child.tagEnd();

			boolean rlt = parent.appendChild(child);
			if (!rlt) {
				Exception e = MyExceptionFactory._parentNotAcceptChild(parent,
						child);
				rlt = parent.appendChild(child);// for debug
				this._onError(e);
			}
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
			// TODO Auto-generated method stub

		}

		@Override
		public void skippedEntity(String name) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void startDocument() throws SAXException {
			this.mStack.clear();
			this.mStack.push(this.mRoot);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {

			// System.out.println("element : " + qName);

			BPElement element = this.getBPElementProvider().createElement(
					this.mDoc, uri, localName);
			this.mStack.push(element);
			if (element == null) {
				Exception e = MyExceptionFactory._noElement(uri, localName);
				this._onError(e);
				return;
			}

			final int cntAttr = atts.getLength();
			for (int i = 0; i < cntAttr; i++) {
				String attrURI = atts.getURI(i);
				String attrLName = atts.getLocalName(i);
				String attrValue = atts.getValue(i);

				// System.out.println("        attr : " + attrLName);

				if (attrURI != null) {
					if (attrURI.length() < 1) {
						attrURI = null;
					}
				}

				BPAttribute attr = this.mDoc.createAttribute(attrURI,
						attrLName, attrValue);

				boolean rlt = element.setAttribute(attr);
				// rlt = true;
				if (!rlt) {
					Exception e = MyExceptionFactory._elementNotAcceptAttr(
							element, attrURI, attrLName, attrValue);
					this._onError(e);
				}
			}

			element.tagBegin();

		}

		private void _onError(Exception e) {
			try {
				this.getBPErrorHandler().error(e);
			} catch (Exception e1) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub

		}
	}

	private class MyFrontErrorHandler implements ErrorHandler {

		private final MyBuilderCore mCore;

		public MyFrontErrorHandler(MyBuilderCore core) {
			this.mCore = core;
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			try {
				this.mCore.getBPErrorHandler().error(e);
			} catch (Exception e1) {
				this._onFatalError(e1);
				throw e;
			}
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			try {
				this.mCore.getBPErrorHandler().fatalError(e);
			} catch (Exception e1) {
				this._onFatalError(e1);
				throw e;
			}
		}

		@Override
		public void warning(SAXParseException e) throws SAXException {
			try {
				this.mCore.getBPErrorHandler().warning(e);
			} catch (Exception e1) {
				this._onFatalError(e1);
				throw e;
			}
		}

		private void _onFatalError(Exception e1) {
			try {
				this.mCore.getBPErrorHandler().fatalError(e1);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

	}

	private class MyFrontContentHandler implements ContentHandler {

		private final MyBuilderCore mCore;

		public MyFrontContentHandler(MyBuilderCore core) {
			this.mCore = core;
		}

		private void _onError(Exception e) {
			try {
				this.mCore.getBPErrorHandler().fatalError(e);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			try {
				this.mCore.characters(ch, start, length);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			try {
				this.mCore.endDocument();
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			try {
				this.mCore.endElement(uri, localName, qName);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
			try {
				this.mCore.endPrefixMapping(prefix);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			try {
				this.mCore.ignorableWhitespace(ch, start, length);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
			try {
				this.mCore.processingInstruction(target, data);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			try {
				this.mCore.setDocumentLocator(locator);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
			try {
				this.mCore.skippedEntity(name);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void startDocument() throws SAXException {
			try {
				this.mCore.startDocument();
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {

			try {
				this.mCore.startElement(uri, localName, qName, atts);
			} catch (Exception e) {
				this._onError(e);
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			try {
				this.mCore.startPrefixMapping(prefix, uri);
			} catch (Exception e) {
				this._onError(e);
			}
		}

	}

	class MyDefaultBPElementProvider implements BPElementProvider {

		// private MyBuilderCore mCore;

		public MyDefaultBPElementProvider(MyBuilderCore core) {
			// this.mCore = core;
		}

		@Override
		public BPElement createElement(BPDocument doc, String uri,
				String localName) {
			return doc.createElement(uri, localName);
		}
	}

	class MyDefaultBPErrorHandler implements BPErrorHandler {

		public MyDefaultBPErrorHandler(MyBuilderCore myBuilderCore) {
		}

		@Override
		public void error(Exception e) throws Exception {
			System.err.print("[error] : ");
			e.printStackTrace(System.err);
			System.err.flush();

			throw e;// debug
		}

		@Override
		public void fatalError(Exception e) throws Exception {
			System.err.println("[fatalError]!");
			throw e;
		}

		@Override
		public void warning(Exception e) throws Exception {
			System.err.println("[warning] : " + e);
			System.err.flush();

			throw e;// debug
		}
	}
}
