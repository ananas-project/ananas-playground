package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.SateTime;
import android.app.Service;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class OTSRuntimeDriver {

	private final Service service;
	private final LocationListener mDataListener = new myDataListener();
	private final Listener mStatusListener = new myStatusListener();
	private final OTSRuntime runtime;

	public OTSRuntimeDriver(OTSServiceContext context, OTSRuntime rt) {
		this.service = context.getService();
		this.runtime = rt;
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
		runtime.onOpen();
	}

	public void close() {
		runtime.onClose();
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

	}

	private class myDataListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			final OTSLocation loc2 = Tools.toOTSLocation(location);
			runtime.onLocation(loc2);
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

}
