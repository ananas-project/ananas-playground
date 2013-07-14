package ananas.lib.impl.axk.client.conn;

import ananas.lib.axk.constant.XmppStatus;

public interface XmppConnectionListener extends ITxThreadPriority {

	/**
	 * all of these methods invoked by rx thread
	 * */

	void onReceive(XmppConnection conn, Object object);

	void invokeWithTxThread(XmppConnection conn, Runnable runn, int priority);

	void onSetCurrentConnection(XmppConnection conn);

	void onSetCurrentPhase(XmppStatus phase);

}
