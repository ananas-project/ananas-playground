package ananas.app.droid_location_monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.startActivity(new Intent(this, OtsMonitorActivity.class));
	}

}
