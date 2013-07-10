package ananas.lib.axk.element.xmpp_sasl;

import ananas.lib.axk.element.XmppObject;

public class Ctrl_success extends XmppObject {

	public Xmpp_success getTarget_success() {
		return (Xmpp_success) this.getTarget(true);
	}

}
