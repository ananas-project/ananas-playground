package ananas.lib.axk.element.xmpp_sasl;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPText;

public class Ctrl_mechanism extends XmppObject {

	public boolean set_attribute_id(BPAttribute attr) {
		return false;
	}

	public Xmpp_mechanism getTarget_mechanism() {
		return (Xmpp_mechanism) this.getTarget(true);
	}

	public boolean append_text(BPText text) {
		String name = text.getData();
		this.getTarget_mechanism().setName(name);
		return true;
	}
}
