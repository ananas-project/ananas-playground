package ananas.app.droid_location_monitor.core;

import java.util.Calendar;
import java.util.TimeZone;

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

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(ms);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);

		StringBuilder sb = new StringBuilder();

		sb.append(_int2str(year, 4));
		sb.append(_int2str(month, 2));
		sb.append(_int2str(day, 2));
		sb.append("_");
		sb.append(_int2str(h, 2));
		sb.append(_int2str(m, 2));
		sb.append(_int2str(s, 2));

		return sb.toString();
	}

	private static Object _int2str(int n, int width) {
		String str = n + "";
		if (n > 0) {
			while (str.length() < width) {
				str = "0" + str;
			}
		}
		return str;
	}
}
