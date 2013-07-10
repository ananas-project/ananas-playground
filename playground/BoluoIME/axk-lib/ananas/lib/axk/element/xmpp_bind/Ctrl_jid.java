package ananas.lib.axk.element.xmpp_bind;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPText;

public class Ctrl_jid extends XmppObject {

	public boolean append_text(BPText text) {
		this.target_jid().setText(text.getData());
		return true;
	}

	public Xmpp_jid target_jid() {
		return (Xmpp_jid) this.getTarget(true);
	}

}
