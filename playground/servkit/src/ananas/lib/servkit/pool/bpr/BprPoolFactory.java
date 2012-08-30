package ananas.lib.servkit.pool.bpr;

import ananas.lib.servkit.pool.IPool;

public class BprPoolFactory implements IBprPool {

	private IBprPool mTargetPool;

	public boolean bind(Object child) {
		if (child instanceof IBprPool) {
			this.mTargetPool = ((IBprPool) child);
			return true;
		}
		return false;
	}

	@Override
	public IPool createPool() {
		return this.mTargetPool.createPool();
	}

}
