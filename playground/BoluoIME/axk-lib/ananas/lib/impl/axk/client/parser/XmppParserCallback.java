package ananas.lib.impl.axk.client.parser;

import ananas.lib.axk.element.stream.Xmpp_stream;

public interface XmppParserCallback {

	void onReceive(Xmpp_stream stream, Object object);

}
