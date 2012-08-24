package ananas.lib.servkit.json.io;

import java.io.IOException;
import java.io.InputStream;

import ananas.lib.servkit.json.JsonException;

class Impl_JsonParser implements IJsonParser {

	private final IMyReader mReader;
	private final IMyLocator mLocator;
	private final StringBuilder mStringBuilder;

	public Impl_JsonParser() {
		this.mLocator = new MyLocator();
		this.mReader = new MyReader(this.mLocator);
		this.mStringBuilder = new StringBuilder(128);
	}

	@Override
	public void parse(InputStream is, IJsonHandler h) {
		this.mReader.bindInputStream(is);
		try {

			h.onDocumentPrepare();
			h.onSetLocator(this.mLocator);

			this.mReader.readChar();
			h.onDocumentBegin();
			this._parseValue(this.mReader, h);
			h.onDocumentEnd();
		} catch (Exception e) {
			h.onException(e);
		} finally {
			h.onDocumentFinal();
		}
		this.mReader.bindInputStream(null);
	}

	private void _parseValue(IMyReader reader, IJsonHandler h)
			throws IOException {

		reader.skipSpace();
		final int ch = reader.getChar();
		if (ch < 0) {
			// EOF
			return;
		}
		switch (ch) {
		case '{': {
			// object
			this._parseObject(reader, h);
			break;
		}
		case '[': {
			// array
			this._parseArray(reader, h);
			break;
		}
		case '"': {
			// string
			this._parseString(reader, h);
			break;
		}
		case 'n': {
			// null
			this._parseFalseNullTrue(reader, "null", h);
			break;
		}
		case 't': {
			// true
			this._parseFalseNullTrue(reader, "true", h);
			break;
		}
		case 'f': {
			// false
			this._parseFalseNullTrue(reader, "false", h);
			break;
		}
		default: {
			// number
			this._parseNumber(reader, h);
			break;
		}
		}

	}

	private void _parseNumber(IMyReader reader, IJsonHandler h)
			throws IOException {

		final StringBuilder sb = this._getStringBuilder();
		int iE = -1;
		boolean hasDot = false;

		for (int i = 0;; i++) {
			int ch = reader.getChar();
			boolean isNumber = false;
			switch (ch) {
			case '-':
			case '+': {
				isNumber = true;
				break;
			}
			case '.': {
				isNumber = true;
				hasDot = true;
				break;
			}
			case 'e':
			case 'E': {
				isNumber = true;
				if (iE < 0) {
					iE = i;
				}
				break;
			}
			default:
				if ('0' <= ch && ch <= '9') {
					isNumber = true;
				}
			}
			if (!isNumber) {
				break;
			}
			ch = reader.readChar();
			sb.append((char) ch);
		}

		if (iE < 0) {
			// no 'e'
			if (hasDot) {
				double n = Double.parseDouble(sb.toString());
				h.onDouble(n);
			} else {
				long n = Long.parseLong(sb.toString());
				if (Integer.MIN_VALUE <= n && n <= Integer.MAX_VALUE) {
					h.onInteger((int) n);
				} else {
					h.onLong(n);
				}
			}
		} else {
			String s1 = sb.substring(0, iE);
			String s2 = sb.substring(iE + 1);
			double part1 = Double.parseDouble(s1);
			int part2 = Integer.parseInt(s2);
			double n = part1 * Math.pow(10, part2);
			h.onDouble(n);
		}

	}

	private void _parseString(IMyReader reader, IJsonHandler h)
			throws IOException {

		String str = this._parseStringImpl(reader);
		h.onString(str);
	}

	private void _parseArray(IMyReader reader, IJsonHandler h)
			throws IOException {

		reader.readAndCheck('[');
		h.onArrayBegin();

		reader.skipSpace();
		int ch = reader.getChar();

		for (; ch != ']';) {

			reader.skipSpace();
			this._parseValue(reader, h);

			reader.skipSpace();
			ch = reader.getChar();
			if (ch == ',') {
				reader.readAndCheck(',');
			}
		}

		reader.readAndCheck(']');
		h.onArrayEnd();

	}

	private void _parseObject(IMyReader reader, IJsonHandler h)
			throws IOException {

		reader.readAndCheck('{');
		h.onObjectBegin();

		reader.skipSpace();
		int ch = reader.getChar();

		for (; ch != '}';) {

			reader.skipSpace();
			this._parseObjectKey(reader, h);

			reader.skipSpace();
			reader.readAndCheck(':');

			reader.skipSpace();
			this._parseValue(reader, h);

			reader.skipSpace();
			ch = reader.getChar();
			if (ch == ',') {
				reader.readAndCheck(',');
			}
		}

		reader.readAndCheck('}');
		h.onObjectEnd();
	}

	private void _parseObjectKey(IMyReader reader, IJsonHandler h)
			throws IOException {
		String str = this._parseStringImpl(reader);
		h.onObjectKey(str);
	}

	private String _parseStringImpl(IMyReader reader) throws IOException {
		final StringBuilder sb = this._getStringBuilder();
		reader.readAndCheck('"');
		boolean isEscape = false;
		for (;;) {
			final char ch = (char) reader.readChar();
			if (isEscape) {
				isEscape = false;
				char ch2;
				switch (ch) {
				case 'b':
					ch2 = '\b';
					break;
				case 'f':
					ch2 = '\f';
					break;
				case 'n':
					ch2 = '\n';
					break;
				case 'r':
					ch2 = '\r';
					break;
				case 't':
					ch2 = '\t';
					break;
				case 'u': {
					int u0 = this._charToDigitHex(reader.readChar()) * 0x1000;
					int u1 = this._charToDigitHex(reader.readChar()) * 0x0100;
					int u2 = this._charToDigitHex(reader.readChar()) * 0x0010;
					int u3 = this._charToDigitHex(reader.readChar()) * 0x0001;
					ch2 = (char) (u0 | u1 | u2 | u3);
					break;
				}
				// case '"':
				// case '\\':
				// case '/':
				default:
					ch2 = (char) ch;
				}
				sb.append(ch2);
			} else {
				if (ch == '"') {
					break;
				} else if (ch == '\\') {
					isEscape = true;
				} else {
					sb.append((char) ch);
				}
			}
		}
		// reader.readAndCheck('"');
		return sb.toString();
	}

	private int _charToDigitHex(int ch) throws JsonException {
		// char ch0 = (char) ch;
		int ret;
		if ('0' <= ch && ch <= '9') {
			ret = ch - '0';
		} else if ('a' <= ch && ch <= 'f') {
			ret = ch - 'a' + 10;
		} else if ('A' <= ch && ch <= 'F') {
			ret = ch - 'A' + 10;
		} else {
			throw MyExceptions.defaultException;
		}
		return ret;
	}

	private StringBuilder _getStringBuilder() {
		StringBuilder sb = this.mStringBuilder;
		sb.setLength(0);
		return sb;
	}

	private void _parseFalseNullTrue(IMyReader reader, String text,
			IJsonHandler h) throws IOException {

		final int ch0 = reader.getChar();
		int len = text.length();
		for (int i = 0; i < len; i++) {
			char ch = text.charAt(i);
			reader.readAndCheck(ch);
		}

		switch (ch0) {
		case 't':
			h.onBoolean(true);
			break;
		case 'f':
			h.onBoolean(false);
			break;
		case 'n':
			h.onNull();
			break;
		default:
			throw MyExceptions.defaultException;
		}
	}

	private static interface IMyReader {

		void bindInputStream(InputStream is);

		void readAndCheck(char c) throws IOException;

		void skipSpace() throws IOException;

		int readChar() throws IOException;

		int getChar();

	}

	private static interface IMyLocator extends IJsonLocator {

		void reset();

		void putChar(char ch);

	}

	static class MyReader implements IMyReader {

		private final IMyLocator mLocator;
		private InputStream mIS;
		private int mNextChar;

		public MyReader(IMyLocator locator) {
			this.mLocator = locator;

		}

		@Override
		public void bindInputStream(InputStream is) {

			this.mIS = is;
			if (is != null) {
				this.mLocator.reset();
			}
		}

		@Override
		public int readChar() throws IOException {

			// TODO to support utf-8
			final int oldChar = this.mNextChar;
			final int newChar = this.mIS.read();
			if (oldChar < 0 && newChar < 0) {
				throw MyExceptions.defaultException;
			}
			this.mNextChar = newChar;
			this.mLocator.putChar((char) oldChar);
			return oldChar;
		}

		@Override
		public void skipSpace() throws IOException {
			for (;;) {
				int ch = this.getChar();
				switch (ch) {
				case 0x09:
				case 0x0a:
				case 0x0d:
				case ' ':
					// skip
					this.readChar();
					break;
				default:
					return;
				}
			}
		}

		@Override
		public int getChar() {
			return this.mNextChar;
		}

		@Override
		public void readAndCheck(char c) throws IOException {
			int ch = this.readChar();
			if (c != ch) {
				throw MyExceptions.defaultException;
			}
		}

	}

	static class MyExceptions {

		static final JsonException defaultException = new JsonException();

	}

	static class MyLocator implements IMyLocator {

		private int mCol;
		private int mLine;
		private int mCnt0x0a;
		private int mCnt0x0d;

		@Override
		public void reset() {
			this.mCol = 0;
			this.mLine = 0;
			this.mCnt0x0a = 0;
			this.mCnt0x0d = 0;
		}

		@Override
		public int getLine() {
			return this.mLine + 1;
		}

		@Override
		public int getColumn() {
			return this.mCol + 1;
		}

		@Override
		public void putChar(char ch) {

			final boolean isNewLine;

			switch (ch) {

			case 0x0a:
				this.mCnt0x0a++;
				isNewLine = true;
				break;

			case 0x0d:
				this.mCnt0x0d++;
				isNewLine = true;
				break;

			default:
				isNewLine = false;
				break;
			}

			if (isNewLine) {
				this.mLine = (this.mCnt0x0a < this.mCnt0x0d) ? this.mCnt0x0d
						: this.mCnt0x0a;
				this.mCol = 0;
			} else {
				this.mCol++;
			}

		}

	}

}
