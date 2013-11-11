package kun.tomato_timer;

public class TheTimer {

	private Runnable _cur_runn;
	private int _offset = 0;
	private int _interval = (3600 * 1000);
	private double _working_percent = 0.8;
	private int _state = State.init;
	private final Listener _listener_in;
	private final Listener _listener_out;

	public interface State {
		int init = 0;
		int working = 1;
		int pause = 2;
	}

	public interface Listener {

		void onStateChanged(TheTimer timer, int state);

		void onTick(TheTimer timer);
	}

	private class MyListener implements Listener {

		@Override
		public void onStateChanged(TheTimer timer, int state) {
			try {
				TheTimer.this._listener_out.onStateChanged(timer, state);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onTick(TheTimer timer) {
			try {
				TheTimer.this._listener_out.onTick(timer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TheTimer(Listener l) {
		this._listener_out = l;
		this._listener_in = new MyListener();
	}

	public void start() {
		Runnable runn = new MyRunn();
		this._cur_runn = runn;
		(new Thread(runn)).start();
	}

	public void stop() {
		this._cur_runn = null;
	}

	class MyRunn implements Runnable {

		@Override
		public void run() {

			final TheTimer timer = TheTimer.this;

			for (; this.equals(timer._cur_runn);) {

				final long now = System.currentTimeMillis();
				long pos = (now + _offset) % _interval;
				long working = (long) (_interval * _working_percent);
				final int state = (pos < working) ? State.working : State.pause;
				int to = (int) (_working_percent * 100);

				{
					System.out.println(__time_to_str(pos) + "/"
							+ __time_to_str(_interval) + "="
							+ ((pos * 100) / _interval) + "% to " + to + "%");
				}

				if (_state != state) {
					_state = state;

					javax.swing.SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							timer._listener_in.onStateChanged(timer, state);
						}
					});

				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						timer._listener_in.onTick(timer);
					}
				});

				__safe_slee(1000);

			}

		}

		private String __time_to_str(long pos) {
			return pos + "";
		}

		private void __safe_slee(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
