package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolable;

public abstract class JsonValue implements IJsonValue, IPoolable {

	private IPool mPool;
	private boolean mIsFree = true;

	protected JsonValue() {
	}

	@Override
	public void onFree() {
		this.mIsFree = true;
	}

	@Override
	public void onAlloc() {
		this.mIsFree = false;
	}

	@Override
	public void free() {
		if (!this.mIsFree) {
			this.onFree();
			this.mPool.dealloc(this);
		}
	}

	@Override
	public boolean isFree() {
		return this.mIsFree;
	}

	@Override
	public void bindToPool(IPool pool) {
		if (this.mPool == null) {
			this.mPool = pool;
		}
	}

	@Override
	public IPool getPool() {
		return this.mPool;
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

}
