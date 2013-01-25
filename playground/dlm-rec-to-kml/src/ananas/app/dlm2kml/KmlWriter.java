package ananas.app.dlm2kml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class KmlWriter implements LocationOutput {

	private File mFile;
	private MyCore mCore;

	public KmlWriter(File file) {
		this.mFile = file;
	}

	@Override
	public void write(Location loc) throws IOException {
		Writer wtr = this._getCore().getWriter();

		boolean withAltitude = false;

		if (withAltitude) {
			wtr.write(loc.longitude + "," + loc.latitude + "," + loc.altitude
					+ "\n");
		} else {
			wtr.write(loc.longitude + "," + loc.latitude + "\n");
		}
	}

	private MyCore _getCore() throws IOException {
		MyCore core = this.mCore;
		if (core == null) {
			core = new MyCore(this.mFile);
			this.mCore = core;
			core.open();
		}
		return core;
	}

	@Override
	public void close() throws IOException {
		MyCore core = this._getCore();
		core.close();
	}

	class MyCore {

		final MyTemplate mTemp = new MyTemplate("kml_template.xml");
		private OutputStream mOut;
		private Writer mWriter;
		private File mFile;

		public MyCore(File file) {
			this.mFile = file;
		}

		public void close() throws IOException {

			this.mWriter.write(this.mTemp.getPart2());

			this.mWriter.close();
			this.mOut.close();
		}

		public Writer getWriter() {
			return this.mWriter;
		}

		public void open() throws IOException {
			File file = this.mFile;

			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			OutputStream out = new FileOutputStream(file);
			Writer wtr = new OutputStreamWriter(out);
			this.mOut = out;
			this.mWriter = wtr;

			this.mTemp.load();
			wtr.write(this.mTemp.getPart1());

		}
	}

	class MyTemplate {

		private String mFileName;
		private String mPart1;
		private String mPart2;

		public MyTemplate(String filename) {
			this.mFileName = filename;
		}

		public String getPart1() {
			return this.mPart1;
		}

		public String getPart2() {
			return this.mPart2;
		}

		public void load() throws IOException {

			InputStream in = KmlWriter.class
					.getResourceAsStream(this.mFileName);
			Reader rdr = new InputStreamReader(in);
			char[] buff = new char[128];
			StringBuilder sb = new StringBuilder();
			for (;;) {
				int cc = rdr.read(buff, 0, buff.length);
				if (cc > 0) {
					sb.append(buff, 0, cc);
				} else {
					break;
				}
			}
			in.close();

			String str = sb.toString();
			String mark = "$(coordinates)";

			int i = str.indexOf(mark);
			this.mPart1 = str.substring(0, i);
			this.mPart2 = str.substring(i + mark.length());

		}
	}

}
