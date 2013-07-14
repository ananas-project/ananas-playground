package ananas.lib.axk.api.presence;

import ananas.lib.axk.XmppAddress;

public interface IPresenceRes {

	IExPresenceManager getPresenceManager();

	XmppAddress getJID();

	String getShow();
}
