package ananas.lib.axk.engine.impl;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ananas.lib.axk.engine.XMLReaderProvider;

public class MyXMLReaderProvider implements XMLReaderProvider {

	String m_className = "ananas.lib.blueprint3.xmlparser.SAXParser";

	@Override
	public XMLReader newReader() throws SAXException {
		return XMLReaderFactory.createXMLReader(this.m_className);
	}

}
