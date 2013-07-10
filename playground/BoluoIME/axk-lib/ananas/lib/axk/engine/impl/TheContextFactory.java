package ananas.lib.axk.engine.impl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ananas.lib.axk.engine.XAccount;
import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextFactory;
import ananas.lib.axk.engine.XEngineListener;
import ananas.lib.axk.engine.util.DefaultXContext;

public class TheContextFactory implements XContextFactory {

	@Override
	public XContext createContext(XAccount account, XEngineListener listener) {
		return this.createMutableContext(account, listener);
	}

	@Override
	public DefaultXContext createMutableContext(XAccount account,
			XEngineListener listener) {
		DefaultXContext context = new DefaultXContext();

		context.m_account = account;
		context.m_ctrlAgent = new DefaultContextCtrlAgent();
		context.m_engineListener = listener;
		context.m_engineFactory = new TheDefaultXEngineFactory();
		context.m_socketContext = new InitSocketContext(context);
		context.m_xmlReaderProvider = new MyXMLReaderProvider();
		context.m_sslSocketFactory = (new MySSLSocketFactoryProvider())
				.getFactory();
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			context.m_domImpl = builder.getDOMImplementation();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return context;
	}

}
