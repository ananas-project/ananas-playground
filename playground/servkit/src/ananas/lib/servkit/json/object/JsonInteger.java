package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonInteger extends JsonNumber implements IJsonInteger {

	private int mValue;

	public JsonInteger() {
	}

	@Override
	public void setValue(int value) {
		this.mValue = value;
	}

	@Override
	public int getValue() {
		return this.mValue;
	}

	@Override
	public void onFree() {

	}

	@Override
	public void onAlloc() {
		this.mValue = 0;
	}

	@Override
	public void output(IJsonHandler h) throws JsonException {
		h.onInteger(mValue);
	}

}
