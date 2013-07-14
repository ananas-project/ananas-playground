package ananas.lib.axk.element.xmpp_sasl;

public class Xmpp_mechanism {

	private String mName = "";

	public String getName() {
		return this.mName;
	}

	public void setName(String text) {
		if (text != null)
			this.mName = text;
	}

	public String toString() {
		return "XMPP_SASL_mechanism:" + this.mName;
	}

}
