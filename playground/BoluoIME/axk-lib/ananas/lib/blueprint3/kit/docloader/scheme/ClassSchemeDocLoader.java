package ananas.lib.blueprint3.kit.docloader.scheme;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.lang.BPDocumentLoader;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactory;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.io.ResourceClassConnection;
import ananas.lib.io.ResourceConnection;

public class ClassSchemeDocLoader implements BPDocumentLoader {

	public static BPDocumentLoaderFactory getFactory() {
		return new BPDocumentLoaderFactory() {

			@Override
			public BPDocumentLoader newLoader() {
				return new ClassSchemeDocLoader();
			}
		};
	}

	@Override
	public void loadDocument(BPDocument doc) throws IOException {

		URI uri = doc.getDocumentURI();
		BPEnvironment envi = doc.getDocumentGroup().getEnvironment();
		ResourceClassConnection conn1 = (ResourceClassConnection) envi
				.getConnector().open(uri);

		ResourceConnection conn2 = conn1.getResource(null);
		InputStream in = conn2.getInputStream();

		BPDocumentLoader ldr = envi.getUriSchemeRegistrar().getFactory("")
				.newLoader();
		ldr.loadDocument(doc, in);

		in.close();
		conn2.close();
		conn1.close();

	}

	@Override
	public void loadDocument(BPDocument doc, InputStream in) throws IOException {
		throw new RuntimeException("no impl");
	}

}
