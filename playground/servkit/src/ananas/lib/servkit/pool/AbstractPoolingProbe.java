package ananas.lib.servkit.pool;

public abstract class AbstractPoolingProbe implements IProbe {

	private boolean mIsFree;

	@Override
	public void free() {
		this.onFree();
	}

	@Override
	public boolean isFree() {
		return this.mIsFree;
	}

	@Override
	public void onAlloc() {
		this.mIsFree = false;
	}

	@Override
	public void onFree() {
		this.mIsFree = true;
	}

}
