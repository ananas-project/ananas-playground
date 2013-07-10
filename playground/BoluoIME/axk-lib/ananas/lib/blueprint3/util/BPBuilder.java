package ananas.lib.blueprint3.util;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;

import ananas.lib.blueprint3.dom.BPNode;

public interface BPBuilder {

	ContentHandler getContentHandler();

	ErrorHandler getErrorHandler();

	BPElementProvider getBPElementProvider();

	BPErrorHandler getBPErrorHandler();

	/**
	 * @return a document or element
	 * */
	BPNode getRootNode();

	void setBPElementProvider(BPElementProvider provider);

	void setBPErrorHandler(BPErrorHandler errorHandler);

}
