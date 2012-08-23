package ananas.lib.servkit.json.object;

import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolable;

public class JsonValue implements IJsonValue, IPoolable {

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
			this.mPool.free(this);
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

}
