package ananas.lib.axk.engine;

public interface XAccount {

	int getPort();

	String getHost();

	String getJID();

	String getResource();

	boolean isUseSSL();

	String getPassword();
}
