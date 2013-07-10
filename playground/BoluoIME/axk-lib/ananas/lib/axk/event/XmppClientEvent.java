package ananas.lib.axk.event;

import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppEvent;

public interface XmppClientEvent extends XmppEvent {

	XmppClient getClient();

}
