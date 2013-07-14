package ananas.lib.axk.element.iq_roster;

import ananas.lib.axk.element.XmppObject;

public class Ctrl_query extends XmppObject {

	public boolean append_child_item(Ctrl_item item) {
		this.target_query().addItem(item.target_item());
		return true;
	}

	public Xmpp_query target_query() {
		return (Xmpp_query) this.getTarget(true);
	}
}
