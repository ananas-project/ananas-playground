package ananas.app.dlm2kml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DlmReader implements LocationInput {

	private File mFile;
	private MyCore mCore;

	public DlmReader(File file) {
		this.mFile = file;
	}

	@Override
	public Location read() throws IOException {
		MyCore core = this._getCore();
		return core.nextLocation();
	}

	private MyCore _getCore() throws IOException {
		MyCore core = this.mCore;
		if (core == null) {
			core = new MyCore(this.mFile);
			core.open();
			this.mCore = core;
		}
		return core;
	}

	@Override
	public void close() throws IOException {
		MyCore core = this._getCore();
		core.close();
	}

	class MyCore {

		private File mFile;
		private InputStream mIn;
		private Reader mReader;
		private boolean mIsEOF;
		private int mCntA;
		private int mCntD;
		private final StringBuilder mStrBuff = new StringBuilder();
		private int mCurLineNum;

		public MyCore(File file) {
			this.mFile = file;
		}

		public Location nextLocation() throws IOException {
			String line;
			for (;;) {
				line = this.nextLine();

				if (line == null) {
					return null;
				} else if (line.startsWith("$LOCATION")) {
					break;
				} else if (line.startsWith("$FIELDS")) {
				}
			}

			return this.str2Location(line);

		}

		final MyStringSplit mStrSplit = new MyStringSplit();

		private Location str2Location(String line) {

			String[] array = this.mStrSplit.split(line, ',');

			Location loc = new Location();

			loc.longitude = Double.parseDouble(array[3 + 1]);
			loc.latitude = Double.parseDouble(array[4 + 1]);
			loc.altitude = Double.parseDouble(array[5 + 1]);

			return loc;
		}

		public void open() throws IOException {

			InputStream in = new FileInputStream(this.mFile);
			Reader rdr = new InputStreamReader(in);

			this.mIn = in;
			this.mReader = rdr;

			for (;;) {
				String str = this.nextLine();
				System.out.println(str);
				if (str == null) {
					break;
				} else if (str.isEmpty()) {
					break;
				}
			}
		}

		private String nextLine() throws IOException {
			if (this.mIsEOF)
				return null;
			Reader rdr = this.mReader;
			StringBuilder sb = this.mStrBuff;
			for (; !this.mIsEOF;) {
				boolean isLineEnd = false;
				int ch = rdr.read();
				if (ch < 0) {
					this.mIsEOF = true;
					isLineEnd = true;
				} else if (ch == 0x0a) {
					this.mCntA++;
					isLineEnd = true;
				} else if (ch == 0x0d) {
					this.mCntD++;
					isLineEnd = true;
				} else {
					sb.append((char) ch);
				}
				if (isLineEnd) {
					int linenum = Math.max(this.mCntA, this.mCntD);
					if (linenum != this.mCurLineNum) {
						this.mCurLineNum = linenum;
						String str = sb.toString();
						sb.setLength(0);
						return str;
					}
				}
			}
			return null;
		}

		public void close() throws IOException {

			this.mIn.close();
		}
	}

	class MyStringSplit {

		final String[] mArray = new String[10];

		final StringBuilder mSb = new StringBuilder();

		public String[] split(String line, char mark) {

			String[] array = this.mArray;
			for (int i = array.length - 1; i >= 0; i--) {
				array[i] = null;
			}
			StringBuilder sb = this.mSb;
			sb.setLength(0);
			int i = 0;
			char[] chs = line.toCharArray();
			for (char ch : chs) {
				if (ch == mark) {
					String str = sb.toString();
					sb.setLength(0);
					if (i < array.length) {
						array[i++] = str;
					}
				} else {
					sb.append(ch);
				}

			}

			return this.mArray;
		}
	}
}
