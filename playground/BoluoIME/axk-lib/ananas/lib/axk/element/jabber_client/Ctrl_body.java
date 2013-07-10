package ananas.lib.axk.element.jabber_client;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPText;

public class Ctrl_body extends XmppObject {

	final StringBuilder mSB = new StringBuilder();

	public boolean append_text(BPText text) {
		this.mSB.append(text.getData());
		return true;
	}

	public Xmpp_body target_body() {
		return (Xmpp_body) this.getTarget(true);
	}

	public void onTagEnd() {
		super.onTagEnd();
		String str = this.mSB.toString().trim();
		this.target_body().setText(str);
	}

}
