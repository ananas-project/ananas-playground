package ananas.lib.axk.element.iq_roster;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPText;

public class Ctrl_group extends XmppObject {

	public Xmpp_group target_group() {
		return (Xmpp_group) this.getTarget(true);
	}

	public boolean append_text(BPText text) {
		String data = text.getData();
		this.target_group().setName(data);
		return true;
	}

}
