package ananas.lib.axk.engine;

import javax.net.ssl.SSLSocketFactory;

import org.w3c.dom.DOMImplementation;

public interface XContext {

	XEngineFactory getEngineFactory();

	XSocketContext getSocketContext();

	XAccount getAccount();

	XContextControllerAgent getContextControllerAgent();

	XEngineListener getEngineListener();

	DOMImplementation getDOMImplementation();

	XMLReaderProvider getXMLReaderProvider();

	SSLSocketFactory getSSLSocketFactory();

}
