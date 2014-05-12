package ananas.app.dlm2kml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class KmlWriter2 implements LocationOutput {

	private File mFile;
	private MyCore mCore;

	public KmlWriter2(File file) {
		this.mFile = file;
	}

	@Override
	public void write(Location loc) throws IOException {

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

	static class MyCore {

		final MyTemplate mTemp = new MyTemplate("kml_template_2.xml");
		private OutputStream mOut;
		private Writer mWriter;
		private final File mFile;

		public MyCore(File file) {
			this.mFile = file;
		}

		public void open() {

			this.mTemp.load();

		}

		public void close() {

			try {

				Document doc = this.mTemp._doc;
				org.w3c.dom.ls.DOMImplementationLS impl_ls = (DOMImplementationLS) doc
						.getImplementation().getFeature("LS", "3.0");
				LSSerializer seri = impl_ls.createLSSerializer();
				String uri = this.mFile.toURI().toString();
				seri.writeToURI(doc, uri);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	static class MyTemplate {

		private final String _file_name;
		private Document _doc;

		public MyTemplate(String file_name) {
			this._file_name = file_name;
		}

		public void load() {
			try {
				InputStream in = KmlWriter2.class
						.getResourceAsStream(_file_name);
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(in);
				in.close();
				this._doc = doc;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
