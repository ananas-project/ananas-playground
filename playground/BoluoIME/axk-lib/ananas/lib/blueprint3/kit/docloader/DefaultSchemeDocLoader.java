package ananas.lib.blueprint3.kit.docloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.lang.BPDocumentLoader;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactory;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.io.ContentConnection;
import ananas.lib.io.InputConnection;

public class DefaultSchemeDocLoader implements BPDocumentLoader {

	public static BPDocumentLoaderFactory getFacotry() {
		return new BPDocumentLoaderFactory() {

			@Override
			public BPDocumentLoader newLoader() {
				return new DefaultSchemeDocLoader();
			}
		};
	}

	@Override
	public void loadDocument(BPDocument doc) throws IOException {
		// default load as inputStreamConn
		URI uri = doc.getDocumentURI();
		InputConnection conn = (InputConnection) doc.getDocumentGroup()
				.getEnvironment().getConnector().open(uri.toString());
		InputStream in = conn.getInputStream();
		String type = null;
		if (conn instanceof ContentConnection) {
			ContentConnection cc = (ContentConnection) conn;
			type = cc.getType();
		}
		if (type == null) {
			type = this._getDocType(doc);
		}
		this._doLoad(doc, in, type);
		in.close();
		conn.close();
	}

	@Override
	public void loadDocument(BPDocument doc, InputStream in) throws IOException {
		String type = this._getDocType(doc);
		this._doLoad(doc, in, type);
	}

	private void _doLoad(BPDocument doc, InputStream in, String type)
			throws IOException {
		BPDocumentLoaderFactory factory = doc.getDocumentGroup()
				.getEnvironment().getContentTypeRegistrar().getFactory(type);
		BPDocumentLoader ldr = factory.newLoader();
		ldr.loadDocument(doc, in);
	}

	private String _getDocType(BPDocument doc) {

		String type = doc.getDocumentType();
		if (type != null) {
			return type;
		}

		String dotName = "";
		String path = doc.getDocumentURI().getPath();
		int slash = path.lastIndexOf('/');
		int dot = path.lastIndexOf('.');
		if ((slash < dot) && (0 <= dot)) {
			dotName = path.substring(dot);
		}

		BPEnvironment envi = doc.getDocumentGroup().getEnvironment();
		type = envi.getFileNameMapper().getContentType(dotName);
		doc.setDocumentType(type);

		return type;
	}

}
