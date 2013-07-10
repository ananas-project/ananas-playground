package ananas.lib.impl.axk.client.conn;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.element.jabber_client.Xmpp_iq;
import ananas.lib.axk.element.stream.Xmpp_features;
import ananas.lib.axk.element.stream.Xmpp_stream;
import ananas.lib.axk.element.xmpp_bind.Xmpp_bind;
import ananas.lib.axk.element.xmpp_sasl.Xmpp_mechanism;
import ananas.lib.axk.element.xmpp_sasl.Xmpp_mechanisms;
import ananas.lib.axk.element.xmpp_tls.Xmpp_proceed;
import ananas.lib.axk.element.xmpp_tls.Xmpp_starttls;
import ananas.lib.axk.util.XmppStanzaBuilder;
import ananas.lib.impl.axk.client.conn.XmppConnection.DefaultCreateContext;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class TheMainConnCtrl extends XmppConnectionController {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private Map<String, IXmppConnectionControllerFactory> mCtrlFactoryMap;

	public TheMainConnCtrl(XmppConnection conn) {
		super(conn);
	}

	private Map<String, IXmppConnectionControllerFactory> getCtrlFactoryMap() {
		Map<String, IXmppConnectionControllerFactory> map = this.mCtrlFactoryMap;
		if (map == null) {
			map = new HashMap<String, IXmppConnectionControllerFactory>();

			map.put("PLAIN", new ThePlainSaslConnCtrl.Factory());
			map.put("+++PLAIN+++", new ThePlainSaslConnCtrl.Factory());

			this.mCtrlFactoryMap = map;
		}
		return map;
	}

	@Override
	public void onReceive(Xmpp_stream stream, Object object) {

		if (object instanceof Xmpp_features) {

			Xmpp_features features = (Xmpp_features) object;

			Xmpp_starttls startTLS = (Xmpp_starttls) features
					.findItemByClass(Xmpp_starttls.class);
			Xmpp_mechanisms mechanisms = (Xmpp_mechanisms) features
					.findItemByClass(Xmpp_mechanisms.class);
			Xmpp_bind bind = (Xmpp_bind) features
					.findItemByClass(Xmpp_bind.class);

			if (startTLS != null) {
				this.doStartTLS();
			} else if (bind != null) {
				this.doBind();
			} else if (mechanisms != null) {
				this.doSelectMechanisms(mechanisms, stream, object);
			} else {
				super.onReceive(stream, object);
			}

		} else if (object instanceof Xmpp_iq) {
			Xmpp_iq iq = (Xmpp_iq) object;
			Xmpp_bind bind = (Xmpp_bind) iq.findItemByClass(Xmpp_bind.class);
			if (bind != null) {
				this.onBindOK(bind);
			}

		} else if (object instanceof Xmpp_proceed) {
			this.doCreateTLSConnect();

		} else {
			super.onReceive(stream, object);
		}
	}

	private void onBindOK(Xmpp_bind bind) {

		logger.info(this + ".onBindOK : " + bind.getJID());

		this.getConnection().setCurrentPhase(XmppStatus.online);

		XmppConnection conn = this.getConnection();
		TheOnlineConnCtrl ctrl = new TheOnlineConnCtrl(conn);
		conn.setController(ctrl);

		// this.doPresence();
	}

	/*
	 * private void doPresence() {
	 * 
	 * // <presence > // <show>dnd</show> // <status>Wooing Juliet</status> //
	 * </presence>
	 * 
	 * String show = ""; // String status = "my name is 007";
	 * 
	 * // ////////////////////////
	 * 
	 * XmppStanzaBuilder xsb = XmppStanzaBuilder.Factory.newInstance();
	 * xsb.append("<presence>"); xsb.append("<show>"); xsb.append(show);
	 * xsb.append("</show>"); // xsb.append("<status>"); // xsb.append(status);
	 * // xsb.append("</status>"); xsb.append("</presence>");
	 * 
	 * byte[] ba = xsb.toByteArray(); this.getConnection().asyncSendBytes(ba, 0,
	 * ba.length);
	 * 
	 * 
	 * }
	 */
	private void doBind() {

		this.getConnection().setCurrentPhase(XmppStatus.bind);

		String res = this.getConnection().getAccount().getResource();
		XmppStanzaBuilder xsb = XmppStanzaBuilder.Factory.newInstance();
		xsb.append("<iq");
		xsb.appendAttribute("type", "set");
		xsb.appendAttribute("id", "bind_1");
		xsb.append(">");
		xsb.append("<bind");
		xsb.appendAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-bind");
		xsb.append(">");
		xsb.append("<resource>");
		xsb.append(res + "");
		xsb.append("</resource>");
		xsb.append("</bind>");
		xsb.append("</iq>");
		byte[] ba = xsb.toByteArray();
		this.getConnection().asyncSendBytes(ba, 0, ba.length);
	}

	private void doSelectMechanisms(Xmpp_mechanisms mechanisms,
			Xmpp_stream stream, Object object) {

		this.getConnection().setCurrentPhase(XmppStatus.startsasl);

		List<Xmpp_mechanism> list = mechanisms.listItems();
		String name = null;
		IXmppConnectionControllerFactory fact = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			Xmpp_mechanism item = list.get(i);

			name = item.getName();
			fact = this.getCtrlFactoryMap().get(name);
			logger.info("          find " + item + " result : " + fact);
			if (fact != null) {
				break;
			}
		}
		XmppConnection conn = this.getConnection();
		IXmppConnectionController ctrl = fact.newController(conn);
		conn.setController(ctrl);
		ctrl.onReceive(stream, this.getStartToken());
	}

	private void doCreateTLSConnect() {

		this.getConnection().setCurrentPhase(XmppStatus.starttls);

		XmppEnvironment envi = this.getConnection().getEnvironment();
		XmppAccount account = this.getConnection().getAccount();

		DefaultCreateContext cc = new XmppConnection.DefaultCreateContext(
				this.getConnection());
		SocketKit sk = this.getConnection().getSocketKit();
		cc.mSocketKitFactory = new MyTLSSocketKitFactory(envi, account, sk);
		XmppConnection conn = new XmppConnection(cc);
		conn.run();
	}

	static class MyTLSSocketKitFactory implements SocketKitFactory {

		private final SocketKit mParentSK;
		private final XmppAccount mAccount;
		private final XmppEnvironment mEnvi;

		public MyTLSSocketKitFactory(XmppEnvironment envi, XmppAccount account,
				SocketKit parent_sk) {

			this.mEnvi = envi;
			this.mAccount = account;
			this.mParentSK = parent_sk;
		}

		@Override
		public SocketKit createSocketKit() throws IOException {
			Socket psock = this.mParentSK.getSocket();
			String host = psock.getInetAddress().getHostAddress();
			int port = psock.getPort();

			// SSLSocketFactory sf = SSLContext.getDefault().

			SSLContext sslContext = this.getSSLContext();
			SSLSocketFactory sf = sslContext.getSocketFactory();

			Socket sock = sf.createSocket(psock, host, port, true);
			return new DefaultSocketKit(sock);
		}

		private SSLContext getSSLContext() {
			boolean ignoreError = this.mAccount.isIgnoreSSLError();
			return this.mEnvi.getSecurityManager().getSSLContext(ignoreError);
		}
	}

	private void doStartTLS() {

		this.getConnection().setCurrentPhase(XmppStatus.starttls);

		XmppStanzaBuilder xsb = XmppStanzaBuilder.Factory.newInstance();
		xsb.append("<starttls");
		xsb.appendAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-tls");
		xsb.append("/>");
		byte[] ba = xsb.toByteArray();
		this.getConnection().asyncSendBytes(ba, 0, ba.length);
	}

}
