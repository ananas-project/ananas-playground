package ananas.lib.impl.axk.client.conn;

import java.io.IOException;

import ananas.lib.impl.axk.client.conn.XmppConnection.DefaultCreateContext;

public class AbstractSaslConnCtrl extends XmppConnectionController {

	public AbstractSaslConnCtrl(XmppConnection conn) {
		super(conn);
	}

	public void doStartNewStreamOnCurrentSocket() {
		XmppConnection conn1 = this.getConnection();
		DefaultCreateContext cc = new XmppConnection.DefaultCreateContext(conn1);
		cc.mSocketKitFactory = new MySocketKitFactory(conn1.getSocketKit());
		XmppConnection conn2 = new XmppConnection(cc);
		conn2.run();
	}

	static class MySocketKitFactory implements SocketKitFactory {

		private final SocketKit mSK;

		public MySocketKitFactory(SocketKit socketKit) {
			this.mSK = socketKit;
		}

		@Override
		public SocketKit createSocketKit() throws IOException {
			return this.mSK;
		}

	}
}
