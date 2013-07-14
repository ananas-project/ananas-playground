package ananas.lib.impl.axk.client.conn.v2;

import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Element;

import ananas.lib.axk.DefaultXmppAddress;
import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.engine.XAccount;

public class ConnV2Runner {

	private boolean m_isStart;
	private boolean m_isStop;
	private final ConnV2TxRunnable m_tx_runn;
	private final ConnV2RxRunnable m_rx_runn;
	private final ConnV2RunnerCallback m_callback;
	protected InputStream m_in;
	protected OutputStream m_out;

	public ConnV2Runner(ConnV2RunnerCallback callback) {
		this.m_callback = callback;
		this.m_rx_runn = new ConnV2RxRunnable(this);
		this.m_tx_runn = new ConnV2TxRunnable(this);
	}

	public void close() {
		this.m_isStop = true;
		ConnV2TxRunnable tx = this.m_tx_runn;
		ConnV2RxRunnable rx = this.m_rx_runn;
		if (rx != null) {
			rx.close();
		}
		if (tx != null) {
			tx.close();
		}

		try {
			this.m_in.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		try {
			this.m_out.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void open() {
		if (this.m_isStop) {
			return;
		}
		if (this.m_isStart) {
			return;
		} else {
			this.m_isStart = true;
		}
		int id = this.hashCode();
		Runnable rx = new MyRunnableProxy(this.m_rx_runn, "Rx" + id);
		Runnable tx = new MyRunnableProxy(this.m_tx_runn, "Tx" + id);
		(new Thread(rx)).start();
		(new Thread(tx)).start();
	}

	public boolean sendStanza(byte[] buffer, int offset, int length) {
		ConnV2TxBuffer txBuff = new ConnV2TxBuffer(buffer, offset, length);
		txBuff.setOutputAgent(this.__getOutputAgent());
		Runnable runn = txBuff.getRunnable();
		return this.m_tx_runn.addTask(runn, -1);
	}

	private ConnV2OutputAgent __getOutputAgent() {
		return this.m_OutputAgent;
	}

	private final ConnV2OutputAgent m_OutputAgent = new ConnV2OutputAgent() {

		@Override
		public OutputStream getOutputStream() {
			return ConnV2Runner.this.m_out;
		}
	};
	protected XmppAddress m_bind_jid;

	public XmppStatus getPhase() {
		return this.m_rx_runn.getStatus();
	}

	public XAccount getXAccount() {
		XmppAccount acc1 = m_callback.getAccount();
		return new ConnV2AccountWrapper(acc1);
	}

	public ConnV2RunnerCallback getCallback() {
		return this.m_callback_proxy;
	}

	private final ConnV2RunnerCallback m_callback_proxy = new ConnV2RunnerCallback() {

		@Override
		public XmppAccount getAccount() {
			ConnV2Runner pthis = ConnV2Runner.this;
			return pthis.m_callback.getAccount();
		}

		@Override
		public void onStanza(Element element) {
			ConnV2Runner pthis = ConnV2Runner.this;
			pthis.m_callback.onStanza(element);
		}

		@Override
		public void onStatusChanged(XmppStatus oldStatus, XmppStatus newStatus) {
			ConnV2Runner pthis = ConnV2Runner.this;
			if (oldStatus.equals(XmppStatus.online)) {
				pthis.m_bind_jid = null;
				pthis.m_in = null;
				pthis.m_out = null;
			}
			pthis.m_callback.onStatusChanged(oldStatus, newStatus);
		}

		@Override
		public void onStatusOnline(String fullJID, InputStream in,
				OutputStream out) {
			ConnV2Runner pthis = ConnV2Runner.this;
			pthis.m_callback.onStatusOnline(fullJID, in, out);

			pthis.m_in = in;
			pthis.m_out = out;
			pthis.m_bind_jid = new DefaultXmppAddress(fullJID);
		}

		@Override
		public ConnV2Token getToken() {
			ConnV2Runner pthis = ConnV2Runner.this;
			return pthis.m_callback.getToken();
		}
	};

	class MyRunnableProxy implements Runnable {

		private String m_name;
		private Runnable m_runn;

		public MyRunnableProxy(Runnable inner, String name) {
			this.m_runn = inner;
			this.m_name = name;
		}

		@Override
		public void run() {
			this.__log("begin");
			this.m_runn.run();
			this.__log("end");
		}

		private void __log(String op) {
			Thread thd = Thread.currentThread();
			String s = "[" + thd.getName() + "." + this.m_name + "." + op + "]";
			System.out.println(s);
		}
	}

	public XmppAddress getBindingJID() {
		return this.m_bind_jid;
	}
}
