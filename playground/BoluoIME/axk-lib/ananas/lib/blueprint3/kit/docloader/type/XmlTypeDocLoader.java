package ananas.lib.blueprint3.kit.docloader.type;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.lang.BPDocumentLoader;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactory;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.util.BPBuilder;

public class XmlTypeDocLoader implements BPDocumentLoader {

	public static BPDocumentLoaderFactory getFactory() {
		return new BPDocumentLoaderFactory() {

			@Override
			public BPDocumentLoader newLoader() {
				return new XmlTypeDocLoader();
			}
		};
	}

	@Override
	public void loadDocument(BPDocument doc) throws IOException {
		// TODO Auto-generated method stub
		throw new RuntimeException("no impl");
	}

	@Override
	public void loadDocument(BPDocument doc, InputStream in) throws IOException {

		try {
			BPEnvironment envi = doc.getDocumentGroup().getEnvironment();
			BPBuilder builder = envi.getBuilderFactory().newBuilder(doc);

			XMLReader reader = envi.getXMLReaderFactory().newReader();
			reader.setContentHandler(builder.getContentHandler());
			reader.setErrorHandler(builder.getErrorHandler());
			reader.parse(new InputSource(in));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
