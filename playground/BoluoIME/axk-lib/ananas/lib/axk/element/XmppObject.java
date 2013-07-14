package ananas.lib.axk.element;

import ananas.lib.axk.DefaultXmppAddress;
import ananas.lib.axk.XmppAddress;
import ananas.lib.blueprint3.lang.CObject;

public abstract class XmppObject extends CObject {

	protected XmppAddress getXmppAddress(String jid, boolean noCache) {
		if (noCache) {
			return new DefaultXmppAddress(jid);
		}
		// TODO add cache support here
		return new DefaultXmppAddress(jid);
	}

	protected XmppAddress getXmppAddress(String jid) {
		return this.getXmppAddress(jid, false);
	}

}
