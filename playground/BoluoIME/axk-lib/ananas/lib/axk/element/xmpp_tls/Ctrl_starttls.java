package ananas.lib.axk.element.xmpp_tls;

import ananas.lib.blueprint3.dom.AbstractElement;
import ananas.lib.blueprint3.dom.BPAttribute;

public class Ctrl_starttls extends AbstractElement {

	public boolean set_attribute_id(BPAttribute attr) {
		return true;
	}

	public boolean append_child_required(Ctrl_required child) {
		return true;
	}
}
