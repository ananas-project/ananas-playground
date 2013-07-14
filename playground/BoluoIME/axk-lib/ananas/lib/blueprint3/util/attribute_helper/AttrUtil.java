package ananas.lib.blueprint3.util.attribute_helper;

import ananas.lib.blueprint3.dom.BPAttribute;

public class AttrUtil {

	public static int getInt(BPAttribute attr) {
		if (attr == null) {
			return 0;
		}
		String value = attr.getValue();
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}

	public static boolean getBool(BPAttribute attr) {
		if (attr == null) {
			return false;
		}
		String value = attr.getValue();
		if (value == null) {
			return false;
		}

		if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")
				|| value.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getString(BPAttribute attr) {
		if (attr == null) {
			return "";
		}
		String value = attr.getValue();
		if (value == null) {
			return "";
		}
		return value;
	}

}
