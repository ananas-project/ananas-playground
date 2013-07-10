package ananas.lib.axk;

public interface XmppAccount {

	String getHost();

	int getPort();

	String getPassword();

	XmppAddress getAddress();

	String getResource();

	boolean isUseSSL();

	boolean isIgnoreSSLError();
}
