package ananas.app.droid_location_monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements ActivityAgent {

	private DLMServiceConnector mServiceConn;
	private Runnable mTimerRunn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mServiceConn = new DLMServiceConnector(this);

		// this._addClickListener(R.id.button_refresh);
		this._addClickListener(R.id.button_stop);
		this._addClickListener(R.id.button_start);
	}

	private void _addClickListener(int id) {
		this.findViewById(id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				_onClick(view);
			}
		});
	}

	private void _onClick(View view) {
		switch (view.getId()) {
		case R.id.button_start: {
			this.mServiceConn.getBinder().start();
			this._startTimer(true);
			break;
		}
		case R.id.button_stop: {
			this.mServiceConn.getBinder().stop();
			this._startTimer(false);
			break;
		}
		/*
		 * case R.id.button_refresh: { this._doRefresh(); break; }
		 */
		default:
		}
	}

	private final Runnable mOnTimerRunn = new Runnable() {

		@Override
		public void run() {
			MainActivity.this._doRefresh();
		}
	};

	private void _startTimer(boolean run) {
		if (run) {
			Runnable runn = new Runnable() {

				@Override
				public void run() {
					final MainActivity self = MainActivity.this;
					for (;;) {
						if (!this.equals(self.mTimerRunn)) {
							return;
						}
						this._safe_sleep(1000);
						self.runOnUiThread(self.mOnTimerRunn);
					}
				}

				private void _safe_sleep(int ms) {
					try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			this.mTimerRunn = runn;
			(new Thread(runn)).start();
		} else {
			this.mTimerRunn = null;
		}
	}

	private void _doRefresh() {
		String info = this.mServiceConn.getBinder().getStatusInfo();
		TextView txt = (TextView) this.findViewById(R.id.text_output);
		txt.setText(info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_dtime_adjust: {
			Class<?> cls = DateTimeAdjustActivity.class;
			this.startActivity(new Intent(this, cls));
			break;
		}
		case R.id.menu_settings:
			break;
		default:
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mServiceConn.disconnect();
		this._startTimer(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mServiceConn.connect();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onServiceConnected() {
		this._doRefresh();
		if (this.mServiceConn.getBinder().isRunning()) {
			this._startTimer(true);
		}
	}

	@Override
	public void onServiceDisconnected() {
		this._startTimer(false);
	}

}
