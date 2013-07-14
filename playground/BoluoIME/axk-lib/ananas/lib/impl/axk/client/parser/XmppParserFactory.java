package ananas.lib.impl.axk.client.parser;

import ananas.lib.axk.XmppEnvironment;

public interface XmppParserFactory {

	XmppParser newParser(XmppEnvironment envi);
}
