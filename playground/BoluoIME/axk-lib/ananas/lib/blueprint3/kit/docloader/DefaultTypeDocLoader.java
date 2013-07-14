package ananas.lib.blueprint3.kit.docloader;

import java.io.IOException;
import java.io.InputStream;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.lang.BPDocumentLoader;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactory;

public class DefaultTypeDocLoader implements BPDocumentLoader {

	public static BPDocumentLoaderFactory getFacotry() {
		return new BPDocumentLoaderFactory() {

			@Override
			public BPDocumentLoader newLoader() {
				return new DefaultTypeDocLoader();
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
		// TODO Auto-generated method stub
		throw new RuntimeException("no impl");

	}

}
