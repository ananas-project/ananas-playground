package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import android.app.Service;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class OTSServiceRuntime {

	private final OTSServiceConfig config;
	private final OTSServiceStatus status;
	private final Service service;

	private final LocationListener mDataListener = new myDataListener();
	private final Listener mStatusListener = new myStatusListener();

	public OTSServiceRuntime(Service service, OTSServiceConfig config,
			OTSServiceStatus status) {
		this.service = service;
		this.config = new OTSServiceConfig(config);
		this.status = new OTSServiceStatus(status);
	}

	public void open() {

		LocationManager lm = (LocationManager) service
				.getSystemService(Context.LOCATION_SERVICE);
		lm.addGpsStatusListener(this.getGpsStatusListener());
		String provider = LocationManager.GPS_PROVIDER;
		long minTime = 1000;
		float minDistance = 8;
		LocationListener listener = this.getGpsDataListener();
		lm.requestLocationUpdates(provider, minTime, minDistance, listener);

	}

	public void close() {

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

	public OTSServiceStatus getStatus() {
		return this.status;
	}

	private class myStatusListener implements Listener {

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub

			System.out.println(this + ".onGpsStatusChanged: " + event);
		}
	}

	private class myDataListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			System.out.println(this + ".onGpsStatusChanged: " + location);

			OTSLocation loc = this.toOTSLocation(location);

			OTSServiceRuntime.this.status.setLocation(loc);

		}

		private OTSLocation toOTSLocation(Location loc1) {

			OTSLocation loc2 = new OTSLocation();
			loc2.setLongitude(loc1.getLongitude());
			loc2.setLatitude(loc1.getLatitude());
			loc2.setAltitude(loc1.getAltitude());
			loc2.setAccuracy(loc1.getAccuracy());
			loc2.setTimestamp(loc1.getTime());

			return loc2;

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
