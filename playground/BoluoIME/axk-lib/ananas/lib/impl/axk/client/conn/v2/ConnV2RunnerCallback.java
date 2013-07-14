package ananas.lib.impl.axk.client.conn.v2;

import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Element;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.constant.XmppStatus;

public interface ConnV2RunnerCallback {

	ConnV2Token getToken();

	XmppAccount getAccount();

	void onStanza(Element element);

	void onStatusChanged(XmppStatus oldStatus, XmppStatus newStatus);

	void onStatusOnline(String fullJID, InputStream in, OutputStream out);

}
