package ananas.lib.axk.element.rosterver;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;

public class Ctrl_ver extends XmppObject {

	public boolean set_attribute_id(BPAttribute attr) {
		return true;
	}

	public boolean append_child_optional(Ctrl_optional op) {
		return true;
	}
}
