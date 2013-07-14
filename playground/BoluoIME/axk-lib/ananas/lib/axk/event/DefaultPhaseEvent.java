package ananas.lib.axk.event;

import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppEventListener;
import ananas.lib.axk.constant.XmppStatus;

public class DefaultPhaseEvent implements PhaseEvent {

	private final XmppStatus mNewPhase;
	private final XmppStatus mOldPhase;
	private final XmppClient mClient;

	public DefaultPhaseEvent(XmppClient client, XmppStatus oldPhase,
			XmppStatus newPhase) {
		this.mClient = client;
		this.mNewPhase = newPhase;
		this.mOldPhase = oldPhase;
	}

	@Override
	public XmppClient getClient() {
		return this.mClient;
	}

	@Override
	public void onReceiveByListener(XmppEventListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public XmppStatus getOldPhase() {
		return this.mOldPhase;
	}

	@Override
	public XmppStatus getNewPhase() {
		return this.mNewPhase;
	}

}
