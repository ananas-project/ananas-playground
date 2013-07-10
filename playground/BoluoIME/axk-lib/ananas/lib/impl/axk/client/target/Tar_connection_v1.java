package ananas.lib.impl.axk.client.target;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.api.IExConnection;
import ananas.lib.axk.api.IExCore;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.event.DefaultDataEvent;
import ananas.lib.axk.event.DefaultPhaseEvent;
import ananas.lib.impl.axk.client.conn.DefaultSocketKit;
import ananas.lib.impl.axk.client.conn.ITxThreadPriority;
import ananas.lib.impl.axk.client.conn.SocketKit;
import ananas.lib.impl.axk.client.conn.SocketKitFactory;
import ananas.lib.impl.axk.client.conn.XmppConnection;
import ananas.lib.impl.axk.client.conn.XmppConnection.DefaultCreateContext;
import ananas.lib.impl.axk.client.conn.XmppConnectionListener;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class Tar_connection_v1 extends Tar_abstractClient implements
		IExConnection {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private XmppStatus mStatus = XmppStatus.init;
	private Worker mCurWorker;

	public Tar_connection_v1() {
	}

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass) {

		if (apiClass.equals(IExConnection.class)) {
			IExConnection api = this;
			return api;
		} else {
			return super.getExAPI(apiClass);
		}
	}

	@Override
	public void setStatus(XmppStatus phase) {

		if (phase == null) {
			return;
		} else if (phase.equals(XmppStatus.online)) {
			this.connect();
		} else if (phase.equals(XmppStatus.offline)) {
			this.disconnect();
		} else if (phase.equals(XmppStatus.closed)) {
			this.close();
		}
	}

	@Override
	public XmppStatus getStatus() {
		return this.mStatus;
	}

	@Override
	public XmppStatus getPhase() {
		Worker wkr = this.mCurWorker;
		if (wkr == null) {
			return XmppStatus.offline;
		} else {
			return wkr.getPhase();
		}
	}

	@Override
	public void reset() {
		this.mStatus = XmppStatus.init;
		this._setCurWorker(null);
	}

	@Override
	public void close() {
		this.mStatus = XmppStatus.closed;
		this._setCurWorker(null);
	}

	@Override
	public void disconnect() {
		if (!this.mStatus.equals(XmppStatus.closed)) {
			this.mStatus = XmppStatus.offline;
			this._setCurWorker(null);
		}
	}

	@Override
	public void connect() {
		if (!this.mStatus.equals(XmppStatus.closed)) {
			if (!this.mStatus.equals(XmppStatus.online)) {
				this.mStatus = XmppStatus.online;
				Worker wkr = new Worker();
				this._setCurWorker(wkr);
			}
		}
	}

	private void _setCurWorker(Worker wkr) {
		Worker pold;
		synchronized (this) {
			pold = this.mCurWorker;
			this.mCurWorker = wkr;
		}
		if (pold != null) {
			pold.close();
		}
		if (wkr != null) {
			wkr.open();
		}
	}

	interface IRunLoop extends ITxThreadPriority {

		void run();

		void addTask(Runnable task, int priority);

		void addTask(Runnable task);

		void removeAllTask();
	}

	static class MyRunLoopTaskContext {

		final static String op_push = "push";
		final static String op_pop = "pop";
		final static String op_clear = "clear";
		private final int mPriority;
		private final Runnable mTask;
		private final String mOp;

		MyRunLoopTaskContext next;

		public MyRunLoopTaskContext(String op, Runnable task, int priority) {
			this.mOp = op;
			this.mTask = task;
			this.mPriority = priority;
		}

		public MyRunLoopTaskContext(String op) {
			this.mOp = op;
			this.mPriority = ITxThreadPriority.priority_normal;
			this.mTask = null;
		}

		public void execute_safe() {
			try {
				this.mTask.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class MyRunLoop implements IRunLoop {

		private final MyRunLoopTaskContext mContPop = new MyRunLoopTaskContext(
				MyRunLoopTaskContext.op_pop);
		private final MyRunLoopTaskContext mContClear = new MyRunLoopTaskContext(
				MyRunLoopTaskContext.op_clear);
		private final Object mLocker = new Object();

		MyRunLoopTaskContext mHead;
		MyRunLoopTaskContext mTail;

		@Override
		public void run() {
			try {
				synchronized (this.mLocker) {
					this.mLocker.wait(1000 * 20);
				}
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			MyRunLoopTaskContext cont = this.mContPop;
			cont = this.taskIO(cont);
			if (cont != null) {
				cont.execute_safe();
			}
		}

		private synchronized MyRunLoopTaskContext taskIO(
				MyRunLoopTaskContext cont) {
			final String op = cont.mOp;
			if (MyRunLoopTaskContext.op_clear == op) {
				this.mHead = null;
				this.mTail = null;
			} else if (MyRunLoopTaskContext.op_push == op) {
				this._doPush(cont);
			} else if (MyRunLoopTaskContext.op_pop == op) {
				return this.doPop();
			}
			return null;
		}

		private MyRunLoopTaskContext doPop() {
			MyRunLoopTaskContext cont = this.mHead;
			if (cont != null) {
				MyRunLoopTaskContext next = cont.next;
				this.mHead = next;
				if (next == null) {
					this.mTail = null;
				}
			}
			return cont;
		}

		private void _doPush(MyRunLoopTaskContext cont) {

			MyRunLoopTaskContext end = this.mTail;
			if (end == null) {
				this.mHead = cont;
				this.mTail = cont;
				cont.next = null;
				return;
			} else {
				if (end.mPriority <= cont.mPriority) {
					this.mTail.next = cont;
					this.mTail = cont;
					cont.next = null;
					return;
				}
			}

			MyRunLoopTaskContext prev = null;
			MyRunLoopTaskContext ptr = this.mHead;
			for (; ptr != null; ptr = ptr.next) {
				if (cont.mPriority < ptr.mPriority) {
					if (prev == null) {
						this.mHead = cont;
					} else {
						prev = cont;
					}
					cont.next = ptr;
					break;
				}
				prev = ptr;
			}
			if (ptr == null) {
				if (prev == null) {
					this.mTail = cont;
					this.mHead = cont;
				} else {
					this.mTail = cont;
				}
				cont.next = null;
			}
		}

		@Override
		public void addTask(Runnable task, int priority) {
			String op = MyRunLoopTaskContext.op_push;
			MyRunLoopTaskContext cont = new MyRunLoopTaskContext(op, task,
					priority);
			this.taskIO(cont);
			this.doNotify();
		}

		private void doNotify() {
			synchronized (this.mLocker) {
				this.mLocker.notify();
			}
		}

		@Override
		public void addTask(Runnable task) {
			String op = MyRunLoopTaskContext.op_push;
			MyRunLoopTaskContext cont = new MyRunLoopTaskContext(op, task,
					IRunLoop.priority_normal);
			this.taskIO(cont);
			this.doNotify();
		}

		@Override
		public void removeAllTask() {
			MyRunLoopTaskContext cont = this.mContClear;
			this.taskIO(cont);
			this.doNotify();
		}

	}

	class MyWorkerTxRunner implements Runnable {

		private final Worker mWorker;

		public MyWorkerTxRunner(Worker worker) {
			this.mWorker = worker;
		}

		@Override
		public void run() {
			final String myName = Thread.currentThread().getName();
			logger.trace(myName + " begin");
			try {
				this.mWorker._do_run_tx();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.trace(myName + " end");
		}
	}

	static int s_CountWorker = 1;

	class Worker implements Runnable, XmppConnectionListener {

		private boolean mIsClose = false;
		private XmppStatus mPhase = XmppStatus.init;
		private int mRetryDelayMS;
		private Thread mThread;
		private XmppConnection mCurConn0;// the root
		private XmppConnection mCurConn1;// the top
		private final MyRunLoop mTxRunLoop = new MyRunLoop();

		public Worker() {
		}

		@Override
		public void run() {
			final String myName = Thread.currentThread().getName();
			logger.trace(myName + " begin");
			final Thread txThread = new Thread(new MyWorkerTxRunner(this));
			txThread.setName(myName + "(Tx)");
			txThread.start();
			try {
				this._do_run_rx();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				txThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.trace(myName + " exit");
		}

		private void _do_run_tx() {
			for (; !this.mIsClose;) {
				this.mTxRunLoop.run();
			}
		}

		private void _do_run_rx() {

			for (; !this.mIsClose;) {

				// delay to conn
				int timeout = this.mRetryDelayMS;
				final int step = 1000;
				for (; !this.mIsClose;) {
					if (timeout > 0) {

						System.out.println("re-connect after " + timeout
								+ " ms");

						int ms = Math.min(timeout, step);
						this._safe_sleep(ms);
						timeout -= ms;
					} else {
						break;
					}
				}

				if (this.mIsClose) {
					this._setCurrentPhase(XmppStatus.closed);
					break;
				}
				// this.setCurPhase(XmppStatus.logining);
				IExCore api = Tar_connection_v1.this.getCoreApi();
				DefaultCreateContext cc = new XmppConnection.DefaultCreateContext();
				cc.mAccount = api.getAccount();
				cc.mEnvironment = api.getEnvironment();
				cc.mListener = this;
				cc.mSocketKitFactory = new MySocketKitFactory(
						api.getEnvironment(), api.getAccount());
				final XmppConnection conn = new XmppConnection(cc);
				this.mCurConn0 = conn;
				this.mTxRunLoop.removeAllTask();
				this.onSetCurrentPhase(XmppStatus.connect);
				{
					conn.run();
				}
				if (this.mIsClose) {
					this._setCurrentPhase(XmppStatus.closed);
					break;
				} else {
					this._setCurrentPhase(XmppStatus.dropped);
				}
				Exception lastError = conn.getLastError();
				this.mCurConn0 = null;
				if (lastError == null) {
					this.mRetryDelayMS = 0;
				} else {
					System.err.println("exit cause of : " + lastError);
					int val = this.mRetryDelayMS * 2;
					if (val < 1000)
						val = 1000;
					if (val <= (3600 * 1000))
						this.mRetryDelayMS = val;
				}

			}

		}

		private void _safe_sleep(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*
		 * private void setCurPhase(XmppStatus status) { XmppStatus old;
		 * synchronized (this) { old = this.mPhase; this.mPhase = status; } if
		 * (old != status) { Tar_connection.this.onPhaseChanged(this, old,
		 * status); } }
		 */
		public XmppStatus getPhase() {
			return this.mPhase;
		}

		public void close() {
			this.mIsClose = true;// set this woker is closed
			this.mTxRunLoop.addTask(new Runnable() {

				@Override
				public void run() {
					try {
						XmppConnection conn = Worker.this.mCurConn0;
						if (conn != null)
							conn.syncClose();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, IRunLoop.priority_top);
		}

		public void open() {
			Thread thd = this.mThread;
			if (thd == null) {
				thd = new Thread(this);
				thd.setName("axk-worker-" + (s_CountWorker++));
				this.mThread = thd;
			}
			thd.start();
		}

		@Override
		public void onReceive(XmppConnection conn, Object object) {
			// System.out.println(this + ".onResceive:" + object);
			Tar_connection_v1.this._onReceiveObject(this, conn, object);
		}

		@Override
		public void onSetCurrentConnection(XmppConnection conn) {
			this.mCurConn1 = conn;
		}

		@Override
		public void invokeWithTxThread(XmppConnection conn, Runnable runn,
				int priority) {
			if (conn.equals(this.mCurConn1)) {
				Worker.this.mTxRunLoop.addTask(runn, priority);
			}
		}

		@Override
		public void onSetCurrentPhase(XmppStatus phase) {
			this._setCurrentPhase(phase);
		}

		private void _setCurrentPhase(XmppStatus phase) {
			if (phase == null) {
				return;
			}
			XmppStatus old;
			synchronized (this) {
				old = this.mPhase;
				this.mPhase = phase;
			}
			if (!old.equals(phase)) {
				Tar_connection_v1.this.onPhaseChanged(this, old, phase);
			}
		}
	}

	public void onPhaseChanged(Worker worker, XmppStatus oldPhase,
			XmppStatus newPhase) {

		logger.info(worker + ".onPhaseChanged: " + oldPhase + " -> " + newPhase);

		final Worker curWkr = this.mCurWorker;
		if (curWkr != null) {
			if (!curWkr.equals(worker)) {
				return;
			}
		}

		XmppClient client = this._getEventClient();
		DefaultPhaseEvent event = new DefaultPhaseEvent(client, oldPhase,
				newPhase);
		this.onEvent(event);
	}

	private XmppClient _getEventClient() {
		return this;
	}

	public void _onReceiveObject(Worker worker, XmppConnection conn,
			Object object) {

		if (worker.equals(this.mCurWorker)) {
			XmppClient client = this._getEventClient();
			DefaultDataEvent event = new DefaultDataEvent(client, object);
			this.onEvent(event);
		}
	}

	public IExCore getCoreApi() {
		return (IExCore) this.getExAPI(IExCore.class);
	}

	static class MySocketKitFactory implements SocketKitFactory {

		private final XmppAccount mAccount;
		private final XmppEnvironment mEnvi;

		public MySocketKitFactory(XmppEnvironment envi, XmppAccount account) {
			this.mAccount = account;
			this.mEnvi = envi;
		}

		@Override
		public SocketKit createSocketKit() throws IOException {
			final SocketFactory sf;
			if (this.mAccount.isUseSSL()) {
				boolean ignoreError = this.mAccount.isIgnoreSSLError();
				SSLContext context = this.mEnvi.getSecurityManager()
						.getSSLContext(ignoreError);
				sf = context.getSocketFactory();
			} else {
				sf = SocketFactory.getDefault();
			}
			Socket sock = sf.createSocket();
			String host = this.mAccount.getHost();
			int port = this.mAccount.getPort();
			SocketAddress addr = new InetSocketAddress(host, port);
			sock.connect(addr, 30 * 1000);
			return new DefaultSocketKit(sock);
		}
	}

	@Override
	public boolean sendStanza(byte[] buffer, int offset, int length) {
		Worker worker = this.mCurWorker;
		if (worker == null) {
			return false;
		} else {
			byte[] ba = new byte[length];
			for (int i = length - 1; i >= 0; i--) {
				ba[i] = buffer[offset + i];
			}
			MyTaskToSendStanza task = new MyTaskToSendStanza(worker, ba);
			worker.mTxRunLoop.addTask(task);
			return true;
		}
	}

	class MyTaskToSendStanza implements Runnable {

		private Worker mWorker;
		private byte[] mData;

		public MyTaskToSendStanza(Worker worker, byte[] ba) {
			this.mWorker = worker;
			this.mData = ba;
		}

		@Override
		public void run() {
			try {
				this.mWorker.mCurConn1.syncSendBytes(this.mData, 0,
						this.mData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean sendStanza(String string) {
		byte[] ba;
		try {
			ba = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return this.sendStanza(ba, 0, ba.length);
	}

	@Override
	public XmppAddress getBindingJID() {
		// TODO Auto-generated method stub
		return null;
	}
}
