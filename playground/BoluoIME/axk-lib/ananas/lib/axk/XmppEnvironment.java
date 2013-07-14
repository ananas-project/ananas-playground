package ananas.lib.axk;

import org.w3c.dom.DOMImplementation;

import ananas.lib.axk.security.AXKSecurityManager;
import ananas.lib.blueprint3.lang.BPEnvironment;

public interface XmppEnvironment {

	void shutdown();

	boolean isAlive();

	BPEnvironment getBPEnvironment();

	XmppClientFactory getClientFactory();

	AXKSecurityManager getSecurityManager();

	DOMImplementation getDOMImplementation();

}
