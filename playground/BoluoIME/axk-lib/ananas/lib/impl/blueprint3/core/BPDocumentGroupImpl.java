package ananas.lib.impl.blueprint3.core;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPDocumentGroup;
import ananas.lib.blueprint3.lang.BPDocumentLoader;
import ananas.lib.blueprint3.lang.BPEnvironment;

public class BPDocumentGroupImpl implements BPDocumentGroup {

	private final BPEnvironment mEnvi;
	private final Map<String, BPDocument> mDocTable;

	public BPDocumentGroupImpl(BPEnvironment envi) {
		this.mEnvi = envi;
		this.mDocTable = new HashMap<String, BPDocument>();
	}

	@Override
	public BPEnvironment getEnvironment() {
		return this.mEnvi;
	}

	@Override
	public BPDocument getDocument(URI uri) {
		return this.mDocTable.get(uri.toString());
	}

	@Override
	public BPDocument[] listDocuments() {
		Collection<BPDocument> docs = this.mDocTable.values();
		return docs.toArray(new BPDocument[docs.size()]);
	}

	@Override
	public BPDocument openDocument(URI uri, boolean doRegister,
			boolean doLoading) throws IOException {

		BPDocument doc = this.getDocument(uri);
		if (doc != null) {
			return doc;
		}

		doc = new BpDocumentImpl(this, uri);
		if (doRegister) {
			this.mDocTable.put(uri.toString(), doc);
		}

		if (doLoading) {
			String key = uri.getScheme();
			BPDocumentLoader ldr = this.getEnvironment()
					.getUriSchemeRegistrar().getFactory(key).newLoader();
			ldr.loadDocument(doc);
		}

		return doc;
	}

	@Override
	public BPDocument openDocument(URI uri) throws IOException {
		return this.openDocument(uri, true, true);
	}

}
