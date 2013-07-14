package ananas.lib.axk.element.xmpp_bind;

import ananas.lib.axk.element.XmppObject;

public class Ctrl_bind extends XmppObject {

	public boolean append_child_jid(Ctrl_jid jid) {
		this.target_bind().setJID(jid.target_jid());
		return true;
	}

	public Xmpp_bind target_bind() {
		return (Xmpp_bind) this.getTarget(true);
	}

}
