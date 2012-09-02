package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonDouble extends JsonNumber implements IJsonDouble {

	private double mValue;

	public JsonDouble() {
	}

	@Override
	public void setValue(double value) {
		this.mValue = value;
	}

	@Override
	public double getValue() {
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
		h.onDouble(this.mValue);
	}

	@Override
	public int getIntValue() {
		// return this.mValue;
		throw new RuntimeException("the value out of range : " + this.mValue);
	}

	@Override
	public long getLongValue() {
		// return this.mValue;
		throw new RuntimeException("the value out of range : " + this.mValue);
	}

	@Override
	public double getDoubleValue() {
		return this.mValue;
	}
}
