package ananas.lib.impl.axk.client.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

public interface XmppParser {

	void parse(InputStream is, XmppParserCallback callback) throws SAXException, IOException;

}
