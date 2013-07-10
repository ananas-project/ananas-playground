package ananas.lib.blueprint3.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class MyUTF8StreamReader extends Reader {

	private final InputStream mIn;

	public MyUTF8StreamReader(InputStream in) {
		this.mIn = in;
	}

	@Override
	public void close() throws IOException {
		this.mIn.close();
	}

	@Override
	public int read(char[] buf, int off, int len) throws IOException {

		if (len > 0) {
			int ch = this._read();
			if (ch >= 0) {
				buf[off] = (char) ch;
				return 1;
			} else {
				return 0;
			}

		} else {
			return 0;
		}
	}

	@Override
	public int read() throws IOException {
		return this._read();
	}

	private int _read() throws IOException {
		return this.mIn.read();
	}

}
