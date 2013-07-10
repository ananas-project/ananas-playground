package ananas.lib.impl.axk;

import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppClientWrapper;
import ananas.lib.axk.XmppCommand;
import ananas.lib.axk.XmppEvent;
import ananas.lib.axk.XmppEventListener;

public class AbstractXmppClientWrapper implements XmppClientWrapper,
		XmppEventListener {

	private XmppEventListener mListener;
	private XmppClient mTarget;

	@Override
	public void setXmppEventListener(XmppEventListener listener) {
		this.mListener = listener;
	}

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass) {
		XmppClient tar = this.mTarget;
		if (tar != null) {
			return tar.getExAPI(apiClass);
		} else {
			return null;
		}
	}

	@Override
	public void dispatch(XmppCommand cmd) {
		cmd.onSendByClient(this);
		XmppClient tar = this.mTarget;
		if (tar != null) {
			tar.dispatch(cmd);
		}
	}

	private void _setCurTarget(XmppClient pnew) {
		XmppClient pold;
		synchronized (this) {
			pold = this.mTarget;
			this.mTarget = pnew;
		}
		if (pold != null) {
			pold.setXmppEventListener(null);
		}
		if (pnew != null) {
			pnew.setXmppEventListener(this);
		}
	}

	@Override
	public void addTarget(XmppClient client) {
		this._setCurTarget(client);
	}

	@Override
	public void addTarget(XmppClient client, String pos) {
		this._setCurTarget(client);
	}

	@Override
	public void onEvent(XmppEvent event) {
		event.onReceiveByListener(this);
		XmppEventListener li = this.mListener;
		if (li != null) {
			li.onEvent(event);
		}
	}

}
