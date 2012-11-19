package ananas.app.droid_location_monitor.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SmartCoreFactory implements ICoreFactory {

	@Override
	public IDLMBinder newCore(Context context) {
		return new MyCore(context);
	}

	class MyCore implements IDLMBinder, TaskContext {

		final String fn_status = "settings/status.json";

		private final Context mContext;
		private RecTask mCurTask;

		public MyCore(Context context) {
			this.mContext = context;
		}

		@Override
		public void load() {
			File file = this.getFile(this.fn_status);
			RecTask task = new RecTask(this);
			if (task.load(file)) {
				this._setCurTask(task);
			}
		}

		private void _setCurTask(final RecTask pnew) {
			final RecTask pold;
			synchronized (this) {
				pold = this.mCurTask;
				this.mCurTask = pnew;
			}
			if (pold != null) {
				pold.stop();
			}
			if (pnew != null) {
				pnew.start();
			}
		}

		private File getFile(String filename) {
			File dir = android.os.Environment.getExternalStorageDirectory();
			dir = new File(dir, "ananas/droid-location-monitor");
			return new File(dir, filename);
		}

		@Override
		public void save() {
			File file = this.getFile(this.fn_status);
			RecTask task = this.mCurTask;
			if (task == null) {
				if (file.exists())
					file.delete();
			} else {
				task.save(file);
			}
		}

		@Override
		public void stop() {
			this._setCurTask(null);
		}

		@Override
		public void start() {
			if (this.mCurTask == null) {
				RecTask task = new RecTask(this);
				this._setCurTask(task);
			}
		}

		@Override
		public boolean isRunning() {
			return (this.mCurTask != null);
		}

		@Override
		public String getStatusInfo() {

			RecTask task = this.mCurTask;
			if (task == null) {
				return "status: stopped";
			} else {
				final int cnt = task.getRecCount();
				final long startTime = task.getStartTime();
				final long now = System.currentTimeMillis();

				StringBuilder sb = new StringBuilder();
				sb.append("status: running\n");
				sb.append("\n");
				sb.append("rec count: " + cnt + "\n");
				sb.append("start time: "
						+ TimeUtil.timestampToString(startTime) + "\n");
				sb.append("span time: "
						+ TimeUtil.timespanToString(now - startTime) + "\n");
				sb.append("\n");

				final Location loc = task.getLastLocation();
				if (loc != null) {
					sb.append("latitude: " + loc.getLatitude() + "\n");
					sb.append("longitude: " + loc.getLongitude() + "\n");
					sb.append("altitude: " + loc.getAltitude() + "\n");
					sb.append("accuracy: " + loc.getAccuracy() + "\n");
					sb.append("time: "
							+ TimeUtil.timestampToString(loc.getTime()) + "\n");
					sb.append("bearing: " + loc.getBearing() + "\n");
					sb.append("speed: " + loc.getSpeed() + "\n");
					sb.append("provider: " + loc.getProvider() + "\n");
				}
				return sb.toString();
			}

		}

		@Override
		public Context getContext() {
			return this.mContext;
		}

		@Override
		public File getAppDir() {
			return this.getFile(".");
		}
	}

	interface TaskContext {

		Context getContext();

		File getAppDir();
	}

	class RecTask implements LocationListener {

		private Location mLastLocation;
		private final Vector<Location> mLocationBuffer = new Vector<Location>();
		private int mCountRec;
		private long mStartTime;
		private final TaskContext mTC;
		private boolean mIsStarted;
		private boolean mIsStopped;
		private File mRecFile;
		private List<LocationPeeker> mPeekerList;

		public RecTask(TaskContext tc) {
			this.mTC = tc;
			this.mStartTime = System.currentTimeMillis();
		}

		public Location getLastLocation() {
			return this.mLastLocation;
		}

		public long getStartTime() {
			return this.mStartTime;
		}

		public int getRecCount() {
			return this.mCountRec;
		}

		public void start() {

			if (this.mIsStarted) {
				return;
			}
			this.mIsStarted = true;
			// open gps
			Context context = this.mTC.getContext();
			LocationManager lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			String provider = LocationManager.GPS_PROVIDER;
			long minTime = 1000;
			float minDistance = 5;
			LocationListener listener = this;
			lm.requestLocationUpdates(provider, minTime, minDistance, listener);
			// flush output
			this._flushRecData();
			// start thread
			Thread thd = new Thread(new Runnable() {

				@Override
				public void run() {
					_task_thread_run();
				}
			});
			thd.start();
		}

		private void _task_thread_run() {
			for (; !this.mIsStopped;) {
				try {
					this._flushRecData();
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void _flushRecData() {
			try {
				final File file = this.getOutputFile();
				final List<Location> buf = this.mLocationBuffer;
				final boolean noFile = !file.exists();
				final boolean hasData = !buf.isEmpty();
				if (noFile || hasData) {
					final RecFile rfile = new RecFile(file);
					if (noFile) {
						// set head
						String time = TimeUtil
								.timestampToString(this.mStartTime);
						rfile.setHeaderField("Content-Type", "text/plain");
						rfile.setHeaderField("Create-Time", time);
						rfile.setHeaderField("Create-App", this.getClass()
								.getName());

						String str = this._locationToString(null);
						rfile.append(str + CRLF);
					}
					if (hasData) {
						for (Location loc : buf) {
							String str = this._locationToString(loc);
							rfile.append(str + CRLF);
						}
						buf.clear();
					}
					rfile.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * @param loc
		 *            if loc==null, return field list
		 * */
		private String _locationToString(Location loc) {
			List<LocationPeeker> list = this._getPeekList();
			StringBuilder sb = new StringBuilder();
			for (LocationPeeker peeker : list) {
				String str = peeker.peek(loc);
				sb.append("\"" + str + "\",");
			}
			return sb.toString();
		}

		private List<LocationPeeker> _getPeekList() {
			List<LocationPeeker> list = this.mPeekerList;
			if (list != null)
				return list;
			list = new Vector<LocationPeeker>();
			// begin
			list.add(new LocationPeeker("type") {
				@Override
				String getString(Location loc) {
					return "location";
				}
			});

			list.add(new LocationPeeker("source") {
				@Override
				String getString(Location loc) {
					return "" + loc.getProvider();
				}
			});

			list.add(new LocationPeeker("timestamp") {
				@Override
				String getString(Location loc) {
					return "" + loc.getTime();
				}
			});

			list.add(new LocationPeeker("longitude") {
				@Override
				String getString(Location loc) {
					return "" + loc.getLongitude();
				}
			});
			list.add(new LocationPeeker("latitude") {
				@Override
				String getString(Location loc) {
					return "" + loc.getLatitude();
				}
			});

			list.add(new LocationPeeker("altitude") {
				@Override
				String getString(Location loc) {
					return "" + loc.getAltitude();
				}
			});
			list.add(new LocationPeeker("accuracy") {
				@Override
				String getString(Location loc) {
					return "" + loc.getAccuracy();
				}
			});

			list.add(new LocationPeeker("speed") {
				@Override
				String getString(Location loc) {
					return "" + loc.getSpeed();
				}
			});
			list.add(new LocationPeeker("bearing") {
				@Override
				String getString(Location loc) {
					return "" + loc.getBearing();
				}
			});

			// end
			this.mPeekerList = list;
			return list;
		}

		private File getOutputFile() {
			File file = this.mRecFile;
			if (file == null) {
				String strtime = TimeUtil.timestampToString(this.mStartTime);
				File dir = this.mTC.getAppDir();
				file = new File(dir, "record/dlm_rec_" + strtime + ".txt");
				this.mRecFile = file;
			}
			return file;
		}

		public void stop() {
			if (this.mIsStopped) {
				return;
			}
			this.mIsStopped = true;
			// close gps
			Context context = this.mTC.getContext();
			LocationManager lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			lm.removeUpdates(this);
			// flush
			this._flushRecData();
		}

		public boolean load(File file) {
			// TODO Auto-generated method stub
			return true;
		}

		public void save(File file) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location arg0) {
			Location loc = new Location(arg0);
			this.mLastLocation = loc;
			Vector<Location> buf = this.mLocationBuffer;
			buf.add(loc);
			this.mCountRec++;
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

	abstract class LocationPeeker {

		private final String mField;

		public LocationPeeker(String field) {
			this.mField = field;
		}

		public String peek(Location loc) {
			if (loc == null) {
				return this.mField;
			} else {
				return this.getString(loc);
			}
		}

		abstract String getString(Location loc);
	}

	class RecFile {

		private final File mFile;
		private final StringBuilder mBuffer = new StringBuilder();
		private final Map<String, String> mHeaders = new HashMap<String, String>();

		public RecFile(File file) {
			this.mFile = file;
		}

		public void append(String str) {
			this.mBuffer.append(str);
		}

		public void setHeaderField(String key, String value) {
			this.mHeaders.put(key, value);
		}

		public void close() throws IOException {

			boolean isNew = !this.mFile.exists();
			final OutputStream out;
			if (isNew) {
				this.mFile.getParentFile().mkdirs();
				this.mFile.createNewFile();
				out = new FileOutputStream(this.mFile);
				// flush head
				final StringBuilder sb = new StringBuilder();
				sb.append("HTDF/1.0" + CRLF);
				for (String key : this.mHeaders.keySet()) {
					String value = this.mHeaders.get(key);
					sb.append(key + ":" + value + CRLF);
				}
				sb.append(CRLF);
				out.write(sb.toString().getBytes());
			} else {
				out = new FileOutputStream(this.mFile, true);
			}

			// flush data
			byte[] data = this.mBuffer.toString().getBytes("UTF-8");
			out.write(data);
			out.flush();
			out.close();
		}
	}

	final static String CRLF = "\n\r";

}
