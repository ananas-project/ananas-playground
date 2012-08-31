package ananas.lib.servkit.pool;

import ananas.lib.servkit.monitor.IMonitorProbe;
import ananas.lib.servkit.monitor.MonitorAgent;

public class DefaultCachedPoolFactory implements ISinglePoolFactory {

	private class MyPool implements ISinglePool {

		private final int mSize;
		private final IPoolableFactory mItemFactory;
		private final boolean mResetable;
		private final IProbe[] mArrayAll;
		private final IProbe[] mArrayFree;
		private int mCountAll;
		private int mCountFree;
		private final IMonitorProbe mMoProbe;

		public MyPool(IPoolableFactory itemFactory, int size, boolean resetable) {

			this.mSize = size;
			this.mItemFactory = itemFactory;
			this.mResetable = resetable;

			this.mArrayAll = new IProbe[size];
			this.mArrayFree = new IProbe[size];

			this.mCountAll = 0;
			this.mCountFree = 0;

			this.mMoProbe = MonitorAgent.getAgent().newProbe(this);
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			IProbe obj = this._alloc();
			obj.onAlloc();
			return obj.toPoolable();
		}

		private/* synchronized */IProbe _alloc() {
			IProbe obj = null;
			if (this.mCountFree <= 0) {
				this._recycle();
			}
			if (this.mCountFree > 0) {
				obj = this.mArrayFree[--this.mCountFree];
			} else {
				obj = this._PureNew();
			}
			return obj;
		}

		private void _recycle() {
			int cnt = 0;
			int index = 0;
			for (IProbe item : this.mArrayAll) {
				if (item != null) {
					if (item.isFree()) {
						this.mArrayFree[cnt++] = item;
					}
				}
				index++;
				if (index >= this.mCountAll) {
					break;
				}
			}
			this.mCountFree = cnt;
		}

		@Override
		public int getSize() {
			return this.mSize;
		}

		@Override
		public void reset() {
			if (!this.mResetable) {
				throw new RuntimeException("no support reset.");
			}
			for (IProbe item : this.mArrayAll) {
				if (item != null) {
					item.free();
				}
			}
		}

		private IProbe _PureNew() {
			try {
				IPoolable poolable = this.mItemFactory.newObject();
				IProbe probe = poolable.toProbe();
				if (this.mCountAll < this.mSize) {
					this.mArrayAll[this.mCountAll++] = probe;
				}
				if (this.mMoProbe.enable()) {
					this.mMoProbe.print("new " + poolable);
				}
				return probe;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public ISinglePool newPool(IPoolableFactory itemFactory, int size,
			boolean resetable) {
		return new MyPool(itemFactory, size, resetable);
	}

}
