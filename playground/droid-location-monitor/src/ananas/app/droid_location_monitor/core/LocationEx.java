package ananas.app.droid_location_monitor.core;

import android.location.Location;

public interface LocationEx {

	Location toNative();

	long getPhoneTime();
}
