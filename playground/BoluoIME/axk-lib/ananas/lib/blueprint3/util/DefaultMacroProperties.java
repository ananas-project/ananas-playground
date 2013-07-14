package ananas.lib.blueprint3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultMacroProperties implements IMacroProperties {

	private final Map<String, String> mTable;

	public DefaultMacroProperties() {
		this.mTable = new HashMap<String, String>();
	}

	@Override
	public void put(String key, String value) {
		this.mTable.put(key, value);
	}

	@Override
	public String get(String key) {
		return this._get(key, true, null, false);
	}

	@Override
	public String get(String key, boolean canBeNull, String defaultValue) {
		return this._get(key, canBeNull, defaultValue, false);
	}

	@Override
	public String get(String key, boolean canBeNull, String defaultValue,
			boolean processMacro) {
		return this._get(key, canBeNull, defaultValue, processMacro);
	}

	private String _get(String key, boolean canBeNull, String defaultValue,
			boolean processMacro) {

		String value = this.mTable.get(key);
		if (value == null) {
			if (!canBeNull) {
				throw new RuntimeException("The value of key is null. [key]="
						+ key);
			}
			value = defaultValue;
		}

		if (processMacro) {
			value = this.processMacro(value);
		}
		return value;
	}

	interface Macro {
		String begin = "$(";
		String end = ")";
	}

	private String _processMacro1(String text) {

		final int i1 = text.indexOf(Macro.begin);
		if (i1 < 0) {
			return text;
		}

		final int i2 = text.indexOf(Macro.end, i1);
		if (i2 < 0) {
			throw new RuntimeException(
					"The macro of string not closed. [string]=" + text);
		}

		String p1 = text.substring(0, i1);
		String p2 = text.substring(i1 + Macro.begin.length(), i2);
		String p3 = text.substring(i2 + Macro.end.length());

		p2 = this.mTable.get(p2);

		return (p1 + p2 + p3);
	}

	@Override
	public Set<String> keySet() {
		return this.mTable.keySet();
	}

	@Override
	public String processMacro(String value) {
		for (int i = 64; value.contains(Macro.begin); i--) {
			if (i < 0) {
				throw new RuntimeException(
						"The macro reference is too deep.[text]=" + value);
			}
			value = this._processMacro1(value);
		}
		return value;
	}

}
