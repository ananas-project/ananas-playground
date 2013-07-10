package ananas.lib.impl.axk.client.conn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.element.stream.Xmpp_stream;
import ananas.lib.impl.axk.client.parser.DefaultXmppParserFactory;
import ananas.lib.impl.axk.client.parser.XmppParser;
import ananas.lib.impl.axk.client.parser.XmppParserCallback;
import ananas.lib.impl.axk.client.parser.XmppParserFactory;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class XmppConnection implements Runnable {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private Exception mLastError;
	private IXmppConnectionController mCurCtrl;
	private boolean mIsFirstTime = true;
	private SocketKit mSocketKit;
	private final CreateContext mCreateContext;

	public static interface CreateContext {

		XmppAccount getAccount();

		XmppEnvironment getEnvironment();

		XmppConnectionListener getListener();

		SocketKitFactory getSocketKitFactory();

		XmppConnection getParent();
	}

	public static class DefaultCreateContext implements CreateContext {

		public XmppConnection mParent;
		public SocketKitFactory mSocketKitFactory;
		public XmppConnectionListener mListener;
		public XmppEnvironment mEnvironment;
		public XmppAccount mAccount;

		public DefaultCreateContext() {
		}

		public DefaultCreateContext(XmppConnection conn) {
			this.mAccount = conn.mCreateContext.getAccount();
			this.mEnvironment = conn.mCreateContext.getEnvironment();
			this.mListener = conn.mCreateContext.getListener();
			this.mParent = conn.mCreateContext.getParent();
			this.mSocketKitFactory = conn.mCreateContext.getSocketKitFactory();
		}

		@Override
		public XmppAccount getAccount() {
			return this.mAccount;
		}

		@Override
		public XmppEnvironment getEnvironment() {
			return this.mEnvironment;
		}

		@Override
		public XmppConnectionListener getListener() {
			return this.mListener;
		}

		@Override
		public SocketKitFactory getSocketKitFactory() {
			return this.mSocketKitFactory;
		}

		@Override
		public XmppConnection getParent() {
			return this.mParent;
		}

	}

	public XmppConnection(CreateContext cc) {
		this.mCreateContext = cc;
		// //
		this.mCurCtrl = new TheMainConnCtrl(this);
	}

	public void printToken() {
		logger.trace("=========================================================================");
		logger.trace("new " + this);
		Thread thd = Thread.currentThread();
		logger.trace("thread = " + thd + "@" + thd.hashCode());
	}

	@Override
	public void run() {

		this.mCreateContext.getListener().onSetCurrentConnection(this);
		try {

			if (this.mIsFirstTime) {
				this.mIsFirstTime = false;
			} else {
				throw new RuntimeException("one time only!");
			}
			this.printToken();

			final SocketKit kit = this.createSocket();
			this.mSocketKit = kit;
			final InputStream is = kit.getInput();
			final OutputStream os = kit.getOutput();

			String stream_stream = this.getStreamOpenTag(this.mCreateContext
					.getAccount());
			os.write(stream_stream.getBytes());
			os.flush();

			XmppParserFactory xpf = new DefaultXmppParserFactory();
			XmppParser parse = xpf.newParser(this.mCreateContext
					.getEnvironment());
			XmppParserCallback callback = new MyXmppParserCallback();
			MyInputStreamProxy is2 = new MyInputStreamProxy(is);
			parse.parse(is2, callback);

		} catch (Exception e) {

			// e.printStackTrace();

			logger.error(e);
			this.mLastError = e;
		}
		this.mCreateContext.getListener().onSetCurrentConnection(null);

		try {
			this.doClose();
		} catch (Exception e) {
			logger.error(e);
			this.mLastError = e;
		}

	}

	static class MyXmppStreamLogger extends OutputStream {

		private final ByteArrayOutputStream mBAOS = new ByteArrayOutputStream();

		@Override
		public void write(int b) throws IOException {
			if (b == '<') {
				this.newLine();
			}
			this.mBAOS.write(b);
			if (b == '>') {
				this.newLine();
			}
		}

		private void newLine() throws IOException {
			byte[] ba = this.mBAOS.toByteArray();
			this.mBAOS.reset();
			if (ba.length > 0) {
				String prefix = "xmpp_rx : ";
				String line = new String(ba, "UTF-8");
				if (ba[0] == '<') {
					logger.info(prefix + line);
				} else {
					logger.info(prefix + "    " + line);
				}
			}
		}
	}

	static class MyInputStreamProxy extends InputStream {

		private final InputStream mIS;

		private final OutputStream mOS = new MyXmppStreamLogger();

		public MyInputStreamProxy(InputStream is) {
			this.mIS = is;
		}

		@Override
		public int read() throws IOException {

			for (;;) {
				long m1 = System.currentTimeMillis();
				int av = this.mIS.available();
				final int b;
				av = 1;
				if (av > 0)
					b = this.mIS.read();
				else
					b = -1;
				long m2 = System.currentTimeMillis();
				if ((m2 - m1) > 1000) {
					System.out.println("blocked!");
				}
				if (av > 0) {
					this.mOS.write(b);
					return b;
				} else {
					this.doSleep();
				}
			}
		}

		private void doSleep() {

			System.out.println(this.mIS + "");
			// System.out.println("sleep!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String getStreamOpenTag(XmppAccount account) {
		XmppAddress jid = account.getAddress();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0'?>");
		sb.append("<stream:stream");
		sb.append(" from='" + jid.toPureString() + "'");
		sb.append(" to='" + jid.getHost() + "'");
		sb.append(" version='1.0'");
		sb.append(" xml:lang='en'");
		sb.append(" xmlns='jabber:client'");
		sb.append(" xmlns:stream='http://etherx.jabber.org/streams'>");
		return sb.toString();
	}

	private void doClose() {

		SocketKit kit = this.mSocketKit;
		this.mSocketKit = null;

		try {
			InputStream is = kit.getInput();
			if (is != null)
				is.close();
		} catch (Exception e) {
			logger.error(e);
			this.mLastError = e;
		}
		try {
			OutputStream os = kit.getOutput();
			if (os != null)
				os.close();
		} catch (Exception e) {
			logger.error(e);
			this.mLastError = e;
		}
		try {
			Socket sock = kit.getSocket();
			if (sock != null)
				sock.close();
		} catch (Exception e) {
			logger.error(e);
			this.mLastError = e;
		}

	}

	private SocketKit createSocket() throws IOException {
		SocketKit kit = null;
		try {
			kit = this.mCreateContext.getSocketKitFactory().createSocketKit();
			kit.getOutput().write(" ".getBytes());
			kit.getOutput().flush();
			return kit;
		} catch (IOException e) {
			throw e;
		}
	}

	public XmppConnectionListener getListener() {
		return this.mCreateContext.getListener();
	}

	class MyXmppParserCallback implements XmppParserCallback {

		@Override
		public void onReceive(Xmpp_stream stream, Object object) {
			// XmppConnection.this.mListener.onReceive(object);
			XmppConnection.this.mCurCtrl.onReceive(stream, object);
		}
	}

	public void setController(IXmppConnectionController ctrl) {
		if (ctrl == null)
			return;
		this.mCurCtrl = ctrl;
	}

	public Exception getLastError() {
		return this.mLastError;
	}

	public void syncSendBytes(byte[] buffer, int offset, int length)
			throws IOException {

		logger.info(this + ".send " + length + " bytes");

		synchronized (this) {
			OutputStream out = this.mSocketKit.getOutput();
			out.write(buffer, offset, length);
			out.flush();
		}
	}

	public void asyncSendBytes(byte[] buffer, int offset, int length) {

		byte ba[] = new byte[length];
		for (int i = length - 1; i >= 0; i--) {
			ba[i] = buffer[offset + i];
		}

		MyTaskToSendStanza task = new MyTaskToSendStanza(this, ba);
		XmppConnectionListener listener = this.mCreateContext.getListener();
		listener.invokeWithTxThread(this, task,
				XmppConnectionListener.priority_normal);

	}

	static class MyTaskToSendStanza implements Runnable {

		private byte[] mData;
		private XmppConnection mConn;

		public MyTaskToSendStanza(XmppConnection conn, byte[] ba) {
			this.mConn = conn;
			this.mData = ba;
		}

		@Override
		public void run() {
			try {
				this.mConn.syncSendBytes(mData, 0, mData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public XmppAccount getAccount() {
		return this.mCreateContext.getAccount();
	}

	public SocketKit getSocketKit() {
		return this.mSocketKit;
	}

	public XmppEnvironment getEnvironment() {
		return this.mCreateContext.getEnvironment();
	}

	public void syncClose() throws IOException {
		this.mSocketKit.getSocket().close();
	}

	public void setCurrentPhase(XmppStatus phase) {
		this.mCreateContext.getListener().onSetCurrentPhase(phase);
	}
}
