package ananas.lib.axk.api;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.constant.XmppStatus;

public interface IExConnection extends XmppClientExAPI {

	void setStatus(XmppStatus phase);

	XmppStatus getStatus();

	XmppStatus getPhase();

	XmppAddress getBindingJID();

	/**
	 * start new thread
	 * */
	void reset();

	/**
	 * exit all thread; set phase to closed
	 * */

	void close();

	/**
	 * create new connection; set phase to online
	 * */
	void connect();

	/**
	 * close all connection; set phase to off-line
	 * */
	void disconnect();

	boolean sendStanza(byte[] buffer, int offset, int length);

	boolean sendStanza(String string);

}
