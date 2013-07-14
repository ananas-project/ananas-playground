package ananas.lib.impl.axk;

import java.net.URI;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientFactory;
import ananas.lib.axk.XmppClientWrapper;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.util.ClassUriGen;

public class TheXmppClientFactory implements XmppClientFactory {

	public TheXmppClientFactory() {
	}

	private XmppEnvironment _getDefaultEnvi() {
		return TheXmppEnvironment.getInstance();
	}

	@Override
	public XmppClient newClient(XmppAccount account) {
		XmppEnvironment envi = this._getDefaultEnvi();
		return this.newClient(account, envi);
	}

	@Override
	public XmppClient newClient(XmppAccount account, XmppEnvironment envi) {

		String path = "xmpp-client.xml";
		URI uri = ClassUriGen.getURI(this.getClass(), path);

		TheXmppClientCore core = new TheXmppClientCore(envi, account);
		XmppClient client = null;

		try {
			BPEnvironment bp = envi.getBPEnvironment();
			BPDocument doc = bp.loadDocument(uri);

			XmppClientWrapper conn = (XmppClientWrapper) doc
					.findTargetById("core");
			conn.addTarget(core);

			client = (XmppClient) doc.getRootElement().getTarget(true);

		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw ((RuntimeException) e);
			} else {
				throw new RuntimeException(e);
			}
		}

		return client;
	}

}
