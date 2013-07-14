package ananas.lib.impl.axk.client.conn.v2;

import java.util.ArrayList;
import java.util.List;

public class ConnV2TxRunnable implements Runnable {

	private final List<Runnable> m_list;
	private boolean m_is_close;
	private final Object m_signal = new Object();

	public ConnV2TxRunnable(ConnV2Runner runner) {
		this.m_list = new ArrayList<Runnable>();
	}

	@Override
	public void run() {
		for (; !this.m_is_close;) {
			try {
				Runnable task = this.__taskIO(false, null, 0);
				if (task == null) {
					synchronized (this.m_signal) {
						this.m_signal.wait(300 * 1000);
						// Thread.sleep(500);
					}
				} else {
					task.run();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		this.m_is_close = true;
		Runnable runn = new Runnable() {

			@Override
			public void run() {
				// a null task
			}
		};
		this.addTask(runn, 0);
	}

	public boolean addTask(Runnable task, int pos) {
		task = this.__taskIO(true, task, pos);

		synchronized (this.m_signal) {
			this.m_signal.notifyAll();
		}

		if (task == null) {
			if (this.m_is_close) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private synchronized Runnable __taskIO(boolean input, Runnable task, int pos) {

		if (input) {
			if (pos < 0) {
				pos = 0x10000;
			}
			int len = this.m_list.size();
			if (pos > len) {
				pos = len;
			}
			this.m_list.add(pos, task);
			return null;
		} else {
			if (this.m_list.size() > 0) {
				return this.m_list.remove(0);
			} else {
				return null;
			}
		}
	}

}
