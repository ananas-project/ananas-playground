package ananas.app.droid_location_monitor;

import ananas.app.droid_location_monitor.core.DefaultCoreFactory;
import ananas.app.droid_location_monitor.core.ICoreFactory;
import ananas.app.droid_location_monitor.core.IDLMBinder;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DLMonitorService extends Service {

	private IDLMBinder mCore;

	@Override
	public void onCreate() {
		super.onCreate();

		ICoreFactory cf = new DefaultCoreFactory();
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

		// this.startForeground(id, notification);

		this.setForeground(true);

	}
}
