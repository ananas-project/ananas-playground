package ananas.app.droid_location_monitor;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class DateTimeAdjustActivity extends Activity {

	private LocationManager mLocationMan;

	private long mTimeBegin;

	private TextView mTextOutput;

	private int mLocationIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_adj);

		this.mLocationMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		this.mTextOutput = (TextView) this.findViewById(R.id.text_output);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this._removeLocationListener();
		this._stopTimer();
	}

	@Override
	protected void onResume() {
		super.onResume();
		long now = System.currentTimeMillis();
		this.mTimeBegin = now - (now % 1000);
		this.mLocationIndex = 0;
		this._setLocationListener();
		this._startTimer();
	}

	private void _startTimer() {
		class MyTimer implements Runnable {

			private final Runnable mRunnOnUI = new Runnable() {

				@Override
				public void run() {
					DateTimeAdjustActivity.this.onTimer();
				}
			};

			@Override
			public void run() {
				for (;;) {
					Runnable other = DateTimeAdjustActivity.this.mTimer;
					if (!this.equals(other)) {
						break;
					}
					this.fire();
					this.sleep();
				}
			}

			private void sleep() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			private void fire() {
				DateTimeAdjustActivity.this.runOnUiThread(this.mRunnOnUI);
			}
		}
		Runnable timer = new MyTimer();
		this.mTimer = timer;
		(new Thread(timer)).start();
	}

	protected void onTimer() {
		final long now = System.currentTimeMillis();
		final StringBuilder sb = new StringBuilder();
		final String CRLF = "\r\n";
		{
			// ms part
			sb.append(this.getMsPartString(now) + CRLF);
			// now
			sb.append("now : " + this.timeToString(now) + CRLF);
			// span
			long span = (now - this.mTimeBegin) / 1000;
			sb.append("task.span : " + span + "s" + CRLF);
		}
		{
			final MyLocation ll = this.mLastLocation;
			if (ll != null) {
				// index
				sb.append("location.index : " + ll.mIndex + CRLF);
				// age
				long age = (now - ll.mTime) / 1000;
				sb.append("location.age : " + age + "s" + CRLF);
				// accuracy
				float accuracy = ll.getAccuracy();
				sb.append("location.accuracy : " + accuracy + "m" + CRLF);

				// gps-time
				sb.append("location.time : " + this.timeToString(ll.getTime())
						+ CRLF);
				// phone-time
				sb.append("phone.time : " + this.timeToString(ll.mTime) + CRLF);
				// gps-phone-time
				long gps_phone_time = ll.getTime() - ll.mTime;
				sb.append("gps-phone-time : " + gps_phone_time + "ms" + CRLF);
			}
			{
				// ?now=1234& ;
			}
			// sb.append("ll : " + ll + CRLF);
		}
		this.mTextOutput.setText(sb.toString());
	}

	private String getMsPartString(long now) {
		int ptr = (int) ((now % 1000) / 100);
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < 10; i++) {
			if (i == ptr) {
				sb.append("[" + i + "]");
			} else {
				sb.append("_" + i + "_");
			}
		}
		return sb.toString();
	}

	private String timeToString(long time) {
		Calendar cale = Calendar.getInstance();
		cale.setTimeInMillis(time);

		int yy = cale.get(Calendar.YEAR);
		int mm = cale.get(Calendar.MONTH);
		int dd = cale.get(Calendar.DAY_OF_MONTH);

		int h = cale.get(Calendar.HOUR_OF_DAY);
		int m = cale.get(Calendar.MINUTE);
		int s = cale.get(Calendar.SECOND);

		return (yy + "-" + mm + "-" + dd + " " + h + ":" + m + ":" + s + " ");
	}

	private void _stopTimer() {
		this.mTimer = null;
	}

	private Runnable mTimer;

	private void _setLocationListener() {

		String provider = LocationManager.GPS_PROVIDER;
		long minTime = 1000;
		float minDistance = 5;
		LocationListener listener = this.mGpsListener;
		this.mLocationMan.requestLocationUpdates(provider, minTime,
				minDistance, listener);

	}

	private void _removeLocationListener() {
		this.mLocationMan.removeUpdates(this.mGpsListener);
	}

	private class MyLocation extends Location {

		private final long mTime;
		private final int mIndex;

		public MyLocation(long time, Location l) {
			super(l);
			this.mIndex = (DateTimeAdjustActivity.this.mLocationIndex++);
			this.mTime = time;
		}
	}

	MyLocation mLastLocation = null;

	private final LocationListener mGpsListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			long time = System.currentTimeMillis();
			DateTimeAdjustActivity.this.mLastLocation = new MyLocation(time,
					location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

}
