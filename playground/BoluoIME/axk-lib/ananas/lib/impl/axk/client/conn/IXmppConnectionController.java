package ananas.lib.impl.axk.client.conn;

import ananas.lib.axk.element.stream.Xmpp_stream;

public interface IXmppConnectionController {

	void onReceive(Xmpp_stream stream, Object object);

	XmppConnection getConnection();

}
