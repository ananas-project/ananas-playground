package ananas.lib.axk.security;

import javax.net.ssl.SSLContext;

public interface AXKSecurityManager {

	SSLContext getSSLContext(boolean ignoreError);

	void setSecurityListener(AXKSecurityListener listener);

}
