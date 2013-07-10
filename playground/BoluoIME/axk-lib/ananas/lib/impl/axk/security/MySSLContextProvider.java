package ananas.lib.impl.axk.security;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import ananas.lib.axk.security.AXKSecurityListener;

public class MySSLContextProvider {

	static class MyX509TrustManager implements X509TrustManager {

		private X509TrustManager standardTrustManager = null;
		private final boolean mIgnore;
		private final AXKSecurityListener mListener;

		public MyX509TrustManager(KeyStore keystore, boolean isIgnoreError,
				AXKSecurityListener listener) throws NoSuchAlgorithmException,
				KeyStoreException {
			super();

			this.mListener = listener;
			this.mIgnore = isIgnoreError;

			TrustManagerFactory factory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			factory.init(keystore);
			TrustManager[] trustmanagers = factory.getTrustManagers();
			if (trustmanagers.length == 0) {
				throw new NoSuchAlgorithmException("no trust manager found");
			}
			this.standardTrustManager = (X509TrustManager) trustmanagers[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] certificates,
				String authType) throws CertificateException {
			standardTrustManager.checkClientTrusted(certificates, authType);
		}

		@Override
		public void checkServerTrusted(X509Certificate[] certificates,
				String authType) throws CertificateException {

			CertificateException ce = null;
			try {

				if ((certificates != null) && (certificates.length == 1)) {
					certificates[0].checkValidity();
				} else {
					standardTrustManager.checkServerTrusted(certificates,
							authType);
				}
			} catch (CertificateException e) {
				ce = e;
			}

			if (ce != null) {
				if (!this.mIgnore) {
					this.mListener.onX509CertificateException(ce, certificates,
							authType);
				}
			}
		}

		public X509Certificate[] getAcceptedIssuers() {
			return this.standardTrustManager.getAcceptedIssuers();
		}

	}

	public SSLContext newContext(AXKSecurityListener listener,
			boolean isIgnoreError) {

		try {
			SSLContext context = SSLContext.getInstance("TLS");
			MyX509TrustManager tm = new MyX509TrustManager(null, isIgnoreError,
					listener);
			context.init(null, new TrustManager[] { tm }, null);
			return context;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
