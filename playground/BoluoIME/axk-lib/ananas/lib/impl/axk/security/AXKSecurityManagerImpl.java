package ananas.lib.impl.axk.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import ananas.lib.axk.security.AXKSecurityListener;
import ananas.lib.axk.security.AXKSecurityManager;

public class AXKSecurityManagerImpl implements AXKSecurityManager {

	private AXKSecurityListener mListener = new DefaultAXKSecurityListener();
	private SSLContext mContextIgnoreError;
	private SSLContext mContextDefault;

	@Override
	public SSLContext getSSLContext(boolean ignoreError) {

		SSLContext defaultCont = this.getDefaultContext();
		SSLContext ignoredCont = this.getIgnoredContext();

		if (ignoreError) {
			return ignoredCont;
		} else {
			return defaultCont;
		}
	}

	private SSLContext getDefaultContext() {
		SSLContext context = this.mContextDefault;
		if (context == null) {
			boolean isIgnoreError = false;
			AXKSecurityListener listener = new MyAXKSecurityListenerProxy();
			context = (new MySSLContextProvider()).newContext(listener,
					isIgnoreError);
			this.mContextDefault = context;
		}
		return context;
	}

	private SSLContext getIgnoredContext() {
		SSLContext context = this.mContextIgnoreError;
		if (context == null) {
			boolean isIgnoreError = true;
			AXKSecurityListener listener = new MyAXKSecurityListenerProxy();
			context = (new MySSLContextProvider()).newContext(listener,
					isIgnoreError);
			this.mContextIgnoreError = context;
		}
		return context;
	}

	@Override
	public void setSecurityListener(AXKSecurityListener listener) {
		if (listener == null) {
			listener = new DefaultAXKSecurityListener();
		}
		this.mListener = listener;
	}

	class MyAXKSecurityListenerProxy implements AXKSecurityListener {

		@Override
		public void onX509CertificateException(CertificateException e,
				X509Certificate[] certificates, String authType)
				throws CertificateException {

			AXKSecurityManagerImpl.this.mListener.onX509CertificateException(e,
					certificates, authType);

		}

	}

}
