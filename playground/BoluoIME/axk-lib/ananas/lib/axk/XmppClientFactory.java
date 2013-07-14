package ananas.lib.axk;

public interface XmppClientFactory {

	XmppClient newClient(XmppAccount account);

	XmppClient newClient(XmppAccount account, XmppEnvironment envi);

}
