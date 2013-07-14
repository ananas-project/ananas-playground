package ananas.lib.axk.engine.sasl;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ananas.lib.axk.engine.XAccount;
import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XContextController;
import ananas.lib.axk.engine.XContextControllerFactory;
import ananas.lib.axk.engine.XEngine;
import ananas.lib.axk.engine.XPhase;
import ananas.lib.axk.engine.util.DefaultSocketContext;
import ananas.lib.axk.engine.util.DefaultXContext;

public class LoginController extends AbstractXContextController implements
		XContextController {

	private final Map<String, XContextControllerFactory> m_saslProcMap;

	public LoginController() {
		this.m_saslProcMap = this.createSaslProcMap();
	}

	private Map<String, XContextControllerFactory> createSaslProcMap() {
		Map<String, XContextControllerFactory> map = new HashMap<String, XContextControllerFactory>();
		map.put("PLAIN", SASLProcPlain.getFactory());
		return map;
	}

	@Override
	public void onStanzaElement(XContext context, Element stanza) {

		final String fu = this.getElementFullURI(stanza);

		if (fu == null) {
			// ...
		} else if (fu.equals("http://etherx.jabber.org/streams#features")) {

			NodeList starttls = stanza.getElementsByTagName("starttls");
			NodeList mechanisms = stanza.getElementsByTagName("mechanisms");
			NodeList bind = stanza.getElementsByTagName("bind");

			if (starttls.getLength() > 0) {
				this.doStartTLS_1(context);
				return;
			} else if (bind.getLength() > 0) {
				this.doBind(context, stanza);
				return;
			} else if (mechanisms.getLength() > 0) {
				this.doSASL(context, stanza);
				return;
			}

		} else if (fu.equals("urn:ietf:params:xml:ns:xmpp-tls#proceed")) {
			this.doStartTLS_2(context);
			return;
		} else if (fu.equals("urn:ietf:params:xml:ns:xmpp-sasl#success")) {
			this.doSASL_ok(context, stanza);
			return;
		} else if (fu.equals("jabber:client#iq")) {
			NodeList bind = stanza.getElementsByTagName("bind");
			if (bind.getLength() > 0) {
				this.doBind_ok(context, stanza);
				return;
			}
		} else {
			// ...
		}

		// default
		super.onStanzaElement(context, stanza);

	}

	private void doBind_ok(XContext context, Element stanza) {
		this.println("doBind_ok");
		XContextController ctrl = new OnlineController();
		context.getContextControllerAgent().setContextController(ctrl);

		NodeList jid = stanza.getElementsByTagName("jid");
		Text txt = (Text) jid.item(0).getFirstChild();
		context.getContextControllerAgent().setBindedFullJID(txt.getData());

		context.getContextControllerAgent().onPhaseChanged(context,
				XPhase.online);

	}

	private void doBind(XContext context, Element stanza) {
		this.println("doBind");
		context.getContextControllerAgent()
				.onPhaseChanged(context, XPhase.bind);
		StringBuilder sb = new StringBuilder();
		sb.append("<iq id='bind_1' type='set'>");
		sb.append("<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>");
		sb.append("<resource>");
		sb.append(context.getAccount().getResource() + "");
		sb.append("</resource></bind></iq>");
		this.send(context, sb.toString());
	}

	private void doSASL_ok(XContext context, Element stanza) {
		this.println("doSASL_ok");
		try {
			DefaultXContext context2 = new DefaultXContext(context);
			XEngine engine = context.getEngineFactory().createEngine();
			engine.run(context2);
		} catch (Exception e) {
			this.__logError(e);
		}
	}

	private void doSASL(XContext context, Element stanza) {
		this.println("doSASL");
		context.getContextControllerAgent()
				.onPhaseChanged(context, XPhase.sasl);
		NodeList chs = stanza.getElementsByTagName("mechanism");
		int len = chs.getLength();
		XContextControllerFactory saslProc = null;
		for (int i = 0; i < len; i++) {
			Element ele = (Element) chs.item(i);
			Text txt = (Text) ele.getFirstChild();
			String key = txt.getData();
			saslProc = LoginController.this.m_saslProcMap.get(key);
			if (saslProc != null) {
				break;
			}
		}
		if (saslProc == null) {
			throw new RuntimeException("no supported mechanism for sasl!");
		}
		XContextController ctrl = saslProc.newController();
		context.getContextControllerAgent().setContextController(ctrl);
		ctrl.onStanzaElement(context, stanza);
	}

	private void doStartTLS_1(XContext context) {
		this.println("doStartTLS_1");
		context.getContextControllerAgent().onPhaseChanged(context, XPhase.tls);
		String s = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
		this.send(context, s);
	}

	private void doStartTLS_2(XContext context) {
		this.println("doStartTLS_2");
		try {
			XAccount account = context.getAccount();
			String host = account.getHost();
			int port = account.getPort();
			Socket sockPlain = context.getSocketContext().getSocket();
			boolean autoClose = true;
			Socket sockSSL = context.getSSLSocketFactory().createSocket(
					sockPlain, host, port, autoClose);
			DefaultXContext context2 = new DefaultXContext(context);
			context2.m_socketContext = new DefaultSocketContext(sockSSL);
			XEngine engine = context.getEngineFactory().createEngine();
			engine.run(context2);
		} catch (Exception e) {
			this.__logError(e);
		}
	}

	private void __logError(Exception e) {
		System.err.println(e);
	}

	private void println(String string) {
		// System.out.println();
		// System.out.println(this + "." + string);
	}

	@Override
	public void onPhaseChanged(XContext context, XPhase phase) {
		context.getEngineListener().onPhaseChanged(context, phase);
	}

}
