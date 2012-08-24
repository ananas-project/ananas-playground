package ananas.lib.servkit.json.object;

import java.util.HashMap;
import java.util.Map.Entry;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonObject extends JsonValue implements IJsonObject {

	private final HashMap<String, JsonValue> mTable = new HashMap<String, JsonValue>();

	public JsonObject() {
	}

	@Override
	public void put(String key, IJsonValue value) {
		this.mTable.put(key, (JsonValue) value);
	}

	@Override
	public void onFree() {
		super.onFree();
		for (JsonValue item : this.mTable.values()) {
			item.free();
		}
		this.mTable.clear();
	}

	@Override
	public void onAlloc() {
		super.onAlloc();
		this.mTable.clear();
	}

	@Override
	public void output(IJsonHandler h) throws JsonException {
		h.onObjectBegin();
		for (Entry<String, JsonValue> item : this.mTable.entrySet()) {
			h.onObjectKey(item.getKey());
			item.getValue().output(h);
		}
		h.onObjectEnd();
	}
}
