package ananas.lib.axk.engine.util;

import javax.net.ssl.SSLSocketFactory;

import org.w3c.dom.DOMImplementation;

import ananas.lib.axk.engine.XAccount;
import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextControllerAgent;
import ananas.lib.axk.engine.XEngineFactory;
import ananas.lib.axk.engine.XEngineListener;
import ananas.lib.axk.engine.XMLReaderProvider;
import ananas.lib.axk.engine.XSocketContext;

public class XContextWrapper implements XContext {

	private final XContext mInner;

	public XContextWrapper(XContext inner) {
		this.mInner = inner;
	}

	@Override
	public XEngineFactory getEngineFactory() {
		return this.mInner.getEngineFactory();
	}

	@Override
	public XSocketContext getSocketContext() {
		return this.mInner.getSocketContext();
	}

	@Override
	public XAccount getAccount() {
		return this.mInner.getAccount();
	}

	@Override
	public XEngineListener getEngineListener() {
		return this.mInner.getEngineListener();
	}

	@Override
	public DOMImplementation getDOMImplementation() {
		return this.mInner.getDOMImplementation();
	}

	@Override
	public XMLReaderProvider getXMLReaderProvider() {
		return this.mInner.getXMLReaderProvider();
	}

	@Override
	public SSLSocketFactory getSSLSocketFactory() {
		return this.mInner.getSSLSocketFactory();
	}

	@Override
	public XContextControllerAgent getContextControllerAgent() {
		return this.mInner.getContextControllerAgent();
	}
}
