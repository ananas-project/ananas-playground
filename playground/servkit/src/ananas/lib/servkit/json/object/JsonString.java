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
		h.onString(this.mData);
	}

	@Override
	protected void onAlloc() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFree() {
		// TODO Auto-generated method stub
		
	}

}
