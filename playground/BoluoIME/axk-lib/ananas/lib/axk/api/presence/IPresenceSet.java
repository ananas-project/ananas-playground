package ananas.lib.axk.api.presence;

import ananas.lib.axk.XmppAddress;

public interface IPresenceSet {

	IExPresenceManager getPresenceManager();

	XmppAddress getJID();

	int size();

	IPresenceRes get(int index);
}
