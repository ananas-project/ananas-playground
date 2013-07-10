package ananas.lib.axk.engine.sasl;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ananas.lib.axk.engine.XAccount;
import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextController;
import ananas.lib.axk.engine.XContextControllerFactory;
import ananas.lib.axk.engine.util.Base64;

public class SASLProcPlain extends AbstractSASLProc {

	public static XContextControllerFactory getFactory() {
		return new XContextControllerFactory() {

			@Override
			public XContextController newController() {
				return new SASLProcPlain();
			}
		};
	}

	@Override
	public void onStanzaElement(XContext context, Element element) {

		String fn = this.getElementFullURI(element);

		if (fn == null) {
			// ...
		} else if (fn.equals("http://etherx.jabber.org/streams#features")) {
			NodeList mech = element.getElementsByTagName("mechanisms");
			if (mech.getLength() > 0) {
				this.doAuth(context);
				return;
			}
		} else if (fn.equals("urn:ietf:params:xml:ns:xmpp-sasl#success")) {
			this.doSuccess(context, element);
		} else {
			// ...
		}
		super.onStanzaElement(context, element);

	}

	private void doSuccess(XContext context, Element element) {
		LoginController ctrl = new LoginController();
		context.getContextControllerAgent().setContextController(ctrl);
		ctrl.onStanzaElement(context, element);
	}

	public void doAuth(XContext context) {

		// <auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl'
		// mechanism='PLAIN'>AGh4dwAx</auth>

		String base64string = null;

		try {

			XAccount account = context.getAccount();
			// System.err.println("sasl plain with : " + account);
			URI jid = URI.create("jid://" + account.getJID());
			String user, host;
			user = jid.getUserInfo();
			host = jid.getHost();

			StringBuilder sb = new StringBuilder();
			sb.append(user + "@" + host);
			sb.append("\0");
			sb.append(user);
			sb.append("\0");
			sb.append(account.getPassword());
			byte[] ba1 = sb.toString().getBytes("UTF-8");

			base64string = this.bytesToBase64(ba1);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuilder xsb = new StringBuilder();
		xsb.append("<auth");
		this.appendAttribute(xsb, "xmlns", "urn:ietf:params:xml:ns:xmpp-sasl");
		this.appendAttribute(xsb, "mechanism", "PLAIN");
		xsb.append(">");
		xsb.append(base64string);
		xsb.append("</auth>");

		this.send(context, xsb.toString());

	}

	private void appendAttribute(StringBuilder buff, String name, String value) {
		buff.append(" " + name + "='" + value + "' ");
	}

	private String bytesToBase64(byte[] buff) {
		return Base64.getInstance().doEncode(buff);
	}

}
