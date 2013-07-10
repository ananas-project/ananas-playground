package ananas.lib.blueprint3.dom;

import java.io.IOException;
import java.net.URI;

import ananas.lib.blueprint3.lang.BPEnvironment;

public interface BPDocumentGroup {

	BPEnvironment getEnvironment();

	BPDocument openDocument(URI uri, boolean doRegister, boolean doLoading)
			throws IOException;

	BPDocument openDocument(URI uri) throws IOException;

	BPDocument getDocument(URI uri);

	BPDocument[] listDocuments();

}
