package ananas.lib.axk.event;

import org.w3c.dom.Element;

import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppEventListener;
import ananas.lib.axk.api.IExStanzaConvertorProvider;
import ananas.lib.axk.stanza.DefaultStanzaContext;
import ananas.lib.axk.stanza.StanzaContext;
import ananas.lib.axk.stanza.StanzaConvertor;

public class DefaultStanzaEvent implements StanzaEvent {

	private final XmppClient mClient;
	private StanzaContext m_target;

	public DefaultStanzaEvent(XmppClient client, Element element) {
		this.mClient = client;
		StanzaContext target = this.__createTarget(client);
		target.setStanza(element);
		this.setTarget(target);
	}

	private StanzaContext __createTarget(XmppClient client) {
		IExStanzaConvertorProvider scp = (IExStanzaConvertorProvider) client
				.getExAPI(IExStanzaConvertorProvider.class);
		StanzaConvertor convertor = scp.getStanzaConvertor();
		DefaultStanzaContext sc = new DefaultStanzaContext(convertor);
		return sc;
	}

	@Override
	public XmppClient getClient() {
		return this.mClient;
	}

	@Override
	public void onReceiveByListener(XmppEventListener listener) {
	}

	@Override
	public Object getData() {
		StanzaContext tar = this.m_target;
		if (tar == null) {
			return null;
		} else {
			return tar.stanzaToObject();
		}
	}

	@Override
	public StanzaContext getTarget() {
		return this.m_target;
	}

	@Override
	public void setTarget(StanzaContext target) {
		this.m_target = target;
	}

}
