package ananas.app.droid_location_monitor;

import android.app.Activity;

public interface ActivityAgent {

	Activity getActivity();

	void onServiceConnected();

	void onServiceDisconnected();

}
