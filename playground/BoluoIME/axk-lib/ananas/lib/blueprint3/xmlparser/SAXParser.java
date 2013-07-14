package ananas.lib.blueprint3.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class SAXParser implements XMLReader {

	private EntityResolver mEntityResolver;
	private ErrorHandler mErrorHandler;
	private DTDHandler mDTDHandler;
	private ContentHandler mContentHandler;

	@Override
	public ContentHandler getContentHandler() {
		return this.mContentHandler;
	}

	@Override
	public DTDHandler getDTDHandler() {
		return this.mDTDHandler;
	}

	@Override
	public EntityResolver getEntityResolver() {
		return this.mEntityResolver;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return this.mErrorHandler;
	}

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		return false;
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		return null;
	}

	@Override
	public void parse(InputSource input) throws IOException, SAXException {

		InputStream in = input.getByteStream();
		IBufferedReader rdr = new MyCharReader(in);

		MyReaderHandler h = new MyReaderHandler();
		h.mContentHandler = new TheContentHandlerProxy(this.mContentHandler);
		h.mErrorHandler = this.mErrorHandler;
		h.mDTDHandler = this.mDTDHandler;
		h.mEntityResolver = this.mEntityResolver;

		IReaderHelper hp = new MyReaderHelper(rdr, h);

		IReaderEngine engine = new MyReaderEngine();

		try {
			engine.parse(hp);
		} catch (Exception e) {

			Locator loc = null;
			SAXParseException spe = new SAXParseException(e.getMessage(), loc,
					e);
			h.getErrorHandler().fatalError(spe);

			if (e instanceof SAXException) {
				throw (SAXException) e;
			} else if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	static interface ITextWriter {

		void flush() throws SAXException;

		void write(int ch) throws SAXException;
	}

	static interface ICharBuffer {

		char[] getBuffer();

		int getLength();

		int getOffset();
	}

	static interface IBufferedReader {

		int read() throws IOException;

		void pushBack(int ch);

		ICharBuffer getString(int length) throws IOException, SAXException;

	}

	static interface ITextWriterType {

		ITextWriterType other = new MyTextWriterType("other");
		ITextWriterType text = new MyTextWriterType("text");
		ITextWriterType comment = new MyTextWriterType("comment");
		ITextWriterType pi = new MyTextWriterType("pi");
	}

	static class MyTextWriterType implements ITextWriterType {

		private final String mText;

		public MyTextWriterType(String string) {
			this.mText = string;
		}

		public String toString() {
			return this.mText;
		}
	}

	static interface IReaderHelper {

		IReaderHandler getHandler();

		IBufferedReader getReader();

		IStringBuffer getStringBuffer();

		ITextWriter getTextWriter(ITextWriterType type);

		boolean isEOF();

		void readUntil(char c, ITextWriter writer) throws IOException,
				SAXException;

		String readQName() throws IOException, SAXException;

		void skipSpace() throws IOException;

		boolean readAndCheck(char c, boolean doThrow) throws IOException,
				SAXException;

		boolean readAndCheck(String string, boolean doThrow)
				throws IOException, SAXException;

		boolean getAndCheck(String string) throws IOException, SAXException;

		int readChar() throws IOException;

		int getChar() throws IOException;
	}

	static interface IReaderEngine {

		void parse(IReaderHelper helper) throws IOException, SAXException;

	}

	static interface IAttributeList {

		void reset();

		Attributes getTarget();

		void appendAttr(String qName, String value);

	}

	static interface IStringBuffer {

		void reset();

		ITextWriter getWriter();

		String getString();

	}

	static interface IReaderHandler {

		ContentHandler getContentHandler();

		DTDHandler getDTDHandler();

		EntityResolver getEntityResolver();

		ErrorHandler getErrorHandler();
	}

	static class MyReaderHandler implements IReaderHandler {

		private ErrorHandler mErrorHandler;
		private EntityResolver mEntityResolver;
		private DTDHandler mDTDHandler;
		private ContentHandler mContentHandler;

		@Override
		public ContentHandler getContentHandler() {
			return this.mContentHandler;
		}

		@Override
		public DTDHandler getDTDHandler() {
			return this.mDTDHandler;
		}

		@Override
		public EntityResolver getEntityResolver() {
			return this.mEntityResolver;
		}

		@Override
		public ErrorHandler getErrorHandler() {
			return this.mErrorHandler;
		}
	}

	@Override
	public void parse(String systemId) throws IOException, SAXException {
		this.parse(new InputSource(systemId));
	}

	@Override
	public void setContentHandler(ContentHandler handler) {
		this.mContentHandler = handler;
	}

	@Override
	public void setDTDHandler(DTDHandler handler) {
		this.mDTDHandler = handler;
	}

	@Override
	public void setEntityResolver(EntityResolver resolver) {
		this.mEntityResolver = resolver;
	}

	@Override
	public void setErrorHandler(ErrorHandler handler) {
		this.mErrorHandler = handler;
	}

	@Override
	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
	}

	@Override
	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
	}

	static class MyReaderEngine implements IReaderEngine {

		private IAttributeList mAttrList;

		@Override
		public void parse(IReaderHelper helper) throws IOException,
				SAXException {

			ContentHandler hCont = helper.getHandler().getContentHandler();
			hCont.startDocument();
			for (;;) {
				if (helper.isEOF()) {
					break;
				}
				this.parseText(helper);
				if (helper.isEOF()) {
					break;
				}
				this.parseOther(helper);
			}
			hCont.endDocument();
		}

		private void parseOther(IReaderHelper helper) throws IOException,
				SAXException {
			final IBufferedReader rdr = helper.getReader();
			final ICharBuffer head = rdr.getString(3);
			final char[] buff = head.getBuffer();
			final int off = head.getOffset();
			final int len = head.getLength();
			if (len != 3) {
				throw new SAXException("...");
			}
			final int ch1 = buff[off + 1];
			if (ch1 == '/') {
				// "</"
				this.parseElementEnd(helper);
			} else if (ch1 == '?') {
				// "<?"
				this.parsePI(helper);
			} else if (ch1 == '!') {
				// "<!"
				final int ch2 = buff[off + 2];
				if (ch2 == '[') {
					// "<!["
					this.parseCDATA(helper);
				} else if (ch2 == '-') {
					// "<!-"
					this.parseComment(helper);
				} else {
					throw new SAXException("...");
				}
			} else {
				// "<xxx"
				this.parseElementBegin(helper);
			}
		}

		private void parsePI(IReaderHelper helper) throws IOException,
				SAXException {
			ITextWriter writer = helper.getTextWriter(ITextWriterType.pi);
			helper.readAndCheck("<?", true);
			for (; !helper.isEOF();) {
				helper.readUntil('?', writer);
				if (helper.getAndCheck("?>")) {
					// ok, the end
					helper.readAndCheck("?>", true);
					writer.flush();
					return;
				} else {
					// not end
					writer.write(helper.readChar());
				}
			}
			throw new SAXException("...");
		}

		private void parseComment(IReaderHelper helper) throws IOException,
				SAXException {
			ITextWriter writer = helper.getTextWriter(ITextWriterType.comment);
			helper.readAndCheck("<!--", true);
			for (; !helper.isEOF();) {
				helper.readUntil('-', writer);
				if (helper.getAndCheck("-->")) {
					// ok, the end
					helper.readAndCheck("-->", true);
					writer.flush();
					return;
				} else {
					// not end
					writer.write(helper.readChar());
				}
			}
			throw new SAXException("...");
		}

		private void parseCDATA(IReaderHelper helper) throws IOException,
				SAXException {
			ITextWriter writer = helper.getTextWriter(ITextWriterType.text);
			helper.readAndCheck("<![CDATA[", true);
			for (; !helper.isEOF();) {
				helper.readUntil(']', writer);
				if (helper.getAndCheck("]]>")) {
					// ok, the end
					helper.readAndCheck("]]>", true);
					writer.flush();
					return;
				} else {
					// not end
					writer.write(helper.readChar());
				}
			}
			throw new SAXException("...");
		}

		private void parseElementEnd(IReaderHelper helper) throws SAXException,
				IOException {
			helper.readAndCheck("</", true);
			String qname = helper.readQName();
			helper.skipSpace();
			helper.readAndCheck('>', true);
			helper.getHandler().getContentHandler()
					.endElement(null, null, qname);
		}

		private void parseElementBegin(IReaderHelper helper)
				throws SAXException, IOException {
			helper.readAndCheck('<', true);
			final String qName = helper.readQName();
			boolean isClosed = false;
			boolean isOk = false;
			final IAttributeList attrList = this.getAttrList();
			for (; !helper.isEOF();) {
				helper.skipSpace();
				int ch0 = helper.getChar();
				if (ch0 == '/') {
					helper.readAndCheck("/>", true);
					isClosed = true;
					isOk = true;
					break;
				} else if (ch0 == '>') {
					helper.readAndCheck('>', true);
					isOk = true;
					break;
				} else {
					String attrQName = helper.readQName();
					helper.skipSpace();
					helper.readAndCheck('=', true);
					helper.skipSpace();
					final char c1 = (char) helper.readChar();
					IStringBuffer sb = helper.getStringBuffer();
					helper.readUntil(c1, sb.getWriter());
					helper.readAndCheck(c1, true);
					String attrValue = sb.getString();
					attrList.appendAttr(attrQName, attrValue);
				}
			}
			if (!isOk) {
				throw new SAXException("...");
			}
			ContentHandler hCont = helper.getHandler().getContentHandler();
			hCont.startElement(null, null, qName, attrList.getTarget());
			if (isClosed) {
				hCont.endElement(null, null, qName);
			}
		}

		private IAttributeList getAttrList() {
			IAttributeList al = this.mAttrList;
			if (al == null) {
				al = new MyAttrList();
				this.mAttrList = al;
			}
			al.reset();
			return al;
		}

		private void parseText(IReaderHelper helper) throws IOException,
				SAXException {
			ITextWriter wtr = helper.getTextWriter(ITextWriterType.text);
			helper.readUntil('<', wtr);
			wtr.flush();
		}
	}

	static class MyReaderHelper implements IReaderHelper {

		private final IReaderHandler mHandler;
		private final IBufferedReader mReader;
		private boolean mIsEOF;

		public MyReaderHelper(IBufferedReader rdr, IReaderHandler h) {
			this.mHandler = h;
			this.mReader = rdr;
		}

		@Override
		public IReaderHandler getHandler() {
			return this.mHandler;
		}

		@Override
		public IBufferedReader getReader() {
			return this.mReader;
		}

		@Override
		public boolean isEOF() {
			return this.mIsEOF;
		}

		@Override
		public void readUntil(char c, ITextWriter writer) throws IOException,
				SAXException {
			for (;;) {
				int ch = this.mReader.read();
				if (ch < 0) {
					this.mIsEOF = true;
					break;
				} else if (ch == c) {
					this.mReader.pushBack(ch);
					// writer.flush();
					break;
				} else {
					writer.write(ch);
				}
			}
		}

		@Override
		public ITextWriter getTextWriter(ITextWriterType type) {
			if (type == ITextWriterType.text) {
				return this.getTextWriterForText();
			} else if (type == ITextWriterType.other) {
				return this.getTextWriterForOther();
			} else {
				return this.getTextWriterForOther();
			}
		}

		ITextWriter mWriterForText;
		ITextWriter mWriterForOther;

		private ITextWriter getTextWriterForOther() {
			ITextWriter wtr = this.mWriterForOther;
			if (wtr == null) {
				wtr = new ITextWriter() {

					@Override
					public void flush() {
						// do nothing
					}

					@Override
					public void write(int ch) throws SAXException {
						// do nothing
					}
				};
				this.mWriterForOther = wtr;
			}
			return wtr;
		}

		private ITextWriter getTextWriterForText() {
			ITextWriter wtr = this.mWriterForText;
			if (wtr == null) {
				wtr = new ITextWriter() {

					final char[] mBuffer = new char[1024 * 4];
					int mLength;

					@Override
					public void flush() throws SAXException {
						if (this.mLength > 0) {
							ContentHandler hCont = MyReaderHelper.this.mHandler
									.getContentHandler();
							hCont.characters(this.mBuffer, 0, this.mLength);
							this.mLength = 0;
						}
					}

					@Override
					public void write(int ch) throws SAXException {
						if (this.mLength >= this.mBuffer.length) {
							this.flush();
						}
						this.mBuffer[this.mLength++] = (char) ch;
					}

				};
				this.mWriterForText = wtr;
			}
			return wtr;
		}

		@Override
		public String readQName() throws IOException, SAXException {
			IStringBuffer sb = this.getStringBuffer();
			for (;;) {
				int ch = this.mReader.read();
				if (this.isQNameChar(ch)) {
					sb.getWriter().write(ch);
				} else {
					this.mReader.pushBack(ch);
					break;
				}
			}
			return sb.getString();
		}

		private boolean isQNameChar(int ch) {
			switch (ch) {
			case ':':
			case '-':
			case '_':
				return true;
			default:
				if ('0' <= ch && ch <= '9') {
					return true;
				} else if ('a' <= ch && ch <= 'z') {
					return true;
				} else if ('A' <= ch && ch <= 'Z') {
					return true;
				} else {
					return false;
				}
			}
		}

		IStringBuffer mStrBuff;

		public IStringBuffer getStringBuffer() {
			IStringBuffer sb = this.mStrBuff;
			if (sb == null) {
				sb = new MyStringBuffer(1024 * 4);
				this.mStrBuff = sb;
			}
			sb.reset();
			return sb;
		}

		@Override
		public void skipSpace() throws IOException {
			for (;;) {
				int ch = this.mReader.read();
				if (this.isSpaceChar(ch)) {
					// continue
				} else {
					this.mReader.pushBack(ch);
					break;
				}
			}
		}

		private boolean isSpaceChar(int ch) {
			switch (ch) {
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
		public boolean readAndCheck(char c, boolean doThrow)
				throws IOException, SAXException {
			int ch2 = this.mReader.read();
			if (ch2 < 0) {
				this.mIsEOF = true;
			}
			boolean rlt = (ch2 == c);
			if ((!rlt) && doThrow) {
				throw new SAXException("...");
			}
			return rlt;
		}

		@Override
		public boolean readAndCheck(String string, boolean doThrow)
				throws IOException, SAXException {
			boolean rlt = true;
			int len = string.length();
			for (int i = 0; i < len; i++) {
				char ch1 = string.charAt(i);
				int ch2 = this.mReader.read();
				if (ch1 != ch2) {
					rlt = false;
				}
			}
			if ((!rlt) && doThrow) {
				throw new SAXException("...");
			}
			return rlt;
		}

		@Override
		public boolean getAndCheck(String string) throws IOException,
				SAXException {
			final int len = string.length();
			final ICharBuffer str = this.mReader.getString(len);
			if (str.getLength() != len) {
				return false;
			}
			boolean rlt = true;
			final char[] buff = str.getBuffer();
			final int off = str.getOffset();
			for (int i = 0; i < len; i++) {
				final char ch1 = string.charAt(i);
				final char ch2 = buff[off + i];
				if (ch1 != ch2) {
					rlt = false;
					break;
				}
			}
			return rlt;
		}

		@Override
		public int readChar() throws IOException {
			return this.mReader.read();
		}

		@Override
		public int getChar() throws IOException {
			int ch = this.mReader.read();
			this.mReader.pushBack(ch);
			if (ch < 0) {
				this.mIsEOF = true;
			}
			return ch;
		}
	}

	static class MyStringBuffer implements IStringBuffer, ITextWriter {

		private final char[] mBuffer;
		private int mLength;

		public MyStringBuffer(int cap) {
			this.mBuffer = new char[cap];
		}

		@Override
		public void reset() {
			this.mLength = 0;
		}

		@Override
		public String getString() {
			if (this.mLength <= 0) {
				return "";
			}
			return new String(this.mBuffer, 0, this.mLength);
		}

		@Override
		public ITextWriter getWriter() {
			return this;
		}

		@Override
		public void flush() throws SAXException {
		}

		@Override
		public void write(int ch) throws SAXException {
			this.mBuffer[this.mLength++] = (char) ch;
		}
	}

	static class MyCharReader implements IBufferedReader, ICharBuffer {

		private final Reader mReader;
		private final char[] mBuffer = new char[1024 * 4];
		private int mOffset;
		private int mLength;
		// results
		private int mResultOffset;
		private int mResultLength;
		private char[] mResultBuffer;

		public MyCharReader(InputStream in) {
			this.mReader = new MyUTF8StreamReader(in);
		}

		@Override
		public int read() throws IOException {
			if (this.mLength > 0) {
				this.mLength--;
				return this.mBuffer[this.mOffset++];
			} else {
				return this.mReader.read();
			}
		}

		@Override
		public void pushBack(int ch) {
			if (this.mOffset <= 0) {
				// move data
				this.moveData(100);
			}
			this.mBuffer[--this.mOffset] = (char) ch;
			this.mLength++;
		}

		private void moveData(final int off) {
			final int oldOff = this.mOffset;
			int newOff = oldOff + off;
			if (newOff + this.mLength > this.mBuffer.length) {
				newOff = this.mBuffer.length - this.mLength;
			}
			if (newOff < 0) {
				newOff = 0;
			}
			if (newOff < oldOff) {
				// <<<
				final int len = this.mLength;
				for (int i = 0; i < len; i++) {
					this.mBuffer[newOff + i] = this.mBuffer[oldOff + i];
				}
			} else if (newOff > oldOff) {
				// >>>
				for (int i = this.mLength - 1; i >= 0; i--) {
					this.mBuffer[newOff + i] = this.mBuffer[oldOff + i];
				}
			}
			this.mOffset = newOff;
		}

		@Override
		public ICharBuffer getString(int length) throws IOException,
				SAXException {

			if (length > (this.mBuffer.length - this.mOffset)) {
				// move data
				this.moveData(0 - length);
			}
			for (; this.mLength < length;) {
				int ch = this.mReader.read();
				this.mBuffer[this.mOffset + this.mLength] = (char) ch;
				this.mLength++;
			}
			this.mResultBuffer = this.mBuffer;
			this.mResultLength = length;
			this.mResultOffset = this.mOffset;
			return this;
		}

		@Override
		public char[] getBuffer() {
			return this.mResultBuffer;
		}

		@Override
		public int getLength() {
			return this.mResultLength;
		}

		@Override
		public int getOffset() {
			return this.mResultOffset;
		}
	}

	static class MyAttrList implements IAttributeList, Attributes {

		final List<String> mQNameList = new ArrayList<String>();
		final List<String> mValueList = new ArrayList<String>();

		@Override
		public void reset() {
			this.mQNameList.clear();
			this.mValueList.clear();
		}

		@Override
		public Attributes getTarget() {
			return this;
		}

		@Override
		public void appendAttr(String qName, String value) {
			if ((qName == null) || (value == null)) {
				throw new RuntimeException("...");
			}
			this.mQNameList.add(qName);
			this.mValueList.add(value);
		}

		@Override
		public int getIndex(String arg0) {
			throw new RuntimeException("no impl");
		}

		@Override
		public int getIndex(String arg0, String arg1) {
			throw new RuntimeException("no impl");
		}

		@Override
		public int getLength() {
			return this.mQNameList.size();
		}

		@Override
		public String getLocalName(int arg0) {
			throw new RuntimeException("no impl");

		}

		@Override
		public String getQName(int index) {
			return this.mQNameList.get(index);
		}

		@Override
		public String getType(int arg0) {
			throw new RuntimeException("no impl");
		}

		@Override
		public String getType(String arg0) {
			throw new RuntimeException("no impl");
		}

		@Override
		public String getType(String arg0, String arg1) {
			throw new RuntimeException("no impl");
		}

		@Override
		public String getURI(int arg0) {
			throw new RuntimeException("no impl");
		}

		@Override
		public String getValue(int index) {
			return this.mValueList.get(index);
		}

		@Override
		public String getValue(String arg0) {
			throw new RuntimeException("no impl");
		}

		@Override
		public String getValue(String arg0, String arg1) {
			throw new RuntimeException("no impl");
		}
	}

}
