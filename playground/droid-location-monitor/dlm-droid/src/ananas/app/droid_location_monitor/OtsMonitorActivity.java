package ananas.app.droid_location_monitor;

import ananas.app.ots.v2.AbstractOTSActivity;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.service.OTSServiceBinding;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;

public class OtsMonitorActivity extends AbstractOTSActivity implements
		OnClickListener {

	private TextView mTextOut;
	private Handler handler;
	private Gson gson = new Gson();
	private OTSServiceBinding mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.handler = new Handler();

		mTextOut = (TextView) findViewById(R.id.text_output);
		setupButton(R.id.button_start);
		setupButton(R.id.button_stop);

	}

	private void setupButton(int id) {
		findViewById(id).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_start:
			this.onClickStart();
			break;
		case R.id.button_stop:
			this.onClickStop();
			break;
		default:
			break;
		}
	}

	private void onClickStart() {
		this.getOTSService().start();
		this.refresh(this.mService);
	}

	private void onClickStop() {
		this.getOTSService().stop();
		this.refresh(this.mService);
	}

	@Override
	protected void onOTSServiceBind(OTSServiceBinding binder) {
		super.onOTSServiceBind(binder);
		this.mService = binder;
		this.refresh(binder);
		this.onTimer(1000);
	}

	private void refresh(OTSServiceBinding serv) {
		OTSServiceStatus status = serv.getStatus();
		String s = gson.toJson(status);
		// String s = serv.getStatusText();
		this.mTextOut.setText(s);
	}

	private void onTimer(final int ms) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				final OTSServiceBinding serv = mService;
				if (serv != null) {
					try {
						refresh(serv);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						onTimer(ms);
					}
				}

			}
		}, ms);
	}

	@Override
	protected void onOTSServiceUnbind(OTSServiceBinding binder) {
		this.mService = null;
		super.onOTSServiceUnbind(binder);
	}

}
