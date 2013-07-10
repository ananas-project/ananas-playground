package ananas.lib.axk.element.compress;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPText;

public class Ctrl_method extends XmppObject {

	public boolean set_attribute_id(BPAttribute attr) {
		return true;
	}

	public boolean append_text(BPText text) {
		Xmpp_method mtd = this.getTarget_method();
		mtd.setName(text.getData());
		return true;
	}

	private Xmpp_method getTarget_method() {
		return (Xmpp_method) this.getTarget(true);
	}
}
