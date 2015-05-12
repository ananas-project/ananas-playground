package ananas.app.ots.v2.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import ananas.app.ots.v2.FileManager;
import ananas.app.ots.v2.pojo.LocationFragmentFormat;
import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.pojo.OTSServiceTask;
import ananas.app.ots.v2.pojo.SateTime;
import ananas.app.ots.v2.tools.StringTools;
import android.app.Service;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.gson.Gson;

public class OTSServiceRuntime {

	private final OTSServiceConfig config;
	private final OTSServiceTask task;
	private final Service service;
	private final Gson gson = new Gson();
	private final List<OTSLocation> locBuffer = new Vector<OTSLocation>();
	private final Queue<OTSLocation> locQueue = new LinkedList<OTSLocation>();
	private final FileManager fileMan = FileManager.Factory.getDefault();

	private final LocationListener mDataListener = new myDataListener();
	private final Listener mStatusListener = new myStatusListener();
	private final OTSServiceContext context;

	public OTSServiceRuntime(OTSServiceContext context) {
		this.context = context;
		this.service = context.getService();
		this.config = new OTSServiceConfig(context.getConfig());
		this.task = new OTSServiceTask(context.getTask());
	}

	public OTSServiceContext getServiceContext() {
		return this.context;
	}

	public void open() {
		// set listeners
		LocationManager lm = (LocationManager) service
				.getSystemService(Context.LOCATION_SERVICE);
		lm.addGpsStatusListener(this.getGpsStatusListener());
		String provider = LocationManager.GPS_PROVIDER;
		long minTime = 1000;
		float minDistance = 8;
		LocationListener listener = this.getGpsDataListener();
		lm.requestLocationUpdates(provider, minTime, minDistance, listener);
		// update status
		OTSServiceStatus status = context.getStatus();
		status.setRunning(true);
		status.setTaskStartTime(task.getStartTime());
		status.setTaskId(task.getTaskId());
		context.setStatus(status);
	}

	public void close() {
		// flush data in buffer
		this.flush();
		// update status
		OTSServiceStatus status = context.getStatus();
		status.setRunning(false);
		context.setStatus(status);
		// remove listeners
		LocationManager lm = (LocationManager) service
				.getSystemService(Context.LOCATION_SERVICE);
		lm.removeGpsStatusListener(this.getGpsStatusListener());
		lm.removeUpdates(this.getGpsDataListener());
	}

	private LocationListener getGpsDataListener() {
		return this.mDataListener;
	}

	private Listener getGpsStatusListener() {
		return this.mStatusListener;
	}

	private class myStatusListener implements Listener {

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub

			System.out.println(this + ".onGpsStatusChanged: " + event);
		}
	}

	private static class Tools {

		private static OTSLocation toOTSLocation(Location loc1) {

			OTSLocation loc2 = new OTSLocation();

			loc2.setLongitude(loc1.getLongitude());
			loc2.setLatitude(loc1.getLatitude());
			loc2.setAltitude(loc1.getAltitude());

			loc2.setAccuracy(loc1.getAccuracy());
			loc2.setBearing(loc1.getBearing());
			loc2.setSpeed(loc1.getSpeed());

			loc2.setProvider(loc1.getProvider());
			loc2.setCoordinateSystem("WGS84");
			loc2.setSatelliteTime(new SateTime(loc1.getTime()));
			loc2.setDeviceTime(System.currentTimeMillis());

			return loc2;
		}

		public static long timeCode(long time) {
			return time -= (time % (60 * 1000));
		}

	}

	private class myDataListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			final OTSLocation loc2 = Tools.toOTSLocation(location);
			final OTSServiceRuntime self = OTSServiceRuntime.this;
			self.sendLocation(loc2);
			self.saveLocation(loc2);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			System.out.println(this + ".onStatusChanged: " + status);

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

			System.out.println(this + ".onProviderEnabled: " + provider);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

			System.out.println(this + ".onProviderDisabled: " + provider);
		}
	}

	private void sendLocation(OTSLocation loc2) {
		// TODO Auto-generated method stub

		config.getClass();

	}

	private void saveLocation(OTSLocation loc2) {
		final List<OTSLocation> buf = locBuffer;
		final int len = buf.size();
		boolean saved = false;
		if (len > 0) {
			final OTSLocation loc1 = buf.get(0);
			long timeCode1 = Tools.timeCode(loc1.getDeviceTime());
			long timeCode2 = Tools.timeCode(loc2.getDeviceTime());
			if ((timeCode1 != timeCode2) || (len > 256)) {
				// flush buffer
				this.flush();
				this.trimQueue(100);
				saved = true;
			}
		}
		locQueue.add(loc2);
		buf.add(loc2);
		OTSServiceStatus status = context.getStatus();
		status.setTaskId(task.getTaskId());
		status.setTaskStartTime(task.getStartTime());
		status.setLocation(loc2);
		status.setCountLocation(status.getCountLocation() + 1);
		context.setStatus(status);
		if (saved) {
			context.saveStatus();
		}
	}

	private void trimQueue(int lengthLimit) {
		lengthLimit = Math.max(lengthLimit, 0);
		Queue<OTSLocation> queue = this.locQueue;
		for (;;) {
			final int len = queue.size();
			if (len > lengthLimit) {
				queue.poll();
			} else {
				break;
			}
		}
	}

	private void flush() {
		final List<OTSLocation> buf = locBuffer;
		if (buf.size() <= 0) {
			return;
		}
		LocationFragmentFormat lff = new LocationFragmentFormat();
		lff.setList(buf);
		lff.setTaskId(task.getTaskId());
		LocationFragmentFormat.normal(lff);
		String js = gson.toJson(lff);
		OTSLocation loc = buf.get(0);
		File dir = fileMan.getFragmentDir();
		File file = new File(dir, "loc-frag-" + loc.getDeviceTime() + ".txt");
		try {
			StringTools.saveString(js, null, file, true);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buf.clear();
		}
	}

	public OTSLocation[] getLastLocations(int limitCount) {
		limitCount = Math.max(limitCount, 0);
		Queue<OTSLocation> list = new LinkedList<OTSLocation>(this.locQueue);
		for (;;) {
			final int len = list.size();
			if (len > limitCount) {
				list.poll();
			} else {
				break;
			}
		}
		return list.toArray(new OTSLocation[list.size()]);
	}

}
