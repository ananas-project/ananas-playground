package ananas.app.droid_location_monitor;

import ananas.app.droid_location_monitor.core.IDLMBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DLMServiceConnector {

	private final ActivityAgent mActAgent;
	private final Activity mActivity;
	private final MyConn mConn = new MyConn();

	public DLMServiceConnector(ActivityAgent aa) {
		this.mActAgent = aa;
		this.mActivity = aa.getActivity();

		this.mActivity.startService(new Intent(aa.getActivity(),
				DLMonitorService.class));
	}

	public void disconnect() {
		this.mActivity.unbindService(this.mConn);
	}

	public void connect() {
		Intent intent = new Intent(this.mActivity, DLMonitorService.class);
		this.mActivity
				.bindService(intent, this.mConn, Context.BIND_AUTO_CREATE);
	}

	class MyConn implements IDLMBinder, ServiceConnection {

		IDLMBinder mTarget;

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			this.mTarget = (IDLMBinder) binder;
			DLMServiceConnector.this.mActAgent.onServiceConnected();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			DLMServiceConnector.this.mActAgent.onServiceDisconnected();
			this.mTarget = null;
		}

		@Override
		public void stop() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				tar.stop();
			}
		}

		@Override
		public void start() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				tar.start();
			}
		}

		@Override
		public String getStatusInfo() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				return tar.getStatusInfo();
			}
			return null;
		}

		@Override
		public void load() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				tar.load();
			}
		}

		@Override
		public void save() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				tar.save();
			}
		}

		@Override
		public boolean isRunning() {
			IDLMBinder tar = this.mTarget;
			if (tar != null) {
				return tar.isRunning();
			}
			return false;
		}
	}

	public IDLMBinder getBinder() {
		return this.mConn;
	}
}
