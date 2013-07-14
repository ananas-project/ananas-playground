package ananas.lib.axk.engine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XEngine;
import ananas.lib.axk.engine.XSocketContext;

final class XEngineImpl implements XEngine {

	@Override
	public void run(XContext context) throws IOException, SAXException {

		XSocketContext sockCont = context.getSocketContext();
		InputStream in = sockCont.getInput();
		OutputStream out = sockCont.getOutput();

		// out
		StringBuilder sb = new StringBuilder();
		{
			sb.append("<?xml encoding='UTF-8' ?>");
			sb.append("<stream:stream");
			sb.append(" xmlns:stream='http://etherx.jabber.org/streams'");
			sb.append(" xmlns='jabber:client'");
			sb.append(" version='1.0'");
			sb.append(">");
		}
		byte[] buff = sb.toString().getBytes("UTF-8");
		out.write(buff);
		out.flush();

		// in
		XBuilder builder = XBuilder.Factory.createBuilder(context);
		XMLReader reader = context.getXMLReaderProvider().newReader();
		// reader.setFeature(name, false);
		reader.setContentHandler(builder.getContentHandler());
		reader.setErrorHandler(builder.getErrorHandler());
		reader.parse(new InputSource(in));

	}

}
