package ananas.lib.axk.element.iq_roster;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.constant.XmppSubscription;
import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;

public class Ctrl_item extends XmppObject {

	public boolean set_attribute_jid(BPAttribute attr) {
		XmppAddress addr = this.getXmppAddress(attr.getValue(), false);
		this.target_item().setJID(addr);
		return true;
	}

	public boolean set_attribute_subscription(BPAttribute attr) {
		XmppSubscription subs = XmppSubscription.Factory.getInstance(attr
				.getValue());
		this.target_item().setSubscription(subs);
		return true;
	}

	public boolean set_attribute_name(BPAttribute attr) {
		this.target_item().setName(attr.getValue());
		return true;
	}

	public Xmpp_item target_item() {
		return (Xmpp_item) this.getTarget(true);
	}

	public boolean append_child_group(Ctrl_group group) {
		this.target_item().appendGroup(group.target_group());
		return true;
	}
}
