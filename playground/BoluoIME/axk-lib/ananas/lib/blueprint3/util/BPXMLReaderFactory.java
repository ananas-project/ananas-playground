package ananas.lib.blueprint3.util;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public interface BPXMLReaderFactory {

	XMLReader newReader() throws SAXException;

}
