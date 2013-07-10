package ananas.lib.axk.element.compress;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;

public class Ctrl_compression extends XmppObject {

	public boolean set_attribute_id(BPAttribute attr) {
		return true;
	}

	public boolean append_child_method(Ctrl_method method) {
		return true;
	}
}
