package ananas.lib.impl.axk;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppCommand;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.XmppEvent;
import ananas.lib.axk.XmppEventListener;
import ananas.lib.axk.api.IExCore;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class TheXmppClientCore implements XmppClient, IExCore {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private final XmppEnvironment mEnvi;
	private final XmppAccount mAccount;
	private XmppEventListener mListener;

	public TheXmppClientCore(XmppEnvironment envi, XmppAccount account) {
		this.mEnvi = envi;
		this.mAccount = account;
	}

	@Override
	public void dispatch(XmppCommand cmd) {
		cmd.onSendByClient(this);
	}

	@Override
	public XmppEnvironment getEnvironment() {
		return this.mEnvi;
	}

	@Override
	public void setXmppEventListener(XmppEventListener listener) {
		this.mListener = listener;
	}

	@Override
	public XmppAccount getAccount() {
		return this.mAccount;
	}

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass) {
		if (apiClass.equals(IExCore.class)) {
			IExCore api = this;
			return api;
		} else {
			return null;
		}
	}

	class MyOuterFinderEvent implements XmppEvent {

		private XmppClient mClient;

		@Override
		public void onReceiveByListener(XmppEventListener listener) {

			if (listener instanceof XmppClient) {
				XmppClient client = (XmppClient) listener;
				this.mClient = client;
				logger.trace(".onRecByListener:" + listener);
			}
		}
	}

	final MyOuterFinderEvent mOuterFinderEvent = new MyOuterFinderEvent();

	@Override
	public XmppClient getOuter() {
		XmppClient client = this.mOuterFinderEvent.mClient;
		if (client != null) {
			return client;
		}
		XmppEventListener listener = this.mListener;
		if (listener == null) {
			return this;
		}
		listener.onEvent(this.mOuterFinderEvent);
		client = this.mOuterFinderEvent.mClient;
		if (client != null) {
			return client;
		}
		return this;
	}

}
