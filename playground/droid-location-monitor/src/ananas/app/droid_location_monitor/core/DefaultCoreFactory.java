package ananas.app.droid_location_monitor.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class DefaultCoreFactory implements ICoreFactory {

	@Override
	public IDLMBinder newCore(Context context) {
		return new MyCore(context);
	}

	class MyLocationListener implements LocationListener {

		private Location mLastLocation;
		private int mCount;

		@Override
		public void onLocationChanged(Location arg0) {
			Location location = new Location(arg0);
			this.mLastLocation = location;
			this.mCount++;
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	class MyCore implements IDLMBinder, MonitorRunnableOwner {

		private final Context mContext;
		private final MyLocationListener mLL = new MyLocationListener();
		private Runnable mMonitorRunn = null;

		public MyCore(Context context) {
			this.mContext = context;
		}

		@Override
		public void load() {
			// TODO Auto-generated method stub

		}

		@Override
		public void save() {
			// TODO Auto-generated method stub

		}

		@Override
		public void stop() {
			LocationManager lm = (LocationManager) this.mContext
					.getSystemService(Context.LOCATION_SERVICE);
			lm.removeUpdates(this.mLL);
			this.mMonitorRunn = null;
		}

		@Override
		public void start() {
			LocationManager lm = (LocationManager) this.mContext
					.getSystemService(Context.LOCATION_SERVICE);
			String provider = LocationManager.GPS_PROVIDER;
			long minTime = 1000;
			float minDistance = 10;
			LocationListener listener = this.mLL;
			lm.requestLocationUpdates(provider, minTime, minDistance, listener);
			// monitor thread
			MyMonitorRunn runn = new MyMonitorRunn(this);
			this.mMonitorRunn = runn;
			(new Thread(runn)).start();
		}

		@Override
		public boolean isRunning() {
			return (this.mMonitorRunn != null);
		}

		@Override
		public String getStatusInfo() {

			int count = this.mLL.mCount;
			Location location = this.mLL.mLastLocation;
			boolean isRunning = this.isRunning();

			StringBuilder sb = new StringBuilder();
			sb.append("status:" + (isRunning ? "running" : "stopped") + "\n");
			sb.append("count:" + count + "\n");
			sb.append("location:" + location + "\n");

			return sb.toString();
		}

		@Override
		public boolean isCurrent(Runnable runn) {
			return (runn == this.mMonitorRunn);
		}

		@Override
		public Location getLastLocation() {
			return this.mLL.mLastLocation;
		}
	}

	interface MonitorRunnableOwner {

		boolean isCurrent(Runnable runn);

		Location getLastLocation();

	}

	class MyMonitorRunn implements Runnable {

		private final MonitorRunnableOwner mOwner;
		private final StringBuilder mBuffer;
		private Location mLastLocation;
		private long mLastSaveTime;

		public MyMonitorRunn(MonitorRunnableOwner owner) {
			this.mOwner = owner;
			this.mBuffer = new StringBuilder();
		}

		@Override
		public void run() {
			for (; this.mOwner.isCurrent(this);) {
				try {
					Thread.sleep(1000);
					this.__run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void __run() throws Exception {

			final Location loc = this.mOwner.getLastLocation();
			if (loc != null) {
				if (loc != this.mLastLocation) {
					this.mLastLocation = loc;
					this.mBuffer.append("" + loc);
					this.mBuffer.append("\n");
				}
			}

			if (this.mBuffer.length() > 0) {
				long now = System.currentTimeMillis();
				if ((now - this.mLastSaveTime) > 30000) {
					String out = this.mBuffer.toString();
					this.mBuffer.setLength(0);
					this.mLastSaveTime = now;
					this._saveOutputString(out);
				}
			}

		}

		private void _saveOutputString(String out) throws IOException {
			File file = this._getOutputFile();
			FileOutputStream os = new FileOutputStream(file, true);
			os.write(out.getBytes());
			os.flush();
			os.close();
		}

		private File _getOutputFile() {
			long now = System.currentTimeMillis();
			long iday = now / (1000 * 3600 * 24);
			File dir = android.os.Environment.getExternalStorageDirectory();
			return new File(dir, "ananas/DLM/output_day_" + iday + ".txt");
		}
	}

}
