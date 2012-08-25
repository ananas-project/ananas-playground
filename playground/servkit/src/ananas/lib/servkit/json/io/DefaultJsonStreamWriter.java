package ananas.lib.servkit.json.io;

import java.io.IOException;
import java.io.OutputStream;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.object.IJsonValue;

public class DefaultJsonStreamWriter implements IJsonSerializer {

	private final MyHandler mHandler;
	private final MyWriter mWriter = new MyWriter();

	public DefaultJsonStreamWriter() {
		mHandler = new MyHandler(this.mWriter);
	}

	@Override
	public void serialize(OutputStream os, IJsonValue json)
			throws JsonException {

		this.mWriter.mOS = os;

		MyHandler h = this.mHandler;
		h.onDocumentBegin();
		json.output(h);
		h.onDocumentEnd();

		this.mWriter.mOS = null;
	}

	interface IMyWriter {

		void writeString(String s);

		void writeChar(char ch);

	}

	static class MyWriter implements IMyWriter {

		OutputStream mOS;

		@Override
		public void writeString(String s) {
			int len = s.length();
			for (int i = 0; i < len; i++) {
				this.writeChar(s.charAt(i));
			}
		}

		@Override
		public void writeChar(char ch) {
			try {
				this.mOS.write(ch);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class MyHandler implements IJsonHandler {

		private final IMyWriter mWriter;
		private char mPrevToken;

		public MyHandler(IMyWriter writer) {
			this.mWriter = writer;
		}

		@Override
		public void onDocumentPrepare() {
			this.mPrevToken = 0;
		}

		@Override
		public void onSetLocator(IJsonLocator locator) {
		}

		@Override
		public void onDocumentBegin() {
			this.mPrevToken = 0;
		}

		@Override
		public void onDocumentEnd() {
		}

		@Override
		public void onDocumentFinal() {
		}

		@Override
		public void onException(Exception exception) {
		}

		@Override
		public void onArrayBegin() {
			this._tryWriteElementSpliter();
			this.mWriter.writeChar('[');
			this.mPrevToken = '[';
		}

		@Override
		public void onArrayEnd() throws JsonException {
			this.mWriter.writeChar(']');
			this.mPrevToken = ']';
		}

		@Override
		public void onObjectBegin() throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeChar('{');
			this.mPrevToken = '{';
		}

		@Override
		public void onObjectEnd() throws JsonException {
			this.mWriter.writeChar('}');
			this.mPrevToken = '}';
		}

		@Override
		public void onObjectKey(String key) throws JsonException {
			this._tryWriteElementSpliter();
			this._writeJsonString(key);
			this.mWriter.writeChar(':');
			this.mPrevToken = ':';
		}

		private void _tryWriteElementSpliter() {

			switch (this.mPrevToken) {
			case 0:
			case '{':
			case '[':
			case ':':
				break;
			default:
				this.mWriter.writeChar(',');
			}
		}

		private void _writeJsonString(String s) {
			this.mWriter.writeChar('"');
			int len = s.length();
			for (int i = 0; i < len; i++) {
				char ch = s.charAt(i);
				char escape = 0;
				switch (ch) {
				case '\b':
					escape = 'b';
					break;
				case '\f':
					escape = 'f';
					break;
				case '\n':
					escape = 'n';
					break;
				case '\r':
					escape = 'r';
					break;
				case '\t':
					escape = 't';
					break;
				case '\\':
					escape = '\\';
					break;
				case '/':
					escape = '/';
					break;
				case '"':
					escape = '"';
					break;
				default:
				}
				if (escape == 0) {
					this.mWriter.writeChar(ch);
				} else {
					this.mWriter.writeChar('\\');
					this.mWriter.writeChar(escape);
				}
			}
			this.mWriter.writeChar('"');
		}

		@Override
		public void onString(String s) throws JsonException {
			this._tryWriteElementSpliter();
			this._writeJsonString(s);
			this.mPrevToken = 'a';
		}

		@Override
		public void onBoolean(boolean value) throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeString(value ? "true" : "false");
			this.mPrevToken = 'a';
		}

		@Override
		public void onInteger(int value) throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeString(Integer.toString(value));
			this.mPrevToken = 'a';
		}

		@Override
		public void onLong(long value) throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeString(Long.toString(value));
			this.mPrevToken = 'a';
		}

		@Override
		public void onDouble(double value) throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeString(Double.toString(value));
			this.mPrevToken = 'a';
		}

		@Override
		public void onNull() throws JsonException {
			this._tryWriteElementSpliter();
			this.mWriter.writeString("null");
			this.mPrevToken = 'a';
		}
	}

}
