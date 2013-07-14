package ananas.lib.axk.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface AXKSecurityListener {

	void onX509CertificateException(CertificateException e,
			X509Certificate[] certificates, String authType)
			throws CertificateException;

}
