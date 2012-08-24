package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonLong extends JsonNumber implements IJsonLong {

	private long mValue;

	public JsonLong() {
	}

	@Override
	public void setValue(long value) {
		this.mValue = value;
	}

	@Override
	public long getValue() {
		return this.mValue;
	}

	@Override
	public void onFree() {
		super.onFree();
	}

	@Override
	public void onAlloc() {
		super.onAlloc();
		this.mValue = 0;
	}

	@Override
	public void output(IJsonHandler h) throws JsonException {
		h.onLong(mValue);
	}
}
