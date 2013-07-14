package ananas.lib.axk.event;

import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppEventListener;

public class DefaultDataEvent implements DataEvent {

	private final XmppClient mClient;
	private final Object mData;

	public DefaultDataEvent(XmppClient client, Object data) {
		this.mClient = client;
		this.mData = data;
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
		return this.mData;
	}

}
