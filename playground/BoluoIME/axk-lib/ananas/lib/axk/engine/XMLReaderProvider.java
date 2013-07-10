package ananas.lib.axk.engine;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public interface XMLReaderProvider {

	XMLReader newReader() throws SAXException;
}
