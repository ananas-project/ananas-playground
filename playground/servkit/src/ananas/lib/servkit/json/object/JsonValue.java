package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;
import ananas.lib.servkit.monitor.IMonitorProbe;
import ananas.lib.servkit.monitor.MonitorAgent;
import ananas.lib.servkit.pool.AbstractPoolingProbe;
import ananas.lib.servkit.pool.IPoolable;
import ananas.lib.servkit.pool.IProbe;

public abstract class JsonValue implements IJsonValue {

	private final IMonitorProbe mMoProbe;

	protected JsonValue() {
		this.mMoProbe = MonitorAgent.getAgent().newProbe(this);
	}

	private static class JsonNull extends JsonValue {

		@Override
		public void output(IJsonHandler h) throws JsonException {
			h.onNull();
		}

	}

	private static class JsonTrue extends JsonValue {

		@Override
		public void output(IJsonHandler h) throws JsonException {
			h.onBoolean(true);
		}

	}

	private static class JsonFalse extends JsonValue {

		@Override
		public void output(IJsonHandler h) throws JsonException {
			h.onBoolean(false);
		}

	}

	public final static JsonValue value_true = new JsonTrue();
	public final static JsonValue value_false = new JsonFalse();
	public final static JsonValue value_null = new JsonNull();

	@Override
	public abstract void output(IJsonHandler h) throws JsonException;

	protected void onAlloc() {
		this.mMoProbe.print("onAlloc");
	}

	protected void onFree() {
		this.mMoProbe.print("onFree");
	}

	@Override
	public void free() {
		this.mProbe.free();
	}

	private final IProbe mProbe = new AbstractPoolingProbe() {

		@Override
		public void onAlloc() {
			super.onAlloc();
			JsonValue.this.onAlloc();
		}

		@Override
		public void onFree() {
			super.onFree();
			JsonValue.this.onFree();
		}

		@Override
		public IPoolable toPoolable() {
			return JsonValue.this;
		}

	};

	@Override
	public IProbe toProbe() {
		return this.mProbe;
	}

}
