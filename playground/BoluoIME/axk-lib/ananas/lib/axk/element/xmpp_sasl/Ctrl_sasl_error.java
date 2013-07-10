package ananas.lib.axk.element.xmpp_sasl;

import ananas.lib.axk.element.XmppObject;

public class Ctrl_sasl_error extends XmppObject {

	@Override
	public Object createTarget() {
		String name = this.getType().getLocalName();
		return new Xmpp_sasl_error(name);
	}

}
