package ananas.lib.impl.axk.client.conn;

import ananas.lib.axk.element.stream.Xmpp_stream;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class TheOnlineConnCtrl extends XmppConnectionController {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	public TheOnlineConnCtrl(XmppConnection conn) {
		super(conn);
	}

	@Override
	public void onReceive(Xmpp_stream stream, Object object) {
		logger.info("onReceive:" + object);
		XmppConnection conn = this.getConnection();
		XmppConnectionListener list = conn.getListener();
		list.onReceive(conn, object);
	}

}
