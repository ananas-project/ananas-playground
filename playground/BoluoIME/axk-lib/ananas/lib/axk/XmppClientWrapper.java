package ananas.lib.axk;

public interface XmppClientWrapper extends XmppClient {

	void addTarget(XmppClient client);

	void addTarget(XmppClient client, String pos);

}
