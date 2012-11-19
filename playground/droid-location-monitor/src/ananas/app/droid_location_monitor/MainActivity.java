package ananas.app.droid_location_monitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements ActivityAgent {

	private DLMServiceConnector mServiceConn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mServiceConn = new DLMServiceConnector(this);

		this._addClickListener(R.id.button_refresh);
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
			break;
		}
		case R.id.button_stop: {
			this.mServiceConn.getBinder().stop();
			break;
		}
		case R.id.button_refresh: {
			this._doRefresh();
			break;
		}
		default:
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
	protected void onPause() {
		super.onPause();
		this.mServiceConn.disconnect();
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
	}

	@Override
	public void onServiceDisconnected() {
		// TODO Auto-generated method stub

	}

}
