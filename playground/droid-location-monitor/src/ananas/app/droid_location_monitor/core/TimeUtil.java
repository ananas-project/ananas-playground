package ananas.app.droid_location_monitor.core;

public class TimeUtil {

	public static String timespanToString(long ms) {
		int d, h, m, s;
		h = (int) ((ms / (1000 * 3600)) % 24);
		m = (int) ((ms / (1000 * 60)) % 60);
		s = (int) ((ms / (1000)) % 60);
		d = (int) (ms / (1000 * 3600 * 24));
		String hms = h + ":" + m + ":" + s;
		if (d == 0) {
			return hms;
		} else {
			return (d + "day(s)" + hms);
		}
	}

	public static String timestampToString(long ms) {
		// TODO Auto-generated method stub
		return ms + "";
	}

}
