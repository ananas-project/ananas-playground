package ananas.lib.axk.element.xmpp_sasl;

public class Xmpp_failure {

	private Xmpp_sasl_error mError;

	public void setError(Xmpp_sasl_error error) {
		this.mError = error;
	}

	public String toString() {
		return "failure::" + this.mError;
	}

}
