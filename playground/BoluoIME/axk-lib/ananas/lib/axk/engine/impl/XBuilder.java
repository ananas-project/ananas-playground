package ananas.lib.axk.engine.impl;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;

import ananas.lib.axk.engine.XContext;

interface XBuilder {

	class Factory {

		public static XBuilder createBuilder(XContext context) {
			return new MyDomBuilderImpl(context);
		}

	}

	ContentHandler getContentHandler();

	ErrorHandler getErrorHandler();

}
