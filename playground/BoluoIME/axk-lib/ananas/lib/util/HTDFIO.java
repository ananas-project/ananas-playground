package ananas.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HTDFIO {

	/**
	 * interface
	 * */
	public static interface HTDFReader {

		String getVersion();

		InputStream getInputStream() throws IOException;

		String getParameter(String name);

		String[] listParameterNames();

		String getContentType();

		void close() throws IOException;
	}

	public static interface HTDFWriter {

		String getVersion();

		OutputStream getOutputStream() throws IOException;

		String[] listParameterNames();

		String getParameter(String name);

		void setParameter(String name, String value);

		void setContentType(String type);

		void close() throws IOException;
	}

	public static HTDFReader newReader(File file) {
		return new HTDFReaderImpl(file);
	}

	public static HTDFWriter newWritter(File file, boolean append) {
		return new HTDFWriterImpl(file, append);
	}

	public static final String VERSION = "HTDF/1.0";

	/**
	 * implements
	 * */

	private static class HTDFWriterImpl implements HTDFWriter {

		private final boolean mAppend;
		private final File mFile;
		private OutputStream mOS;
		private final Map<String, String> mParamList;

		public HTDFWriterImpl(File file, boolean append) {
			this.mFile = file;
			this.mAppend = append;
			this.mParamList = new HashMap<String, String>();
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			OutputStream os = this.mOS;
			if (os == null) {
				if (this.mAppend && (this.mFile.length() > 0)) {
					// do append
					os = new FileOutputStream(this.mFile, true);
				} else {
					// new file
					this.mFile.getParentFile().mkdirs();
					this.mFile.createNewFile();
					os = new FileOutputStream(this.mFile);
					OutputStreamWriter wtr = new OutputStreamWriter(os);
					wtr.write(VERSION + "\n");
					String[] names = this.listParameterNames();
					for (String name : names) {
						String value = this.getParameter(name);
						wtr.write(name + ":" + value + "\n");
					}
					wtr.write("\n");
					wtr.flush();
					wtr.close();
				}
				this.mOS = os;
			}
			return os;
		}

		@Override
		public String getVersion() {
			return VERSION;
		}

		@Override
		public String[] listParameterNames() {
			Set<String> keys = this.mParamList.keySet();
			return keys.toArray(new String[keys.size()]);
		}

		@Override
		public String getParameter(String name) {
			return this.mParamList.get(name);
		}

		@Override
		public void setParameter(String name, String value) {
			this.mParamList.put(name, value);
		}

		@Override
		public void close() throws IOException {
			OutputStream os = this.getOutputStream();
			os.flush();
			os.close();
		}

		@Override
		public void setContentType(String type) {
			this.setParameter("Content-Type", type);
		}

	}

	private static class HTDFReaderImpl implements HTDFReader {

		private final File mFile;
		private String mVersion;
		private final Map<String, String> mParamList;
		private InputStream mIS;

		public HTDFReaderImpl(File file) {
			this.mFile = file;
			this.mParamList = new HashMap<String, String>();
		}

		@Override
		public InputStream getInputStream() throws IOException {
			InputStream is = this.mIS;
			if (is == null) {
				is = new FileInputStream(this.mFile);
				int cntLine = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for (int b = is.read(); b >= 0; b = is.read()) {
					if (b == 0x0a || b == 0x0d) {
						final byte[] ba = baos.toByteArray();
						final int iLine = (cntLine++);
						baos.reset();
						String line = new String(ba, "utf-8");
						if (iLine == 0) {
							this.mVersion = line;
						} else {
							if (line.length() == 0) {
								// head end
								break;
							} else {
								this._parseLine(line);
							}
						}
					} else {
						baos.write(b);
					}
				}
				if (!VERSION.equals(this.mVersion)) {
					throw new IOException("The file is not a regular HTDF:"
							+ this.mVersion);
				}
				this.mIS = is;
			}
			return is;
		}

		private void _parseLine(String line) {
			int i = line.indexOf(':');
			if (i > 0) {
				String value = line.substring(i + 1).trim();
				String name = line.substring(0, i).trim();
				this.mParamList.put(name, value);
			}
		}

		@Override
		public String getVersion() {
			return this.mVersion;
		}

		@Override
		public String getParameter(String name) {
			return this.mParamList.get(name);
		}

		@Override
		public String[] listParameterNames() {
			Set<String> keys = this.mParamList.keySet();
			return keys.toArray(new String[keys.size()]);
		}

		@Override
		public void close() throws IOException {
			this.getInputStream().close();
		}

		@Override
		public String getContentType() {
			return this.getParameter("Content-Type");
		}
	}
}