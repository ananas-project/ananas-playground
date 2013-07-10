package ananas.lib.axk.element.xmpp_sasl;

public class Xmpp_sasl_error {

	private String mName;

	public Xmpp_sasl_error() {
		this.mName = "unknow";
	}

	public Xmpp_sasl_error(String name) {
		this.mName = name + "";
	}

	public String toString() {
		return this.mName;
	}

}
