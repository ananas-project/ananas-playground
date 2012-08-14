package ananas.lib.blueprint.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ananas.lib.blueprint.IBlueprintContext;
import ananas.lib.blueprint.IClass;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.IDocumentBuilder;
import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceRegistrar;
import ananas.lib.blueprint.io.IInputConnection;

final class ImplDocumentBuilder implements IDocumentBuilder {

	private final IBlueprintContext mContext;

	public ImplDocumentBuilder(IBlueprintContext context) {
		this.mContext = context;
	}

	@Override
	public IDocument build(InputStream is, String docURI) {
		try {
			return this._build(new MyBuildParam_Stream(is, docURI));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IDocument build(File file) {
		try {
			return this._build(new MyBuildParam_File(file));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IDocument build(String docURI) {
		try {
			return this._build(new MyBuildParam_URI(this.mContext, docURI));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IDocument _build(IBuildParam param) throws IOException {
		IDocument doc = null;
		try {
			doc = this.mContext.getImplementation().newDocument(
					param.getDocURI());
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(true);
			SAXParser parser = parserFactory.newSAXParser();
			MyCore dh = new MyCore(doc);
			parser.parse(param.getInputStream(), dh);
		} catch (Exception e) {
			e.printStackTrace();
			doc = null;
		} finally {
			param.close();
		}
		return doc;
	}

	interface IBuildParam {

		String getDocURI();

		void close() throws IOException;

		InputStream getInputStream();
	}

	class MyBuildParam_URI implements IBuildParam {

		private String mDocURI;
		private InputStream mIS;
		private IInputConnection mConn;

		public MyBuildParam_URI(IBlueprintContext context, String docURI)
				throws IOException {

			this.mDocURI = docURI;
			IInputConnection conn = (IInputConnection) context.getConnector()
					.open(docURI);
			this.mConn = conn;
			this.mIS = conn.getInputStream();
		}

		@Override
		public String getDocURI() {
			return this.mDocURI;
		}

		@Override
		public void close() {
			try {
				this.mIS.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				this.mConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public InputStream getInputStream() {
			return this.mIS;
		}
	}

	static class MyBuildParam_File implements IBuildParam {

		private InputStream mIS;
		private String mURI;

		public MyBuildParam_File(File file) throws FileNotFoundException {
			String uri = file.getAbsolutePath();
			if (uri.startsWith("/")) {
				uri = "file://" + uri;
			} else {
				uri = "file:///" + uri;
			}
			this.mURI = uri;
			this.mIS = new FileInputStream(file);
		}

		@Override
		public String getDocURI() {
			return this.mURI;
		}

		@Override
		public void close() throws IOException {
			this.mIS.close();
		}

		@Override
		public InputStream getInputStream() {
			return this.mIS;
		}

	}

	static class MyBuildParam_Stream implements IBuildParam {

		private String mURI;
		private InputStream mIS;

		public MyBuildParam_Stream(InputStream is, String docURI) {
			this.mIS = is;
			this.mURI = docURI;
		}

		@Override
		public String getDocURI() {
			return this.mURI;
		}

		@Override
		public void close() throws IOException {
			this.mIS.close();
		}

		@Override
		public InputStream getInputStream() {
			return this.mIS;
		}

	}

	private class MyCore extends DefaultHandler {

		private final Stack<IElement> mStack;
		private final IDocument mDoc;
		private Locator mLocator;
		private INamespaceRegistrar mNSReg;

		public MyCore(IDocument doc) {
			this.mDoc = doc;
			this.mStack = new Stack<IElement>();
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			if (this.mStack.isEmpty()) {
				return;
			}
			final IElement element = this.mStack.peek();

			int from = start;
			int to = start + length;
			for (; from < to; from++) {
				final char c = ch[from];
				if (!this._isSpace(c))
					break;
			}
			for (; from < to; to--) {
				final char c = ch[to - 1];
				if (!this._isSpace(c))
					break;
			}
			if (from >= to)
				return;

			String text = new String(ch, start, length);
			boolean rlt = element.appendText(text);
			if (!rlt) {
				String msg = "The ELEMENT not accept the TEXT";
				System.err.println(msg + this._stringOfLocator());
				System.err.println("    " + "element = " + element);
				System.err.println("    " + "text    = " + text);
				throw new SAXException(msg);
			}

		}

		private boolean _isSpace(char c) {
			switch (c) {
			case 0:
			case ' ':
			case 0x09:
			case 0x0a:
			case 0x0d:
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

			final IElement child = this.mStack.pop();
			child.tagEnd();
			if (this.mStack.isEmpty()) {
				this.mDoc.setRootElement(child);
				child.setParent(null);
			} else {
				final IElement parent = this.mStack.peek();
				child.setParent(parent);
				boolean rlt = parent.appendChild(child);
				if (!rlt) {
					String msg = "The PARENT not accept the CHILD";
					System.err.println(msg + this._stringOfLocator());
					System.err.println("    " + "parent = " + parent);
					System.err.println("    " + "child  = " + child);
					throw new SAXException(msg);
				}
			}
		}

		private String _stringOfLocator() {
			Locator locator = this.mLocator;
			if (locator == null)
				return "";
			return ("(line:" + locator.getLineNumber() + ", col:"
					+ locator.getColumnNumber() + ")");
		}

		@Override
		public void error(SAXParseException exception) throws SAXException {
			System.err.println("error:" + exception);
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			this.mLocator = locator;
		}

		@Override
		public void startDocument() throws SAXException {
			this.mStack.clear();
			this.mDoc.setRootElement(null);
			this.mNSReg = this.mDoc.getImplementation().getNamespaceRegistrar();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attr) throws SAXException {

			INamespace ns = this.mNSReg.getNamespace(uri);
			if (ns == null) {
				String msg = "No Namespace";
				System.err.println(msg + this._stringOfLocator());
				System.err.println("    " + "Namespace-URI = " + uri);
				System.err.println("    " + "Local-Name    = " + localName);
				throw new SAXException(msg);
			}
			IClass cls = ns.findClass(localName);
			if (cls == null) {
				String msg = "No Class";
				System.err.println(msg + this._stringOfLocator());
				System.err.println("    " + "Namespace-URI = " + uri);
				System.err.println("    " + "Local-Name    = " + localName);
				throw new SAXException(msg);
			}
			IElement element = cls.createElement(this.mDoc);

			this.mStack.push(element);
			element.tagBegin();

			final int len = attr.getLength();
			for (int i = 0; i < len; i++) {
				String attrURI = attr.getURI(i);
				String name = attr.getLocalName(i);
				String value = attr.getValue(i);
				boolean rlt = element.setAttribute(attrURI, name, value);
				if (!rlt) {
					String msg = "The element not accept the attribute";
					System.err.println(msg + this._stringOfLocator());
					System.err.println("    " + "element   = " + element);
					System.err.println("    " + "attrName  = " + attrURI + ":"
							+ name);
					System.err.println("    " + "attrValue = " + value);
					throw new SAXException(msg);
				}
			}

		}

		@Override
		public void warning(SAXParseException exception) throws SAXException {
			System.err.println("warning:" + exception);
		}
	}

	@Override
	public IBlueprintContext getBlueprintContext() {
		return this.mContext;
	}
}
