package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public class JsonString extends JsonValue implements IJsonString {

	private String mData;

	@Override
	public void setData(String s) {
		this.mData = s;
	}

	@Override
	public String getData() {
		return this.mData;
	}

	@Override
	public void output(IJsonHandler h) throws JsonException {
		String dat = this.mData;
		if (dat == null) {
			h.onNull();
		} else {
			h.onString(dat);
		}
	}

	@Override
	protected void onAlloc() {
		this.mData = null;
	}

	@Override
	protected void onFree() {
		// TODO Auto-generated method stub

	}

}
