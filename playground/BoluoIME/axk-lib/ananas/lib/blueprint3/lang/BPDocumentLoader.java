package ananas.lib.blueprint3.lang;

import java.io.IOException;
import java.io.InputStream;

import ananas.lib.blueprint3.dom.BPDocument;

public interface BPDocumentLoader {

	void loadDocument(BPDocument doc) throws IOException;

	void loadDocument(BPDocument doc, InputStream in) throws IOException;

}
