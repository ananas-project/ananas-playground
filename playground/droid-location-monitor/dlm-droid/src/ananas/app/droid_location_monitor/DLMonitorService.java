package ananas.app.droid_location_monitor;

import ananas.app.droid_location_monitor.core.ICoreFactory;
import ananas.app.droid_location_monitor.core.IDLMBinder;
import ananas.app.droid_location_monitor.core.SmartCoreFactory;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DLMonitorService extends Service {

	private IDLMBinder mCore;

	@Override
	public void onCreate() {
		super.onCreate();

		// ICoreFactory cf = new DefaultCoreFactory();
		ICoreFactory cf = new SmartCoreFactory();

		this.mCore = cf.newCore(this);
		this.mCore.load();
		if (this.mCore.isRunning()) {
			this._setFg(true);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return this.mBinder;
	}

	private final MyBinder mBinder = new MyBinder();

	private class MyBinder extends Binder implements IDLMBinder {

		@Override
		public void stop() {
			DLMonitorService.this.mCore.stop();
			DLMonitorService.this._setFg(false);
			DLMonitorService.this.mCore.save();
		}

		@Override
		public void start() {
			DLMonitorService.this.mCore.start();
			DLMonitorService.this._setFg(true);
			DLMonitorService.this.mCore.save();
		}

		@Override
		public String getStatusInfo() {
			return DLMonitorService.this.mCore.getStatusInfo();
		}

		@Override
		public void load() {
			DLMonitorService.this.mCore.load();
		}

		@Override
		public void save() {
			DLMonitorService.this.mCore.save();
		}

		@Override
		public boolean isRunning() {
			return DLMonitorService.this.mCore.isRunning();
		}
	}

	public void _setFg(boolean fg) {

		if (!fg) {
			this.stopForeground(true);
			return;
		}

		String textInfo = "";
		Notification notification = new Notification(R.drawable.ic_launcher,
				textInfo, System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		notification.setLatestEventInfo(this,
				this.getString(R.string.app_name), textInfo, contentIntent);
		this.startForeground(1, notification);

	}
}
