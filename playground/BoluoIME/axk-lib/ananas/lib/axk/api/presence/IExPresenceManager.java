package ananas.lib.axk.api.presence;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClientExAPI;

public interface IExPresenceManager extends XmppClientExAPI {

	// my presence

	/**
	 * @param presence
	 *            a presence stanza
	 * */
	void setMyPresence(String presence);

	/**
	 * @return a presence stanza
	 * */

	String getMyPresence();

	void sendMyPresence();

	void sendMyPresence(String presence);

	// attributes

	boolean isAutoPresenceAfterBinding();

	void setAutoPresenceAfterBinding(boolean value);

	// others

	void clear();

	IPresenceSet getPresenceSet(XmppAddress jid);

	IPresenceRes getPresenceRes(XmppAddress jid);

}
