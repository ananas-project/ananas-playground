package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.lang.BPFileNameMapper;

public class BPFileNameMapperImpl implements BPFileNameMapper {

	private final Map<String, String> mTable = new HashMap<String, String>();

	@Override
	public void register(String dotName, String contentType) {
		dotName = this._normalDotName(dotName);
		this.mTable.put(dotName, contentType);
	}

	private String _normalDotName(String str) {
		str = str.trim().toLowerCase();
		if (str.startsWith(".")) {
			return str;
		} else {
			return "." + str;
		}
	}

	@Override
	public String getContentType(String dotName) {
		dotName = this._normalDotName(dotName);
		String type = this.mTable.get(dotName);
		return type;
	}

}
