package ananas.app.ots.v2;

import ananas.app.droid_location_monitor.OtsService;
import ananas.app.ots.v2.service.OTSServiceBinding;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class AbstractOTSActivity extends Activity {

	private myServiceConn mServiceConn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mServiceConn = new myServiceConn();
		Intent service = new Intent(this, OtsService.class);
		this.startService(service);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent service = new Intent(this, OtsService.class);
		ServiceConnection conn = this.mServiceConn;
		int flags = Context.BIND_AUTO_CREATE;
		this.bindService(service, conn, flags);
	}

	@Override
	protected void onPause() {
		this.unbindService(this.mServiceConn);
		super.onPause();
	}

	protected OTSServiceBinding getOTSService() {
		return this.mServiceConn.mBinder;
	}

	protected void onOTSServiceBind(OTSServiceBinding binder) {
	}

	protected void onOTSServiceUnbind(OTSServiceBinding binder) {
	}

	private class myServiceConn implements ServiceConnection {

		private OTSServiceBinding mBinder;

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			this.mBinder = (OTSServiceBinding) service;
			AbstractOTSActivity.this.onOTSServiceBind(this.mBinder);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			AbstractOTSActivity.this.onOTSServiceUnbind(this.mBinder);
			this.mBinder = null;
		}
	}

}
