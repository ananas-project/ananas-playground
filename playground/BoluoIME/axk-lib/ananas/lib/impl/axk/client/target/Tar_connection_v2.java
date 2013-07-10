package ananas.lib.impl.axk.client.target;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Element;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppEvent;
import ananas.lib.axk.api.IExConnection;
import ananas.lib.axk.api.IExCore;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.event.DefaultPhaseEvent;
import ananas.lib.axk.event.DefaultStanzaEvent;
import ananas.lib.impl.axk.client.conn.v2.ConnV2Runner;
import ananas.lib.impl.axk.client.conn.v2.ConnV2RunnerCallback;
import ananas.lib.impl.axk.client.conn.v2.ConnV2Token;

public class Tar_connection_v2 extends Tar_abstractClient implements
		IExConnection {

	private XmppStatus mStatus = XmppStatus.init;
	private boolean m_isClosed;
	private ConnV2Runner m_curRunner;

	public Tar_connection_v2() {
		this.reset();
	}

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass) {
		if (IExConnection.class.equals(apiClass)) {
			return this;
		} else {
			return super.getExAPI(apiClass);
		}
	}

	@Override
	public void setStatus(XmppStatus phase) {
		if (phase != null) {
			this.mStatus = phase;
		}
		if (phase == null) {
			return;
		} else if (phase.equals(XmppStatus.init)) {
			this.doReset();
		} else if (phase.equals(XmppStatus.online)) {
			this.doConnect();
		} else if (phase.equals(XmppStatus.offline)) {
			this.doDisconnect();
		} else if (phase.equals(XmppStatus.closed)) {
			this.doClose();
		} else {
		}
	}

	@Override
	public XmppStatus getStatus() {
		return this.mStatus;
	}

	@Override
	public XmppStatus getPhase() {
		ConnV2Runner runner = this.m_curRunner;
		if (runner == null) {
			return XmppStatus.closed;
		} else {
			return runner.getPhase();
		}
	}

	private void doReset() {
		this.m_isClosed = false;
		this.doDisconnect();
	}

	private void doClose() {
		this.m_isClosed = true;
		this.doDisconnect();
	}

	private void doConnect() {
		if (this.m_isClosed) {
			return;
		}
		ConnV2Token token = new ConnV2Token();
		ConnV2RunnerCallback cbk = new MyRunnerCallback(token);
		ConnV2Runner pnew = new ConnV2Runner(cbk);
		ConnV2Runner pold = this.__swapCurrentRunner(pnew);
		if (pold != null) {
			pold.close();
		}
		if (pnew != null) {
			pnew.open();
		}
	}

	private void doDisconnect() {
		ConnV2Runner pnew = null;
		ConnV2Runner pold = this.__swapCurrentRunner(pnew);
		if (pold != null) {
			pold.close();
		}
	}

	@Override
	public void reset() {
		this.setStatus(XmppStatus.init);
	}

	@Override
	public void close() {
		this.setStatus(XmppStatus.closed);
	}

	@Override
	public void connect() {
		this.setStatus(XmppStatus.online);
	}

	@Override
	public void disconnect() {
		this.setStatus(XmppStatus.offline);
	}

	private synchronized ConnV2Runner __swapCurrentRunner(ConnV2Runner pnew) {
		ConnV2Runner ret = this.m_curRunner;
		this.m_curRunner = pnew;
		return ret;
	}

	class MyRunnerCallback implements ConnV2RunnerCallback {

		private final ConnV2Token m_token;

		public MyRunnerCallback(ConnV2Token token) {
			this.m_token = token;
		}

		@Override
		public ConnV2Token getToken() {
			return this.m_token;
		}

		@Override
		public XmppAccount getAccount() {
			IExCore core = (IExCore) Tar_connection_v2.this
					.getExAPI(IExCore.class);
			return core.getAccount();
		}

		@Override
		public void onStanza(Element element) {
			// System.out.println(this + ".onStanza:" + element);
			Tar_connection_v2 pthis = Tar_connection_v2.this;
			DefaultStanzaEvent event = new DefaultStanzaEvent(pthis, element);
			this.__dispEvent(event);
		}

		@Override
		public void onStatusChanged(XmppStatus oldStatus, XmppStatus newStatus) {
			// System.out.println(this + ".onStatusChanged:" + newStatus);
			Tar_connection_v2 pthis = Tar_connection_v2.this;
			XmppEvent event = new DefaultPhaseEvent(pthis, oldStatus, newStatus);
			this.__dispEvent(event);
		}

		private void __dispEvent(XmppEvent event) {
			try {
				Tar_connection_v2 pthis = Tar_connection_v2.this;
				pthis.onEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStatusOnline(String fullJID, InputStream in,
				OutputStream out) {
			// System.out.println(this + ".onStatusOnline:" + fullJID);
		}
	}

	@Override
	public boolean sendStanza(byte[] buffer, int offset, int length) {
		ConnV2Runner runner = this.m_curRunner;
		if (runner == null) {
			return false;
		} else {
			return runner.sendStanza(buffer, offset, length);
		}
	}

	@Override
	public boolean sendStanza(String string) {
		try {
			byte[] buff = string.getBytes("UTF-8");
			return this.sendStanza(buff, 0, buff.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public XmppAddress getBindingJID() {
		ConnV2Runner runner = this.m_curRunner;
		if (runner == null) {
			return null;
		}
		return runner.getBindingJID();
	}

}
