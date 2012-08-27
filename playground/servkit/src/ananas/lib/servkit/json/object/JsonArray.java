package ananas.lib.servkit.json.object;

import java.util.ArrayList;
import java.util.List;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonArray extends JsonValue implements IJsonArray {

	private List<JsonValue> mArray = new ArrayList<JsonValue>();

	public JsonArray() {
	}

	@Override
	public void add(IJsonValue child) {
		this.mArray.add((JsonValue) child);
	}

	@Override
	public void onFree() {

		for (JsonValue item : this.mArray) {
			item.free();
		}
		this.mArray.clear();
	}

	@Override
	public void onAlloc() {

		this.mArray.clear();
	}

	@Override
	public void output(IJsonHandler h) throws JsonException {
		h.onArrayBegin();
		for (IJsonValue item : this.mArray) {
			item.output(h);
		}
		h.onArrayEnd();
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IJsonValue get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
