package ananas.app.droid_location_monitor.core;

import java.io.File;

public class WorkingDirectory {

	public static File getApplicationDirectory() {
		File dir = android.os.Environment.getExternalStorageDirectory();
		dir = new File(dir, "ananas/droid-location-monitor");
		return dir;
	}

	public static File getRecordDirectory() {
		File dir = getApplicationDirectory();
		dir = new File(dir, "record");
		return dir;
	}

}
